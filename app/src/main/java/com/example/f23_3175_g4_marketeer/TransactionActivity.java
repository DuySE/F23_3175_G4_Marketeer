package com.example.f23_3175_g4_marketeer;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String username = StoredDataHelper.get(this, "username");
        List<Transaction> transactions = databaseHelper.getTransactions(username, null);
        TransactionAdapter transactionAdapter = new TransactionAdapter(transactions);
        ListView listViewTransactions = findViewById(R.id.listViewTransactions);
        listViewTransactions.setAdapter(transactionAdapter);
        // Filter by date
        EditText editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth
                        );
                        editTextDate.setText(selectedDate);
                        transactions.clear();
                        List<Transaction> newTransactions = databaseHelper.getTransactions(username,
                                editTextDate.getText().toString());
                        add(transactions, newTransactions);
                        transactionAdapter.notifyDataSetChanged();
                    }, year, month, day);
            datePickerDialog.show();
        });
        LineChart chart = findViewById(R.id.chart);
        List<String> xAxisValues = new ArrayList<>(); // x-axis
        List<Entry> entries = new ArrayList<>(); // data
        List<Transaction> transactionData =
                databaseHelper.getTransactionsChart(username);
        for (int i = 0; i < transactionData.size(); i++) {
            entries.add(new Entry(i, transactionData.get(i).getAmount()));
        }

        chart.getXAxis().setValueFormatter(
                new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        List<LineDataSet> lines = new ArrayList<>();
        LineDataSet set = new LineDataSet(entries, "Quantity");
        set.setDrawFilled(true);
        set.setFillColor(Color.WHITE);
        set.setColor(Color.RED);
        set.setCircleColor(Color.DKGRAY);
        lines.add(set);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        if (transactions.size() > 0) {
            chart.setData(new LineData(dataSets));
            chart.getDescription().setText("");
            chart.getDescription().setTextColor(Color.RED);
            chart.animateY(1400, Easing.EaseInOutBounce);
            chart.invalidate();
        }
    }

    private void add(List<Transaction> oldList, List<Transaction> newList) {
        oldList.clear();
        oldList.addAll(newList);
    }
}