package com.guzov.expensemanagercompat.message;

import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.dto.Currency;
import com.guzov.expensemanagercompat.dto.TimeFrame;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageUtils {
    private static Double roundValue(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.valueOf(df.format(value));
    }

    public static List<BankMessage> getMessagesWithinTimeframe(
            List<BankMessage> messages, TimeFrame timeFrame
    ){
        List<BankMessage> sortedMessages = sortMessagesByDate(messages);
        Date fromDate = timeFrame.getFromDate();
        Date tillDate = timeFrame.getTillDate();
        return sortedMessages.stream().filter(message -> {
            Date currentDate = message.getDate();
            boolean eligibleFrom = (fromDate == null) || (fromDate.equals(currentDate) || fromDate.before(currentDate));
            boolean eligibleTill = (tillDate == null) || (tillDate.equals(currentDate)) || tillDate.after(currentDate);
            return eligibleFrom && eligibleTill;
        }).collect(Collectors.toList());
    }

    public static List<BankMessage> sortMessagesByDate(List<BankMessage> messages) {
        return messages.stream().sorted(Comparator.nullsLast(
                (e1, e2) -> e2.getDate().compareTo(e1.getDate()))).collect(Collectors.toList());
    }

    public static List<BankMessage> filterMessagesByCurrency(List<BankMessage> messages, Currency currency) {
        return messages.stream()
                .filter(bankMessage -> currency.equals(bankMessage.getCurrency()))
                .collect(Collectors.toList());
    }

    public static <T extends BankMessage> List<BankMessage> castMessages(List<T> messages) {
        return messages.stream().map(message -> (BankMessage) message).collect(Collectors.toList());
    }

    public static Double getSummaryOfMessages(List<BankMessage> messages) {
        double result = 0;
        if (messages != null) {
            result = messages.stream().mapToDouble(message -> (double) message.getValue()).sum();
        }
        return roundValue(result);
    }

    public static Double getAverageCheck(List<BankMessage> messages) {
        double result = messages.stream().mapToDouble(message -> (double) message.getValue()).average().orElse(0);
        return roundValue(result);
    }

    public static Double getAverageByDay(List<BankMessage> messages, TimeFrame timeFrame) {
        long diff = timeFrame.getTillDate().getTime() - timeFrame.getFromDate().getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        List<BankMessage> messagesWithinTimeframe = getMessagesWithinTimeframe(messages, timeFrame);
        double sum = getSummaryOfMessages(messagesWithinTimeframe);
        return roundValue(sum / days);
    }

    public static Double getExpectedSum(double averageByDay, TimeFrame timeFrame) {
        long diff = timeFrame.getTillDate().getTime() - timeFrame.getFromDate().getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return roundValue(averageByDay * days);
    }
}
