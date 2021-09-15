package com.guzov.expensemanagercompat.dto;

import java.util.Arrays;
import java.util.Optional;

public enum MessageType {
    EXPENSE;


    public static Optional<Currency> getTag(String value) {
        return Arrays.stream(Currency.values())
                .filter(e -> e.name().equals(value))
                .findFirst();
    }
}
