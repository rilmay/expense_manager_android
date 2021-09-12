package com.guzov.expensemanagercompat.chart;

import android.text.format.DateFormat;

import com.github.mikephil.charting.data.BarEntry;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatasetFactory {
    public static List<BarEntry> getEntriesFromExpenseMessage(List<ExpenseMessage> messages) {
        List<BarEntry> entries = new ArrayList<>();
        Map<String, List<ExpenseMessage>> messagesByDates = messages
                .stream()
                .collect(Collectors.groupingBy(expenseMessage ->
                        String.valueOf(DateFormat.format("yyyy-MM-dd", expenseMessage.getDate()))));
        if (!messagesByDates.isEmpty()) {
            List<Map.Entry<String, List<ExpenseMessage>>> entryList = new ArrayList<>(messagesByDates.entrySet());
            entryList.sort(Map.Entry.comparingByKey());
            String oldestDate = entryList.get(0).getKey();
            String newestDate = entryList.get(entryList.size() - 1).getKey();
            if (oldestDate.equals(newestDate)) {
                return getBarDataSet(entryList);
            }
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date oldestDateParsed = format.parse(oldestDate);
                Date newestDateParsed = format.parse(newestDate);
                long diff = newestDateParsed.getTime() - oldestDateParsed.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                List<BarEntry> barEntries = new ArrayList<>();
                Date currentDay = (Date) oldestDateParsed.clone();
                for (int index = 1; index <= days; index++) {
                    String currentDateKey = String.valueOf(DateFormat.format("yyyy-MM-dd", currentDay));
                    List<ExpenseMessage> messagesByDate = messagesByDates.get(currentDateKey);
                    BarEntry entry;
                    if (messagesByDate == null || messagesByDate.isEmpty()) {
                        entry = new BarEntry(index, 0f);
                    } else {
                        entry = new BarEntry(index, getValuesFromMessages(messagesByDate), messagesByDate);
                    }
                    barEntries.add(entry);
                    currentDay = incrementDay(currentDay);
                }
                return barEntries;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return getBarDataSet(null);
    }

    private static List<BarEntry> getBarDataSet(List<Map.Entry<String, List<ExpenseMessage>>> entryList) {
        if (entryList == null) {
            return new ArrayList<>();
        } else {
            return IntStream
                    .range(0, entryList.size())
                    .boxed()
                    .map(index -> new BarEntry((float) index, getValuesFromMessages(entryList.get(index).getValue())))
                    .collect(Collectors.toList());
        }
    }

    private static float[] getValuesFromMessages(List<ExpenseMessage> messages) {
        float[] primData = new float[messages.size()];
        for (int x = 0; x < messages.size(); x++) {
            primData[x] = messages.get(x).getValue();
        }
        return primData;
    }


    private static Date incrementDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }
}
