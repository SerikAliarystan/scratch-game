package com.game.service.impl;

import com.game.dto.ConfigFile;
import com.game.service.MatrixService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.game.util.ScratchGameConstant.MISS_VALUE;
import static com.game.util.ScratchGameUtil.generateListWithMissValue;

public class MatrixServiceImpl implements MatrixService {


    @Override
    public List<List<String>> generateMatrix(final int rows, final int columns,
                                             final List<ConfigFile.Probabilities.SymbolProbability> symbolWeights,
                                             final String bonusSymbolName, final ConfigFile.Symbol bonusSymbol) {
        final List<List<String>> matrix = generateListWithMissValue(rows, columns);

        fillMatrix(symbolWeights, matrix);

        if (!MISS_VALUE.equals(bonusSymbol.getImpact())) {
            final Random random = new SecureRandom();
            matrix.get(random.nextInt(rows))
                    .set(random.nextInt(columns), bonusSymbolName);
        }

        return matrix;
    }

    @Override
    public String generateSymbol(final ConfigFile.Probabilities.SymbolProbability symbolProbability) {
        final int totalWeight = symbolProbability.getSymbols().values().stream().reduce(0, Integer::sum);
        int randomIdx = new SecureRandom().nextInt(totalWeight);
        String symbol = null;
        for (Map.Entry<String, Integer> entry : symbolProbability.getSymbols().entrySet()) {
            if (randomIdx < entry.getValue()) {
                symbol = entry.getKey();
                break;
            }
            randomIdx -= entry.getValue();
        }
        return symbol;
    }

    private void fillMatrix(List<ConfigFile.Probabilities.SymbolProbability> symbolWeights, List<List<String>> matrix) {
        for (ConfigFile.Probabilities.SymbolProbability symbolProbability : symbolWeights) {
            String symbol = generateSymbol(symbolProbability);
            matrix.get(symbolProbability.getRow())
                    .set(symbolProbability.getColumn(), symbol);
        }
    }
}
