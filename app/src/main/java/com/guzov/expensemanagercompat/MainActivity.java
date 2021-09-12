package com.guzov.expensemanagercompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.guzov.expensemanagercompat.chart.DatasetFactory;
import com.guzov.expensemanagercompat.entity.Currency;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;
import com.guzov.expensemanagercompat.entity.Sms;
import com.guzov.expensemanagercompat.message.ExpenseSmsParser;
import com.guzov.expensemanagercompat.message.SmsManager;
import com.guzov.expensemanagercompat.message.factory.DefaultExpenseConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private TextView textView;
    private BarChart chart;
    private List<ExpenseMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts);
        textView = (TextView) findViewById(R.id.selected_value);
        chart = (BarChart) findViewById(R.id.chart);

//        BarData data = new BarData(getDataSet());
//
//        chart.setData(data);

        int range = 3;
        int start = 1;
        int count = 30;
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));

            if (val > 2) {
                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }
        BarDataSet set1;
        set1 = new BarDataSet(values, "The year 2017");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        chart.setData(data);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                List<ExpenseMessage> entries = (List<ExpenseMessage>) e.getData();
                if (entries != null) {
                    int index = h.getStackIndex();
                    //String messages = entries.stream().map(entry -> entry.toString()).collect(Collectors.joining(", "));
                    textView.setText("Value = " + entries.get(index).toString());
                }
            }

            @Override
            public void onNothingSelected() {
                textView.setText("");
            }
        });


        Description description = new Description();
        description.setText("My Chart");
        chart.setDescription(description);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return months[(int) value % months.length];
                return "" + (int) value;
            }
        });

        chart.animateXY(2000, 2000);
        chart.setPinchZoom(false);
        chart.invalidate();
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }

    public void onMessagesButtonClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;

            if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

                showExpenses();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            showExpenses();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showExpenses();
                }
            }
        }
    }

    private void showExpenses(){
        List<Sms> lst = SmsManager.getAllSms(this);
        Map<String, String> expenseConfig = DefaultExpenseConfigFactory.get();
        ExpenseSmsParser expenseSmsParser = new ExpenseSmsParser(expenseConfig);
        messages = expenseSmsParser.parse(lst);
        List<ExpenseMessage> localMessages = messages
                .stream()
                .filter(expenseMessage -> Currency.BYN.equals(expenseMessage.getCurrency()))
                .collect(Collectors.toList()).subList(0,10);
        textView.setText("message " + messages.get(0).getValue());
        List<BarEntry> entries = DatasetFactory.getEntriesFromExpenseMessage(localMessages);
        BarDataSet barDataSet = new BarDataSet(entries, "expense");
        chart.clear();
        BarData barData = new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();
    }
}