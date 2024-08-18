package com.game.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.dto.ConfigFile;
import com.game.service.impl.MatrixServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MatrixServiceTest {

    @InjectMocks
    private MatrixServiceImpl matrixService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static Stream<Object[]> matrixParameters() throws Exception {
        ConfigFile configFile = objectMapper.readValue(new File("src/test/resources/config.json"), ConfigFile.class);

        var symbol = configFile.getSymbols().get("10x");
        var standardSymbols = configFile.getProbabilities().getStandardSymbols();
        return Stream.of(
            new Object[]{3, 3, standardSymbols, symbol.getType(), symbol},
            new Object[]{4, 4, standardSymbols, symbol.getType(), symbol},
            new Object[]{5, 5, standardSymbols, symbol.getType(), symbol}
        );
    }

    @ParameterizedTest
    @MethodSource("matrixParameters")
    void testGenerateMatrix(int rows, int columns,
                            List<ConfigFile.Probabilities.SymbolProbability> symbolWeights,
                            String bonusSymbolName, ConfigFile.Symbol bonusSymbol) {
        List<List<String>> result = matrixService.generateMatrix(rows, columns, symbolWeights, bonusSymbolName, bonusSymbol);

        assertNotNull(result, "Matrix should not be null");
        assertEquals(rows, result.size(), "Number of rows should match the specified amount");
        result.forEach(row -> assertEquals(columns, row.size(), "Number of columns in each row should match the specified amount"));
    }

}
