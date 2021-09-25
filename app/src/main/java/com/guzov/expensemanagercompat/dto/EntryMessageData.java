package com.guzov.expensemanagercompat.dto;

import com.guzov.expensemanagercompat.entity.BankMessage;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EntryMessageData {
    private List<BankMessage> messages;
    private Date date;

    public List<BankMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<BankMessage> messages) {
        this.messages = messages;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EntryMessageData(List<BankMessage> messages, Date date) {
        this.messages = messages;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryMessageData that = (EntryMessageData) o;
        return Objects.equals(messages, that.messages) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, date);
    }
}
