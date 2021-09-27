package com.guzov.expensemanagercompat.entity;

import android.os.Parcel;

import com.guzov.expensemanagercompat.dto.Currency;
import com.guzov.expensemanagercompat.dto.MessageType;

import java.util.Date;
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
    public MessageType getMessageType() {
        return MessageType.EXPENSE;
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
        return  "Expense message: " +
                "valuesSpent=" + value +
                ", currency=" + currency +
                ", sourceOfSpending='" + sourceOfSpending + '\'' +
                ", date=" + date +
                ", cardNumber='" + cardNumber + '\'' +
                ", bankName='" + bankName + '\'';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalMessage);
        dest.writeFloat(value);
        dest.writeLong(date.getTime());
        dest.writeString(currency.name());
        dest.writeString(bankName);
        dest.writeString(cardNumber);
        dest.writeString(sourceOfSpending);
    }

    public static final Creator<ExpenseMessage> CREATOR = new Creator<ExpenseMessage>() {
        @Override
        public ExpenseMessage createFromParcel(Parcel source) {
            ExpenseMessage expenseMessage = new ExpenseMessage();
            expenseMessage.setOriginalMessage(source.readString());
            expenseMessage.setValue(source.readFloat());
            expenseMessage.setDate(new Date(source.readLong()));
            expenseMessage.setCurrency(Currency.getTag(source.readString()).orElse(null));
            expenseMessage.setBankName(source.readString());
            expenseMessage.setCardNumber(source.readString());
            expenseMessage.setSourceOfSpending(source.readString());
            return expenseMessage;
        }

        @Override
        public ExpenseMessage[] newArray(int size) {
            return new ExpenseMessage[0];
        }
    };
}
