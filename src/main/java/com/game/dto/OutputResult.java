package com.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class OutputResult {
    private List<List<String>> matrix;
    private double reward;
    @JsonProperty("applied_winning_combinations")
    private Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
    @JsonProperty("applied_bonus_symbol")
    private String appliedBonusSymbol;
}
