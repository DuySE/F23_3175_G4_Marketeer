package com.example.f23_3175_g4_marketeer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button btnCreateAccount, btnLogin;
    Intent intentLogin;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUsername = findViewById(R.id.editTextUsernameRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnLogin = findViewById(R.id.btnLogin1);
        databaseHelper = new DatabaseHelper(this);
        Register();
    }

    private void Register() {
        intentLogin = new Intent(this, LoginActivity.class);
        btnCreateAccount.setOnClickListener((View view) -> {
            User user = databaseHelper.getUser(editTextUsername.getText().toString());
            if (editTextUsername.getText().toString().isEmpty())
                editTextUsername.setError("Please type your username.");
            else if (user != null)
                editTextUsername.setError("This username is taken. Try another.");
            else if (editTextPassword.getText().toString().isEmpty())
                editTextPassword.setError("Please type your password.");
            else {
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
        btnLogin.setOnClickListener(v -> startActivity(intentLogin));
    }
}