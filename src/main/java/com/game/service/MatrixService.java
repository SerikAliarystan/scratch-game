package com.game.service;

import com.game.dto.ConfigFile;

import java.util.List;

public interface MatrixService {
    List<List<String>> generateMatrix(int rows, int columns, List<ConfigFile.Probabilities.SymbolProbability> symbolWeights,
                                      String bonusSymbolName, ConfigFile.Symbol bonusSymbol);

    String generateSymbol(ConfigFile.Probabilities.SymbolProbability symbolProbability);
}
