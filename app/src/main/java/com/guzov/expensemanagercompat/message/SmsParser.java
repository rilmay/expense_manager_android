package com.guzov.expensemanagercompat.message;

import com.guzov.expensemanagercompat.entity.Currency;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;
import com.guzov.expensemanagercompat.entity.Sms;
import com.guzov.expensemanagercompat.entity.SmsConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SmsParser {
    private static final String EXPENSE_KEYWORD = "Oplata ";
    private static final String VALUE_REGEX = "(?<=Oplata )[0-9]+\\.[0-9]+";
    private static final String CARD_REGEX = "(?<=Karta )[0-9*]+";
    private static final String SOURCE_OF_SPENDING = "[A-Z][A-Z ]+(?=\\. Dostupno:)";


    public static List<ExpenseMessage> getExpenseMessages(SmsConfig config, List<Sms> smsList) {
        return smsList
                .stream()
                .filter(sms -> sms.getFolderName().equals("inbox"))
                .filter(sms -> sms.getAddress().equals(config.getAddressToCheck()) && isExpenseMessage(sms))
                .map(SmsParser::parseMessage)
                .collect(Collectors.toList());
    }

    private static Boolean isExpenseMessage(Sms sms) {
        return sms.getMsg().contains(EXPENSE_KEYWORD);
    }

    private static ExpenseMessage parseMessage(Sms message) {
        ExpenseMessage expenseMessage = new ExpenseMessage();
        String originalMsg = message.getMsg();
        expenseMessage.setOriginalMessage(originalMsg);
        expenseMessage.setValuesSpent(findFirstByPattern(originalMsg,VALUE_REGEX).map(Float::new).orElse(null));
        expenseMessage.setCurrency(getCurrency(originalMsg, expenseMessage.getValuesSpent()));
        expenseMessage.setCardNumber(findFirstByPattern(originalMsg, CARD_REGEX).orElse(null));
        expenseMessage.setSourceOfSpending(getSourceOfSpending(originalMsg, expenseMessage.getValuesSpent()).orElse(null));
        expenseMessage.setDate(new Date(Long.parseLong(message.getTime())));
        return expenseMessage;
    }

    private static Currency getCurrency(String message, Float valueSpent) {
        int intValue = valueSpent == null? 0: valueSpent.intValue();
        String pattern = "(?<=Oplata " + intValue + "\\.[0-9]{2} )[A-Z]{1,3}";
        return Currency.getTag(findFirstByPattern(message, pattern).orElse(null)).orElse(Currency.BYN);
    }

    private static Optional<String> getSourceOfSpending(String message, Float valueSpent) {
        int intValue = valueSpent == null? 0: valueSpent.intValue();
        String pattern = "(?<=Oplata " + intValue + "\\.[0-9]{2} [A-Z]{3}\\. ).+(?=\\. Dostupno)";
        return findFirstByPattern(message, pattern);
    }

    private static Optional<String> findFirstByPattern(String text, String pattern) {
        return findByPattern(text, pattern).stream().findFirst();
    }

    private static List<String> findByPattern(String text, String pattern) {
        List<String> foundStrings = new ArrayList<>();
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(text);
        while (matcher.find()) {
            int start=matcher.start();
            int end=matcher.end();
            foundStrings.add(text.substring(start, end));
        }
        return foundStrings;
    }
}
