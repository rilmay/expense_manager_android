package com.guzov.expensemanagercompat;

import com.guzov.expensemanagercompat.entity.Currency;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;
import com.guzov.expensemanagercompat.entity.Sms;
import com.guzov.expensemanagercompat.message.ExpenseSmsParser;
import com.guzov.expensemanagercompat.message.factory.DefaultExpenseConfigFactory;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ParserTest {
    private Sms getMockSms(String address, String msg) {
        Sms mockSms = new Sms();
        mockSms.setAddress(address);
        mockSms.setMsg(msg);
        mockSms.setId(String.valueOf(new Random()));
        mockSms.setTime(String.valueOf(System.currentTimeMillis()));
        mockSms.setFolderName("inbox");
        return mockSms;
    }

    @Test
    public void testParsingOnlyExpense() {
        String address = "Alfaaa";
        Sms sms1 = getMockSms(address, "Alfaaa. Karta 1***1111 01-01-2021 22:49:12. Oplata 6.98 BYN. BLR SHOP SOSEDI. Dostupno: 127.60 BYN. Spravka: 111111111");
        Sms sms2 = getMockSms(address, "Alfaaa. Karta 1***1111 01-01-2021 22:03:46. Nalichnye v bankomate 300.00 USD. BLR ATM 100.   Spravka: 111111111");
        Sms sms3 = getMockSms(address, "Alfaaa. Karta 1***1111 01-01-2021 21:46:28. Oplata 2.10 BYN. BLR KAFETERIY TERRI BAPB. Dostupno: 134.58 BYN. Spravka: 111111111");
        List<Sms> smsList = Arrays.asList(sms1, sms2, sms3);
        Map<String, String> config = DefaultExpenseConfigFactory.get();
        ExpenseSmsParser parser = new ExpenseSmsParser(config);
        List<ExpenseMessage> messages = parser.parse(smsList);
        assertEquals(2, messages.size());
    }

    @Test
    public void testParsingSpecificValues() {
        String address = "Alfaaa";
        Sms sms1 = getMockSms(address, "Alfaaa. Karta 1***1111 01-01-2021 22:49:12. Oplata 6.98 BYN. BLR SHOP SOSEDI. Dostupno: 127.60 BYN. Spravka: 111111111");
        Map<String, String> config = DefaultExpenseConfigFactory.get();
        ExpenseSmsParser parser = new ExpenseSmsParser(config);
        List<ExpenseMessage> messages = parser.parse(Collections.singletonList(sms1));
        assertEquals(1, messages.size());
        ExpenseMessage expenseMessage = messages.get(0);
        assertEquals(Float.valueOf(6.98f), Float.valueOf(expenseMessage.getValue()));
        assertEquals("BLR SHOP SOSEDI", expenseMessage.getSourceOfSpending());
        assertEquals(Currency.BYN, expenseMessage.getCurrency());
        assertEquals("1***1111", expenseMessage.getCardNumber());

    }

    @Test
    public void testParsingSpecificValues2() {
        String address = "Alfaaa";
        Sms sms1 = getMockSms(address, "Alfaaa. Karta 1***1111 01-01-2021 14:28:11. Oplata 7.24 BYN. BLR D-R OSTROV CHIST.I VKUSA. Dostupno: 13.65 BYN. Spravka: 111111111");
        Map<String, String> config = DefaultExpenseConfigFactory.get();
        ExpenseSmsParser parser = new ExpenseSmsParser(config);
        List<ExpenseMessage> messages = parser.parse(Collections.singletonList(sms1));
        assertEquals(1, messages.size());
        ExpenseMessage expenseMessage = messages.get(0);
        assertEquals(Float.valueOf(7.24f), Float.valueOf(expenseMessage.getValue()));
        assertEquals("BLR D-R OSTROV CHIST.I VKUSA", expenseMessage.getSourceOfSpending());
        assertEquals(Currency.BYN, expenseMessage.getCurrency());
        assertEquals("1***1111", expenseMessage.getCardNumber());

    }

    @Test
    public void testParsingSpecificValues3() {
        String address = "Alfaaa";
        Sms sms1 = getMockSms(address, "Alfaaa. Karta 1***1111 01-01-2021 17:51:46. Oplata 902.00 RUB. RUS Wildberries. Dostupno: 7.70 BYN. Spravka: 111111111");
        Map<String, String> config = DefaultExpenseConfigFactory.get();
        ExpenseSmsParser parser = new ExpenseSmsParser(config);
        List<ExpenseMessage> messages = parser.parse(Collections.singletonList(sms1));
        assertEquals(1, messages.size());
        ExpenseMessage expenseMessage = messages.get(0);
        assertEquals(Float.valueOf(902f), Float.valueOf(expenseMessage.getValue()));
        assertEquals("RUS Wildberries", expenseMessage.getSourceOfSpending());
        assertEquals(Currency.RUB, expenseMessage.getCurrency());
        assertEquals("1***1111", expenseMessage.getCardNumber());

    }
}
