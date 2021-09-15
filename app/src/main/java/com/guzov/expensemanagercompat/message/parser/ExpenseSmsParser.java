package com.guzov.expensemanagercompat.message.parser;

import androidx.annotation.NonNull;

import com.guzov.expensemanagercompat.constants.ExpenseConstants;
import com.guzov.expensemanagercompat.constants.ProjectConstants;
import com.guzov.expensemanagercompat.dto.Currency;
import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;
import com.guzov.expensemanagercompat.entity.Sms;

import java.util.Date;
import java.util.Map;

public class ExpenseSmsParser extends GenericBankSmsParser implements BankSmsParser {
    private Map<String, String> config;
    private String expenseKeyword;
    private String valueRegex;
    private String cardRegex;
    private String currencyRegex;
    private String sourceOfSpendingRegex;

    private void extractValues(Map<String, String> config) {
        expenseKeyword = config.get(ExpenseConstants.KEYWORD);
        valueRegex = config.get(ExpenseConstants.VALUE_REGEX);
        cardRegex = config.get(ExpenseConstants.CARD_REGEX);
        currencyRegex = config.get(ExpenseConstants.CURRENCY_REGEX);
        sourceOfSpendingRegex = config.get(ExpenseConstants.SOURCE_OF_SPENDING_REGEX);
    }

    private ExpenseSmsParser(Map<String, String> config) {
        this.config = config;
    }

    public static BankSmsParser getInstance(Map<String, String> config) {
        ExpenseSmsParser expenseSmsParser = new ExpenseSmsParser(config);
        if (expenseSmsParser.isConfigEligible()) {
            expenseSmsParser.extractValues(config);
        }
        return expenseSmsParser;
    }

    @Override
    protected Boolean filterSms(Sms sms) {
        return sms != null
                && sms.getMsg() != null
                && sms.getTime() != null
                && ProjectConstants.SMS_FOLDER_INBOX.equals(sms.getFolderName())
                && findFirstByPattern(sms.getMsg(), expenseKeyword).isPresent();
    }

    @Override
    protected BankMessage parseMessage(Sms sms) {
        ExpenseMessage expenseMessage = new ExpenseMessage();
        String originalMsg = sms.getMsg();
        expenseMessage.setOriginalMessage(originalMsg);
        Float value = findFirstByPattern(originalMsg, valueRegex).map(Float::new).orElse(0f);
        expenseMessage.setValue(value);
        expenseMessage.setCurrency(getCurrency(originalMsg, expenseMessage.getValue()));
        expenseMessage.setCardNumber(findFirstByPattern(originalMsg, cardRegex).orElse(null));
        expenseMessage.setSourceOfSpending(getSourceOfSpending(originalMsg, expenseMessage.getValue()));
        expenseMessage.setDate(new Date(Long.parseLong(sms.getTime())));
        expenseMessage.setBankName(sms.getAddress());
        return (BankMessage) expenseMessage;
    }

    private Currency getCurrency(String message, Float value) {
        String patternWithValue = String.format(currencyRegex, value.intValue());
        return Currency
                .getTag(findFirstByPattern(message, patternWithValue).orElse(null))
                .orElse(Currency.BYN);
    }

    private String getSourceOfSpending(String message, Float value) {
        String patternWithValue = String.format(sourceOfSpendingRegex, value.intValue());
        return findFirstByPattern(message, patternWithValue).orElse(null);
    }

    protected Boolean isConfigEligible() {
        return this.config != null && this.config.keySet().containsAll(ExpenseConstants.ALL_CONSTANTS);
    }
}
