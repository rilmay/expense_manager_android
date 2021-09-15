package com.guzov.expensemanagercompat.message.parser;

import com.guzov.expensemanagercompat.dto.MessageType;

import java.util.Map;

public class BankSmsParserFactory {
    private static BankSmsParserFactory instance = new BankSmsParserFactory();

    public static BankSmsParserFactory getInstance() {
        if (instance == null) {
            instance = new BankSmsParserFactory();
        }
        return instance;
    }

    public BankSmsParser getParser(MessageType messageType, Map<String, String> config) {
        if (messageType == MessageType.EXPENSE) {
            return ExpenseSmsParser.getInstance(config);
        } else {
            return null;
        }
    }
}
