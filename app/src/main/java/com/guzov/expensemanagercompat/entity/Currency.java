package com.guzov.expensemanagercompat.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Currency {
    BYN,
    USD;

    public static Optional<Currency> getTag(String value) {
        return Arrays.stream(Currency.values())
                .filter(e -> e.name().toLowerCase().replaceAll("_"," ").equals(value))
                .findFirst();
    }
}
