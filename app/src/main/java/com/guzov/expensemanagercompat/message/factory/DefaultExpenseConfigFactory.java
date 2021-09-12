package com.guzov.expensemanagercompat.message.factory;

import com.guzov.expensemanagercompat.constants.ExpenseConstants;

import java.util.HashMap;
import java.util.Map;

public class DefaultExpenseConfigFactory {
    public static Map<String, String> get() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put(ExpenseConstants.KEYWORD, "Oplata ");
        configMap.put(ExpenseConstants.VALUE_REGEX, "(?<=Oplata )[0-9]+\\.[0-9]+");
        configMap.put(ExpenseConstants.CARD_REGEX, "(?<=Karta )[0-9*]+");
        configMap.put(ExpenseConstants.CURRENCY_REGEX, "(?<=Oplata %s\\.[0-9]{2} )[A-Z]{1,3}");
        configMap.put(ExpenseConstants.SOURCE_OF_SPENDING_REGEX, "(?<=Oplata %s\\.[0-9]{2} [A-Z]{3}\\. ).+(?=\\. Dostupno)");
        return configMap;
    }
}
