package com.game.service.impl;

import com.game.dto.ConfigFile;
import com.game.dto.OutputResult;
import com.game.service.MatrixService;
import com.game.service.ScratchGameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.game.util.ScratchGameConstant.*;

public class ScratchGameServiceImpl implements ScratchGameService {
    private final ConfigFile configFile;
    private final Map<String, List<String>> winCombinationGroup = new HashMap<>();
    private final MatrixService matrixService;

    public ScratchGameServiceImpl(ConfigFile conf, MatrixService matrixGenerator) {
        this.configFile = conf;
        this.matrixService = matrixGenerator;
        conf.getWinCombinations().forEach((key, value) -> addWinningCombination(winCombinationGroup, value.getGroup(), key));
    }

    @Override
    public OutputResult spin(double wagerAmount) {
        OutputResult result = new OutputResult();
        String bonusSymbolKey = matrixService.generateSymbol(configFile.getProbabilities().getBonusSymbols());
        ConfigFile.Symbol bonusSymbolData = configFile.getSymbols().get(bonusSymbolKey);
        List<List<String>> generatedMatrix = matrixService.generateMatrix(
                configFile.getRows(),
                configFile.getColumns(),
                configFile.getProbabilities().getStandardSymbols(),
                bonusSymbolKey,
                bonusSymbolData
        );
        result.setMatrix(generatedMatrix);

        Map<String, Integer> symbolFrequencyMap = new HashMap<>();
        generatedMatrix.forEach(row -> row.forEach(symbol -> {
            if (symbol != null) {
                symbolFrequencyMap.merge(symbol, 1, Integer::sum);
            }
        }));

        double totalReward = 0;

        for (String symbol : getStandardSymbolsList(configFile)) {
            double currentSymbolReward = wagerAmount * configFile.getSymbols().get(symbol).getRewardMultiplier();
            boolean hasWinningCombination = false;

            for (List<String> winningCombinationGroup : winCombinationGroup.values()) {
                for (String winningCombinationKey : winningCombinationGroup) {
                    ConfigFile.WinCombination winCombinationData = configFile.getWinCombinations().get(winningCombinationKey);

                    if (WIN_COMBINATION_SAME_SYMBOLS_CASE.equals(winCombinationData.getWhen()) &&
                            winCombinationData.getCount() == symbolFrequencyMap.getOrDefault(symbol, 0)) {
                        currentSymbolReward *= winCombinationData.getRewardMultiplier();
                        hasWinningCombination = true;
                        addWinningCombination(result.getAppliedWinningCombinations(), symbol, winningCombinationKey);
                        break;
                    } else if (WIN_COMBINATION_LINEAR_SYMBOLS_CASE.equals(winCombinationData.getWhen())) {
                        boolean matchInArea = false;
                        for (List<String> area : winCombinationData.getCoveredAreas()) {
                            matchInArea = area.stream().allMatch(cell -> {
                                String[] coordinates = cell.split(":");
                                return symbol.equals(generatedMatrix.get(Integer.parseInt(coordinates[0])).get(Integer.parseInt(coordinates[1])));
                            });
                            if (matchInArea) {
                                break;
                            }
                        }
                        if (matchInArea) {
                            hasWinningCombination = true;
                            currentSymbolReward *= winCombinationData.getRewardMultiplier();
                            addWinningCombination(result.getAppliedWinningCombinations(), symbol, winningCombinationKey);
                            break;
                        }
                    }
                }
            }
            if (hasWinningCombination) {
                totalReward += currentSymbolReward;
            }
        }

        if (totalReward > 0 && !MISS_VALUE.equals(bonusSymbolData.getImpact())) {
            if (MULTIPLY_REWARD_SYMBOL_IMPACT_TYPE.equals(bonusSymbolData.getImpact())) {
                totalReward *= bonusSymbolData.getRewardMultiplier();
            } else if (EXTRA_BONUS_SYMBOL_IMPACT_TYPE.equals(bonusSymbolData.getImpact())) {
                totalReward += bonusSymbolData.getExtra();
            }
            result.setAppliedBonusSymbol(bonusSymbolKey);
        }

        result.setReward(totalReward);
        return result;
    }


    private static void addWinningCombination(Map<String, List<String>> output, String symbol, String winningCombination) {
        output.putIfAbsent(symbol, new ArrayList<>());
        output.get(symbol).add(winningCombination);
    }

    private static List<String> getStandardSymbolsList(ConfigFile conf) {
        return conf.getSymbols().entrySet().stream()
                .filter(entry -> SYMBOL_TYPE_STANDARD.equals(entry.getValue().getType()))
                .map(Map.Entry::getKey)
                .toList();
    }
}