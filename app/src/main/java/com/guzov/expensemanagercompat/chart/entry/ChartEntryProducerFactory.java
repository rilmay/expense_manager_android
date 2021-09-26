package com.guzov.expensemanagercompat.chart.entry;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.guzov.expensemanagercompat.dto.TimeFrame;
import com.guzov.expensemanagercompat.entity.BankMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartEntryProducerFactory {
    private static ChartEntryProducerFactory instance;
    private Map<Class<? extends Entry>, ChartEntryProducer<? extends Entry>> entryProducerMap;

    {
        entryProducerMap = new HashMap<>();
        entryProducerMap.put(BarEntry.class, new BarEntryProducer());
    }

    public static ChartEntryProducerFactory getInstance() {
        if (instance == null) {
            instance = new ChartEntryProducerFactory();
        }
        return instance;
    }

    private ChartEntryProducerFactory() {
    }

    public <E extends Entry> List<E> getEntriesFromMessages(
            List<BankMessage> messages,
            TimeFrame timeFrame,
            Class<E> entryType
    ) {
        ChartEntryProducer<E> producer = (ChartEntryProducer<E>) entryProducerMap.get(entryType);
        return producer != null ?
                producer.getEntriesFromMessages(messages, timeFrame) :
                new ArrayList<>();
    }
}
