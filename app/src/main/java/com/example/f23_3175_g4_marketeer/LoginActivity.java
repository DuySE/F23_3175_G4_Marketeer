package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    TextView textViewLogin;
    EditText editTextUsername;
    EditText editTextPassword;
    Button btnLogin, btnRegister;
    Intent intentMain, intentRegister;
    String username, password;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            Bundle bundle = getIntent().getExtras();
            username = bundle.getString("USERNAME", "");
            password = bundle.getString("PASSWORD", "");
            //usernameList.add(username);
            //passwordList.add(password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        textViewLogin = findViewById(R.id.textViewLoginTitle);
        editTextUsername = findViewById(R.id.editTextUsernameLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        databaseHelper = new DatabaseHelper(this);
        Login();
    }

    private void Login() {
        intentMain = new Intent(this, MainActivity.class);
        btnLogin.setOnClickListener((View view) -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (username.isEmpty()) {
                editTextUsername.setError("Please type your username.");
            } else if (password.isEmpty()) {
                editTextPassword.setError("Please type your password.");
            } else if (databaseHelper.getUser(username, password)) {
                startActivity(intentMain);
            } else {
                Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
            }
        });
        btnRegister.setOnClickListener((View view) -> {
            intentRegister = new Intent(this, RegisterActivity.class);
            startActivity(intentRegister);
        });
    }
}