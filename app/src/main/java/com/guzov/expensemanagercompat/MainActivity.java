package com.guzov.expensemanagercompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.guzov.expensemanagercompat.chart.Configurator;
import com.guzov.expensemanagercompat.chart.entry.ChartEntryProducerFactory;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
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
        showExpenses();
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
        BankSmsParser parser = BankSmsParserFactory.getParser(MessageType.EXPENSE, expenseConfig);
        if (parser != null) {
            messages = parser.parse(lst);
        }
        List<BankMessage> localMessages =  MessageUtils.filterMessagesByCurrency(MessageUtils.getMessagesWithinTimeframe(messages, TimeFrame.FROM_CURRENT_MONTH), Currency.BYN);
        List<BankMessage> localMessagesPastMonth =  MessageUtils.filterMessagesByCurrency(MessageUtils.getMessagesWithinTimeframe(messages, TimeFrame.FROM_PREVIOUS_MONTH_TO_CURRENT_MONTH), Currency.BYN);
        Double sumCurrentMonth = MessageUtils.getSummaryOfMessages( MessageUtils.filterMessagesByCurrency(localMessages, Currency.BYN));
        Double sumPrevMonth = MessageUtils.getSummaryOfMessages( MessageUtils.filterMessagesByCurrency(localMessagesPastMonth, Currency.BYN));
        Double avgCurrentMonth = MessageUtils.getAverageByDay(localMessages, TimeFrame.FROM_CURRENT_MONTH);
        Double avgPrevMonth = MessageUtils.getAverageByDay(localMessagesPastMonth, TimeFrame.FROM_PREVIOUS_MONTH_TO_CURRENT_MONTH);
        Double expectedSumCurrentMonth = MessageUtils.getExpectedSum(avgCurrentMonth, TimeFrame.FROM_CURRENT_MONTH_FULL);

        statistics.setText("Sum current month " + sumCurrentMonth
                + " sum expected " + expectedSumCurrentMonth
                + " avg " + avgCurrentMonth
                + " sum prev " + sumPrevMonth +
                " avg " + avgPrevMonth);

        textView.setText("Messages loaded");
        List<BarEntry> entries = ChartEntryProducerFactory
                .getInstance()
                .getEntriesFromMessages(localMessages, TimeFrame.FROM_CURRENT_MONTH, BarEntry.class);
        Configurator.prepareBarChart(chart, entries, new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                List<BankMessage> messages = ((EntryMessageData)e.getData()).getMessages();
                if (messages != null && !messages.isEmpty()) {
                    int index = h.getStackIndex();
                    String result = "";
                    BankMessage bankMessage = messages.get(index);
                    if (bankMessage != null) {
                        result = bankMessage.toString();
                    }
                    textView.setText(result);
                }
            }

            @Override
            public void onNothingSelected() {
                textView.setText("");
            }
        });
    }
}