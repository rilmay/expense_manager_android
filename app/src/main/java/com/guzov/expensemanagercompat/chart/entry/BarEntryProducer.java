package com.guzov.expensemanagercompat.chart.entry;

import com.github.mikephil.charting.data.BarEntry;

public class BarEntryProducer extends GenericEntryProducer<BarEntry> {
    @Override
    protected BarEntry getInstanceOfEntry(int index, float[] data, Object relatedData) {
        return new BarEntry(index, data, relatedData);
    }

    @Override
    protected BarEntry getInstanceOfEntry(int index, float data, Object relatedData) {
        return new BarEntry(index, data, relatedData);
    }
}
