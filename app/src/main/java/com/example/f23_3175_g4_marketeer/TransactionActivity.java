package com.example.f23_3175_g4_marketeer;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {
    private TransactionAdapter transactionAdapter;
    List<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String username = StoredDataHelper.get(this, "username");
        transactions = databaseHelper.getTransactions(username, null);
        transactionAdapter = new TransactionAdapter(transactions);
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
                        List<Transaction> filteredTransactions = databaseHelper.getTransactions(username,
                                editTextDate.getText().toString());
                        transactionAdapter.setFilteredList(filteredTransactions);
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
        YAxis leftYAxis = chart.getAxisLeft();
        YAxis rightYAxis = chart.getAxisRight();
        // avoid duplicate value when zooming
        leftYAxis.setGranularity(1f);
        rightYAxis.setGranularity(1f);
        // cast Y-axis values to integer
        leftYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        rightYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
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
        // Setup search view
        SetUpSearchView();
    }

    private void SetUpSearchView() {
        SearchView searchView = findViewById(R.id.transactionSearchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, transactions);
                return true;
            }
        });
    }

    private void filterList(String text, List<Transaction> transactions) {
        List<Transaction> filteredList = new ArrayList<>();
        if (transactions != null)
            for (Transaction transaction : transactions)
                if (transaction.getProductName().toLowerCase().contains(text.toLowerCase()))
                    filteredList.add(transaction);
        if (filteredList.isEmpty())
            Toast.makeText(this, "Transaction not found!", Toast.LENGTH_SHORT).show();
        else transactionAdapter.setFilteredList(filteredList);
    }
}