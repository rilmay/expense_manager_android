package com.guzov.expensemanagercompat.entity;

import java.util.Date;
import java.util.Objects;

public abstract class BankMessage {
    protected String originalMessage;
    protected float value;
    protected Date date;
    protected Currency currency;
    protected String bankName;
    protected String cardNumber;

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankMessage that = (BankMessage) o;
        return Float.compare(that.value, value) == 0 &&
                Objects.equals(originalMessage, that.originalMessage) &&
                Objects.equals(date, that.date) &&
                currency == that.currency &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(cardNumber, that.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalMessage, value, date, currency, bankName, cardNumber);
    }

    @Override
    public String toString() {
        return "BankMessage{" +
                "originalMessage='" + originalMessage + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", currency=" + currency +
                ", bankName='" + bankName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
