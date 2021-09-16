package com.guzov.expensemanagercompat.message.parser;

import com.guzov.expensemanagercompat.dto.MessageType;

import java.util.Map;

public class BankSmsParserFactory {
    public static BankSmsParser getParser(MessageType messageType, Map<String, String> config) {
        if (messageType == MessageType.EXPENSE) {
            return ExpenseSmsParser.getInstance(config);
        } else {
            return null;
        }
    }
}
