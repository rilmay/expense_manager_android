package com.guzov.expensemanagercompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.guzov.expensemanagercompat.chart.DatasetFactory;
import com.guzov.expensemanagercompat.entity.Currency;
import com.guzov.expensemanagercompat.entity.ExpenseMessage;
import com.guzov.expensemanagercompat.entity.Sms;
import com.guzov.expensemanagercompat.entity.SmsConfig;
import com.guzov.expensemanagercompat.message.SmsManager;
import com.guzov.expensemanagercompat.message.SmsParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    protected final String[] months = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private TextView textView;
    private BarChart chart;

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
                return "" + (int)value;
            }
        });

        chart.animateXY(2000, 2000);
        chart.setPinchZoom(false);
        chart.invalidate();
    }

    private List<IBarDataSet> getDataSet() {
        ArrayList dataSets = null;

        ArrayList valueSet1 = new ArrayList();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList valueSet2 = new ArrayList();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
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
            final String myPackageName = getPackageName();
            String telephonyDefault = Telephony.Sms.getDefaultSmsPackage(this);

            final int REQUEST_CODE_ASK_PERMISSIONS = 123;

            if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                List<Sms> lst = SmsManager.getAllSms(this);
                SmsConfig config = new SmsConfig();
                config.setAddressToCheck("Priorbank");
                List<ExpenseMessage> messages = SmsParser.getExpenseMessages(config, lst)
                        .stream()
                        .filter(expenseMessage -> Currency.BYN.equals(expenseMessage.getCurrency()))
                        .collect(Collectors.toList());
                messages = messages.subList(0, 90);
                textView.setText("message " + messages.get(0).getValuesSpent());
                List<BarEntry> entries = DatasetFactory.getEntriesFromExpenseMessage(messages);
                BarDataSet barDataSet = new BarDataSet(entries, "expense");
                chart.clear();
                BarData barData = new BarData(barDataSet);
                chart.setData(barData);
                chart.invalidate();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);

            }


//            if (telephonyDefault == null || !telephonyDefault.equals(myPackageName)) {
//
//                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
//                startActivityForResult(intent, 1);
//            }else {
            //}
        }else {
            List<Sms> lst = SmsManager.getAllSms(this);
            SmsConfig config = new SmsConfig();
            config.setAddressToCheck("Priorbank");
            List<ExpenseMessage> messages = SmsParser.getExpenseMessages(config, lst);
            messages = messages.subList(messages.size() - 20, messages.size()-1);
            textView.setText("message " + messages.get(0).getValuesSpent());
            List<BarEntry> entries = DatasetFactory.getEntriesFromExpenseMessage(messages);
            BarDataSet barDataSet = new BarDataSet(entries, "expense");
            chart.clear();
            BarData barData = new BarData(barDataSet);
            chart.setData(barData);
            chart.invalidate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final String myPackageName = getPackageName();
                    if (Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                        List<Sms> lst = SmsManager.getAllSms(this);
                        if(!lst.isEmpty()) {
                            textView.setText("message " + lst.get(0).getMsg());
                        }
                    }
                }
            }
        }
    }
}