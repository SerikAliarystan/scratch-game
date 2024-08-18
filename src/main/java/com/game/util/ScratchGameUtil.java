package com.game.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.dto.ConfigFile;
import com.game.dto.OutputResult;
import com.game.exception.ScratchGameException;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.game.exception.ScratchGameExceptionMessage.JSON_PARSE_EXCEPTION;
import static com.game.util.ScratchGameConstant.MISS_VALUE;

@UtilityClass
public class ScratchGameUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, String> getMapArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            argsMap.put(args[i].replaceFirst("^--", ""), args[i + 1]);
        }
        return argsMap;
    }

    public static String getPropOrThrow(Map<String, String> argsMap, String key, String errorMessage) {
        String value = argsMap.get(key);
        if (value == null) {
            throw new ScratchGameException(errorMessage);
        }
        return value;
    }

    public static void printOutput(OutputResult outputResult) {
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(System.out, outputResult);
        } catch (Exception e) {
            throw new ScratchGameException(JSON_PARSE_EXCEPTION);
        }
    }

    public static ConfigFile getConfig(String config) {
        try {
            return MAPPER.readValue(new File(config), ConfigFile.class);
        } catch (Exception e) {
            throw new ScratchGameException(JSON_PARSE_EXCEPTION);
        }
    }


    public static List<List<String>> generateListWithMissValue(int rows, int columns) {
        final List<List<String>> matrix = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                row.add(MISS_VALUE);
            }
            matrix.add(row);
        }
        return matrix;
    }

}
