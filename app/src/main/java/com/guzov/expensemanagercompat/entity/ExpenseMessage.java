package com.guzov.expensemanagercompat.entity;

import java.util.Objects;

public class ExpenseMessage extends BankMessage {
    private String sourceOfSpending;

    public String getSourceOfSpending() {
        return sourceOfSpending;
    }

    public void setSourceOfSpending(String sourceOfSpending) {
        this.sourceOfSpending = sourceOfSpending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseMessage that = (ExpenseMessage) o;
        return Objects.equals(originalMessage, that.originalMessage) &&
                Objects.equals(value, that.value) &&
                currency == that.currency &&
                Objects.equals(sourceOfSpending, that.sourceOfSpending) &&
                date.equals(that.date) &&
                Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(bankName, that.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalMessage, value, currency, sourceOfSpending, date, cardNumber, bankName);
    }

    @Override
    public String toString() {
        return "ExpenseMessage{" +
                "originalMessage='" + originalMessage + '\'' +
                ", valuesSpent=" + value +
                ", currency=" + currency +
                ", sourceOfSpending='" + sourceOfSpending + '\'' +
                ", date=" + date +
                ", cardNumber='" + cardNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
