package com.game.util;

import lombok.Getter;

@Getter
public enum Arguments {
    CONFIG("config"),
    BETTING_AMOUNT("betting-amount");

    private final String value;

    Arguments(String value) {
        this.value = value;
    }

}