package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button btnCreateAccount;
    Intent intentLogin;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUsername = findViewById(R.id.editTextUsernameRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        Register();
    }

    private void Register() {
        // For now this is very basic registration that will only work one time
        intentLogin = new Intent(this, LoginActivity.class);
        btnCreateAccount.setOnClickListener((View view) -> {
            if (editTextUsername.getText().toString().isEmpty()) {
                editTextUsername.setError("Please type your username.");
            } else if (editTextPassword.getText().toString().isEmpty()) {
                editTextPassword.setError("Please type your password.");
            } else {
                try {
                    String newUsername = editTextUsername.getText().toString();
                    String newPassword = editTextPassword.getText().toString();
                    databaseHelper = new DatabaseHelper(this);
                    databaseHelper.addUser(newUsername, newPassword);
                    startActivity(intentLogin);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}