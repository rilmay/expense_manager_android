package com.guzov.expensemanagercompat.constants;

import java.util.Arrays;
import java.util.List;

public class ExpenseConstants {
    public static final String KEYWORD = "EXPENSE_KEYWORD";
    public static final String VALUE_REGEX = "VALUE_REGEX";
    public static final String CARD_REGEX = "CARD_REGEX";
    public static final String CURRENCY_REGEX = "CURRENCY_REGEX";
    public static final String SOURCE_OF_SPENDING_REGEX = "SOURCE_OF_SPENDING_REGEX";


    public static final List<String> ALL_CONSTANTS =
            Arrays.asList(KEYWORD, VALUE_REGEX, CARD_REGEX, SOURCE_OF_SPENDING_REGEX, CURRENCY_REGEX);
}
