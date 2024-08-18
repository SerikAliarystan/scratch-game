package com.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ConfigFile {
    @Min(1)
    private int columns;
    @Min(1)
    private int rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations;

    @Data
    public static class Symbol {
        @JsonProperty("reward_multiplier")
        @Min(0)
        private double rewardMultiplier;
        private String type;
        private String impact = "none";
        private Double extra = 0.0;
    }

    @Data
    public static class Probabilities {
        @JsonProperty("standard_symbols")
        private List<SymbolProbability> standardSymbols;
        @JsonProperty("bonus_symbols")
        private SymbolProbability bonusSymbols;

        @Data
        public static class SymbolProbability {
            @Min(0)
            private int column;
            @Min(0)
            private int row;
            private Map<String, Integer> symbols;
        }
    }

    @Data
    public static class WinCombination {
        @JsonProperty("reward_multiplier")
        @Min(0)
        private double rewardMultiplier;
        private String when;
        private String group;
        @Min(0)
        private int count;
        @JsonProperty("covered_areas")
        private List<List<String>> coveredAreas = new ArrayList<>();
    }
}