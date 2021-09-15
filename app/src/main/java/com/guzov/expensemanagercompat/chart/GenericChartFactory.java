package com.guzov.expensemanagercompat.chart;

import android.text.format.DateFormat;

import com.github.mikephil.charting.data.Entry;
import com.guzov.expensemanagercompat.entity.BankMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class GenericChartFactory<E extends Entry> {
    protected final String datePattern = "yyyy-MM-dd";
    protected final SimpleDateFormat format = new SimpleDateFormat(datePattern);
    protected abstract E getInstanceOfEntry(int index, float[] data, Object relatedData);
    protected abstract E getInstanceOfEntry(int index, float data, Object relatedData);

    public List<E> getEntriesFromMessages(List<BankMessage> messages) {
        List<E> entries = new ArrayList<>();
        Map<String, List<BankMessage>> messagesByDates = getMessagesGroupedByDate(messages);
        if (!messagesByDates.isEmpty()) {
            List<String> dates = new ArrayList<>(messagesByDates.keySet());
            dates.sort(String::compareTo);
            Date oldestDate = parseDate(dates.get(0));
            Date newestDate = parseDate(dates.get(dates.size() - 1));
            if (oldestDate != null && newestDate != null) {
                entries = getEntriesFromDateToDate(oldestDate, newestDate, messagesByDates);
            }
        }
        return entries;
    }

    protected Date parseDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException parseException) {
            return null;
        }
    }

    protected List<E> getEntriesFromDateToDate(
            Date fromDate, Date toDate, Map<String, List<BankMessage>> messagesByDates
    ) {
        List<E> entries = new ArrayList<>();
        long diff = toDate.getTime() - fromDate.getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        Date currentDay = (Date) fromDate.clone();
        for (int index = 1; index <= days+1; index++) {
            String currentDateKey = getFormattedDate(currentDay);
            List<BankMessage> messagesByDate = messagesByDates.get(currentDateKey);
            E entry;
            if (messagesByDate == null || messagesByDate.isEmpty()) {
                entry = getInstanceOfEntry(index, 0f, messagesByDate);
            } else {
                entry = getInstanceOfEntry(index, getValuesFromMessages(messagesByDate), messagesByDate);
            }
            entries.add(entry);
            currentDay = incrementDay(currentDay);
        }
        return entries;
    }


    protected Date incrementDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    protected String getFormattedDate(Date date) {
        return String.valueOf(DateFormat.format(datePattern, date));
    }

    protected Map<String, List<BankMessage>> getMessagesGroupedByDate(List<BankMessage> messages) {
        return messages
                .stream()
                .collect(Collectors.groupingBy(message -> getFormattedDate(message.getDate())));
    }

    protected float[] getValuesFromMessages(List<BankMessage> messages) {
        float[] primData = new float[messages.size()];
        for (int x = 0; x < messages.size(); x++) {
            primData[x] = messages.get(x).getValue();
        }
        return primData;
    }



}
