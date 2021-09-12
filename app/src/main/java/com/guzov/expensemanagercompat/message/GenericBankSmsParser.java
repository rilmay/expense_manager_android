package com.guzov.expensemanagercompat.message;

import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.entity.Sms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class GenericBankSmsParser<T extends BankMessage> {
    protected Map<String, String> config;

    public GenericBankSmsParser(Map<String, String> config) {
        this.config = config;
        if (isConfigEligible()) {
            extractValues();
        }
    }

    public List<T> parse(List<Sms> smsList) {
        List<T> result = new ArrayList<>();
        if (smsList != null && !smsList.isEmpty() && isConfigEligible()) {
            result = smsList.stream()
                    .filter(this::filterSms)
                    .map(this::parseMessage)
                    .collect(Collectors.toList());
        }
        return result;
    }

    protected abstract Boolean filterSms(Sms sms);

    protected abstract T parseMessage(Sms sms);

    protected abstract Boolean isConfigEligible();

    protected abstract void extractValues();

    protected Optional<String> findFirstByPattern(String text, String pattern) {
        return findByPattern(text, pattern).stream().findFirst();
    }

    protected List<String> findByPattern(String text, String pattern) {
        List<String> foundStrings = new ArrayList<>();
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            foundStrings.add(text.substring(start, end));
        }
        return foundStrings;
    }
}
