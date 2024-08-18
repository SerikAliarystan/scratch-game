package com.game;

import com.game.exception.ScratchGameException;
import com.game.dto.OutputResult;
import com.game.service.impl.MatrixServiceImpl;
import com.game.service.impl.ScratchGameServiceImpl;
import com.game.util.Arguments;

import java.util.Map;

import static com.game.exception.ScratchGameExceptionMessage.*;
import static com.game.util.ScratchGameUtil.*;
import static java.lang.Double.parseDouble;

public class ScratchGameRunner {

    public static void main(String[] args) {
        final Map<String, String> argsMap = getMapArgs(args);
        final double bettingAmount = parseDouble(
                getPropOrThrow(argsMap, Arguments.BETTING_AMOUNT.getValue(), NO_BETTING_AMOUNT_ARGS));
        final String config = getPropOrThrow(argsMap, Arguments.CONFIG.getValue(), NO_CONFIG_ARG);

        if (bettingAmount <= 0)
            throw new ScratchGameException(BETTING_AMOUNT_MUST_BE_POSITIVE);

        OutputResult outputResult = new ScratchGameServiceImpl(getConfig(config), new MatrixServiceImpl())
                .spin(bettingAmount);

        printOutput(outputResult);
    }


}