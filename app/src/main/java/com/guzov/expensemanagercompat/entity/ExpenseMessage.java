package com.guzov.expensemanagercompat.entity;

import java.util.Date;
import java.util.Objects;

public class ExpenseMessage {
    private String originalMessage;
    private Double valuesSpent;
    private Currency currency;
    private String sourceOfSpending;
    private Date date;
    private String cardNumber;
    private String bankName;

    public ExpenseMessage(String originalMessage, Double valuesSpent, Currency currency, String sourceOfSpending, Date date, String bankName) {
        this.originalMessage = originalMessage;
        this.valuesSpent = valuesSpent;
        this.currency = currency;
        this.sourceOfSpending = sourceOfSpending;
        this.date = date;
        this.bankName = bankName;
    }

    public ExpenseMessage() {
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public Double getValuesSpent() {
        return valuesSpent;
    }

    public void setValuesSpent(Double valuesSpent) {
        this.valuesSpent = valuesSpent;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSourceOfSpending() {
        return sourceOfSpending;
    }

    public void setSourceOfSpending(String sourceOfSpending) {
        this.sourceOfSpending = sourceOfSpending;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseMessage that = (ExpenseMessage) o;
        return originalMessage.equals(that.originalMessage) &&
                Objects.equals(valuesSpent, that.valuesSpent) &&
                currency == that.currency &&
                Objects.equals(sourceOfSpending, that.sourceOfSpending) &&
                date.equals(that.date) &&
                Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(bankName, that.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalMessage, valuesSpent, currency, sourceOfSpending, date, cardNumber, bankName);
    }

    @Override
    public String toString() {
        return "ExpenseMessage{" +
                "originalMessage='" + originalMessage + '\'' +
                ", valuesSpent=" + valuesSpent +
                ", currency=" + currency +
                ", sourceOfSpending='" + sourceOfSpending + '\'' +
                ", date=" + date +
                ", cardNumber='" + cardNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
