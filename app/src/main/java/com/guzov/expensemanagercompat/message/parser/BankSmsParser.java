package com.guzov.expensemanagercompat.message.parser;

import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.entity.Sms;

import java.util.List;
import java.util.Map;

public interface BankSmsParser {
    List<BankMessage> parse(List<Sms> smsList);
}
