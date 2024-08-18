package com.game.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.dto.ConfigFile;
import com.game.dto.OutputResult;
import com.game.service.impl.ScratchGameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ScratchGameServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private MatrixService matrixService;

    private  ScratchGameServiceImpl scratchGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideSpinTestCases")
    void testSpin(ConfigFile configFile, double wagerAmount, List<List<String>> generatedMatrix,
                  String bonusSymbol, double expectedReward, String expectedBonusSymbol,
                  Map<String, List<String>> expectedWinningCombinations) {
        scratchGameService = new ScratchGameServiceImpl(configFile, matrixService);

        when(matrixService.generateSymbol(configFile.getProbabilities().getBonusSymbols()))
                .thenReturn(bonusSymbol);
        when(matrixService.generateMatrix(
                configFile.getRows(),
                configFile.getColumns(),
                configFile.getProbabilities().getStandardSymbols(),
                bonusSymbol,
                configFile.getSymbols().get(bonusSymbol)
        )).thenReturn(generatedMatrix);

        OutputResult result = scratchGameService.spin(wagerAmount);

        assertEquals(expectedReward, result.getReward(), "The reward should match the expected value");
        assertEquals(expectedBonusSymbol, result.getAppliedBonusSymbol(), "The applied bonus symbol should match the expected value");
        assertEquals(expectedWinningCombinations, result.getAppliedWinningCombinations(), "The winning combinations should match the expected value");
        assertEquals(generatedMatrix, result.getMatrix(), "The generated matrix should match the expected matrix");
    }

    static Stream<Arguments> provideSpinTestCases() throws IOException {
        ConfigFile configFile = objectMapper.readValue(new File("src/test/resources/config.json"), ConfigFile.class);

        // Test case 1: Same symbol repeated 4 times horizontally
        List<List<String>> matrix1 = List.of(
                List.of("A", "A", "A"),
                List.of("B", "C", "D"),
                List.of("E", "F", "A")
        );
        Map<String, List<String>> expectedWinningCombinations1 = Map.of(
                "A", List.of("same_symbol_4_times", "same_symbols_horizontally")
        );

        // Test case 2: Same symbol repeated 4 times
        List<List<String>> matrix2 = List.of(
                List.of("B", "E", "B"),
                List.of("A", "C", "B"),
                List.of("B", "F", "A")
        );
        Map<String, List<String>> expectedWinningCombinations2 = Map.of(
                "B", List.of("same_symbol_4_times")
        );

        // Test case 3: Same symbol repeated vertically
        List<List<String>> matrix3 = List.of(
                List.of("C", "B", "A"),
                List.of("C", "D", "E"),
                List.of("C", "F", "A")
        );
        Map<String, List<String>> expectedWinningCombinations3 = Map.of(
                "C", List.of("same_symbols_vertically", "same_symbol_3_times")
        );

        // Test case 4: Same symbol diagonally from left to right
        List<List<String>> matrix4 = List.of(
                List.of("D", "B", "A"),
                List.of("C", "D", "E"),
                List.of("F", "G", "D")
        );
        Map<String, List<String>> expectedWinningCombinations4 = Map.of(
                "D", List.of("same_symbol_3_times", "same_symbols_diagonally_left_to_right")
        );

        // Test case 5: Same symbol diagonally from right to left
        List<List<String>> matrix5 = List.of(
                List.of("E", "B", "F"),
                List.of("C", "F", "A"),
                List.of("F", "G", "A")
        );
        Map<String, List<String>> expectedWinningCombinations5 = Map.of(
                "F", List.of("same_symbols_diagonally_right_to_left", "same_symbol_3_times")
        );

        // Test case 6: Combination of multiple symbols and bonus symbol
        List<List<String>> matrix6 = List.of(
                List.of("A", "A", "A"),
                List.of("C", "+1000", "A"),
                List.of("C", "C", "A")
        );
        Map<String, List<String>> expectedWinningCombinations6 = Map.of(
                "A", List.of("same_symbols_vertically", "same_symbol_5_times", "same_symbols_horizontally"),
                "C", List.of("same_symbol_3_times")
        );

        return Stream.of(
                Arguments.of(configFile, 100.0, matrix1, "MISS", 1500.0, null, expectedWinningCombinations1),
                Arguments.of(configFile, 100.0, matrix2, "5x", 2250.0, "5x", expectedWinningCombinations2),
                Arguments.of(configFile, 100.0, matrix3, "+500", 1000.0, "+500", expectedWinningCombinations3),
                Arguments.of(configFile, 100.0, matrix4, "10x", 10000.0, "10x", expectedWinningCombinations4),
                Arguments.of(configFile, 100.0, matrix5, "MISS", 500.0, null, expectedWinningCombinations5),
                Arguments.of(configFile, 100.0, matrix6, "+1000", 5250.0, "+1000", expectedWinningCombinations6)
        );
    }

}

