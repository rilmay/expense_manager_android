package com.guzov.expensemanagercompat.chart.entry;

import com.github.mikephil.charting.data.Entry;
import com.guzov.expensemanagercompat.dto.TimeFrame;
import com.guzov.expensemanagercompat.entity.BankMessage;

import java.util.List;

public interface ChartEntryProducer<E extends Entry> {
    List<E> getEntriesFromMessages(List<BankMessage> messages, TimeFrame timeFrame);
}
