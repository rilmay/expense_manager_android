package com.guzov.expensemanagercompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.guzov.expensemanagercompat.chart.BarEntryProducer;
import com.guzov.expensemanagercompat.chart.ChartEntryProducerFactory;
import com.guzov.expensemanagercompat.dto.EntryMessageData;
import com.guzov.expensemanagercompat.dto.MessageType;
import com.guzov.expensemanagercompat.entity.BankMessage;
import com.guzov.expensemanagercompat.dto.Currency;
import com.guzov.expensemanagercompat.entity.Sms;
import com.guzov.expensemanagercompat.dto.TimeFrame;
import com.guzov.expensemanagercompat.message.parser.BankSmsParser;
import com.guzov.expensemanagercompat.message.parser.BankSmsParserFactory;
import com.guzov.expensemanagercompat.message.MessageUtils;
import com.guzov.expensemanagercompat.message.SmsManager;
import com.guzov.expensemanagercompat.message.factory.DefaultExpenseConfigFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private TextView textView;
    private BarChart chart;
    private TextView statistics;
    private List<BankMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts);
        statistics = (TextView) findViewById(R.id.statistic);
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
                List<BankMessage> messages = ((EntryMessageData)e.getData()).getMessages();
                if (messages != null && !messages.isEmpty()) {
                    int index = h.getStackIndex();
                    textView.setText("Value = " + messages.get(index).toString());
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
        BankSmsParser smsParser = BankSmsParserFactory.getParser(MessageType.EXPENSE, expenseConfig);
        messages = smsParser.parse(lst);
        List<BankMessage> localMessages =  MessageUtils.filterMessagesByCurrency(MessageUtils.getMessagesWithinTimeframe(messages, TimeFrame.FROM_CURRENT_MONTH), Currency.BYN);
        List<BankMessage> localMessagesPastMonth =  MessageUtils.filterMessagesByCurrency(MessageUtils.getMessagesWithinTimeframe(messages, TimeFrame.FROM_PREVIOUS_MONTH_TO_CURRENT_MONTH), Currency.BYN);
        Double sumCurrentMonth = MessageUtils.getSummaryOfMessages( MessageUtils.filterMessagesByCurrency(localMessages, Currency.BYN));
        Double sumPrevMonth = MessageUtils.getSummaryOfMessages( MessageUtils.filterMessagesByCurrency(localMessagesPastMonth, Currency.BYN));
        Double avgCurrentMonth = MessageUtils.getAverageByDay(localMessages, TimeFrame.FROM_CURRENT_MONTH);
        Double avgPrevMonth = MessageUtils.getAverageByDay(localMessagesPastMonth, TimeFrame.FROM_PREVIOUS_MONTH_TO_CURRENT_MONTH);

        statistics.setText("Sum current month " + sumCurrentMonth + " avg " + avgCurrentMonth + " " + " sum prev " + sumPrevMonth + " avg " + avgPrevMonth);

        textView.setText("message " + messages.get(0).getValue());
        List<BarEntry> entries = ChartEntryProducerFactory
                .getInstance()
                .getEntriesFromMessages(localMessages, BarEntry.class);
        BarDataSet barDataSet = new BarDataSet(entries, "expense");
        chart.clear();
        BarData barData = new BarData(barDataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
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
            }
        });
        
        chart.setData(barData);
        chart.invalidate();
    }
}