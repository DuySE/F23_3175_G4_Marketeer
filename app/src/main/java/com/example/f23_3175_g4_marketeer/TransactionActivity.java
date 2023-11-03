package com.example.f23_3175_g4_marketeer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {

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
    }

    private void add(List<Transaction> oldList, List<Transaction> newList) {
        oldList.clear();
        oldList.addAll(newList);
    }
}