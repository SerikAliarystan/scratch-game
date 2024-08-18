package com.game.util;

import com.game.exception.ScratchGameException;
import com.game.util.ScratchGameConstant;
import com.game.util.ScratchGameUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScratchGameUtilTest {

    @Test
    void testGetMapArgs() {
        String[] args = {"--config", "path/to/config.json", "--another", "value"};
        Map<String, String> argsMap = ScratchGameUtil.getMapArgs(args);
        assertEquals("path/to/config.json", argsMap.get("config"));
        assertEquals("value", argsMap.get("another"));
    }

    @Test
    void testGetPropOrThrow() {
        Map<String, String> argsMap = Map.of("key", "value");
        assertEquals("value", ScratchGameUtil.getPropOrThrow(argsMap, "key", "Error message"));
        Exception exception = assertThrows(ScratchGameException.class, () -> ScratchGameUtil.getPropOrThrow(argsMap, "missing", "Error message"));
        assertEquals("Error message", exception.getMessage());
    }

    @Test
    void testGenerateListWithMissValue() {
        int rows = 3, columns = 3;
        List<List<String>> matrix = ScratchGameUtil.generateListWithMissValue(rows, columns);
        assertEquals(rows, matrix.size());
        matrix.forEach(row -> {
            assertEquals(columns, row.size());
            row.forEach(value -> assertEquals(ScratchGameConstant.MISS_VALUE, value));
        });
    }

}
