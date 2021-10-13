package com.guzov.expensemanagercompat.chart;

import android.graphics.Color;
import android.text.format.DateFormat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.guzov.expensemanagercompat.dto.EntryMessageData;
import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.message.MessageUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class Configurator {
    public static void prepareBarChart(BarChart chart, List<BarEntry> entries, OnChartValueSelectedListener listener) {
        chart.clear();
        chart.setOnChartValueSelectedListener(listener);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(true);
        chart.animateXY(2000,2000);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        chart.setData(getBarData(entries));
        configureBarChartAxes(chart, entries);
        chart.invalidate();
    }

    private static BarData getBarData(List<BarEntry> entries) {
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(Color.parseColor("#F0E5CF"), Color.parseColor("#C8C6C6"), Color.parseColor("#4B6587"));

        final int[] lastValueCount = {0};
        barDataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> {
            String result = "";
            List<BankMessage> messages = entry.getData() == null ? null: ((EntryMessageData) entry.getData()).getMessages();
            if (messages != null && !messages.isEmpty()) {
                float lastValue = messages.get(messages.size() - 1).getValue();
                int count = (int) messages
                        .stream()
                        .filter(bankMessage -> bankMessage.getValue() == lastValue)
                        .count();
                if (value == lastValue) {
                    lastValueCount[0]++;
                }
                if (lastValueCount[0] == count) {
                    result = String.valueOf(MessageUtils.getSummaryOfMessages(messages));
                    lastValueCount[0] = 0;
                }
            } else {
                result = "0";
                lastValueCount[0] = 0;
            }
            return result;
        });
        barDataSet.setValueTextSize(8f);
        return new BarData(barDataSet);
    }

    private static void configureBarChartAxes(BarChart chart, List<BarEntry> entries) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter((value, axis) -> {
            int index = (int) value - 1;
            String result = String.valueOf(index);
            if (entries.size() > index) {
                BarEntry entry = entries.get(index);
                if (entry.getData() != null ) {
                    Date date = ((EntryMessageData) entry.getData()).getDate();
                    result = String.valueOf(DateFormat.format("MMM-dd", date));
                }
            }
            return result;
        });
        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setDrawLabels(true);
        axisLeft.setGranularity(50f);
        axisLeft.setSpaceBottom(0);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setDrawLabels(false);
        axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRight.setSpaceBottom(0);
        axisRight.setGranularity(50f);
    }
}
