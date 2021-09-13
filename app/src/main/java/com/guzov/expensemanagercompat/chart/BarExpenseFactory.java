package com.guzov.expensemanagercompat.chart;

import com.github.mikephil.charting.data.BarEntry;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;

public class BarExpenseFactory extends GenericChartFactory<BarEntry> {
    @Override
    protected BarEntry getInstanceOfEntry(int index, float[] data, Object relatedData) {
        return new BarEntry(index, data, relatedData);
    }

    @Override
    protected BarEntry getInstanceOfEntry(int index, float data, Object relatedData) {
        return new BarEntry(index, data, relatedData);
    }
}
