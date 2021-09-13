package com.guzov.expensemanagercompat;

import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;
import com.guzov.expensemanagercompat.entity.TimeFrame;
import com.guzov.expensemanagercompat.message.MessageUtils;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageUtilsTest {
    private ExpenseMessage getMessage(String origMsg, Date date) {
        ExpenseMessage expenseMessage = new ExpenseMessage();
        expenseMessage.setDate(date);
        expenseMessage.setOriginalMessage(origMsg);
        return expenseMessage;
    }


    @Test
    public void testMessagesWithinMonth() {
        String msg1 = "msg1";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) -1);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Date date1 = cal.getTime();
        ExpenseMessage expenseMessage1 = getMessage(msg1, date1);
        String msg2 = "msg2";

        Calendar cal2 = Calendar.getInstance();
        Date date2 = new Date();
        ExpenseMessage expenseMessage2 = getMessage(msg2, date2);
        List<BankMessage> bankMessages =
                MessageUtils.getMessagesWithinTimeframe(Arrays.asList(expenseMessage1, expenseMessage2), TimeFrame.FROM_CURRENT_MONTH);
        assertEquals(1, bankMessages.size());
        assertEquals(msg2, bankMessages.get(0).getOriginalMessage());

    }
}
