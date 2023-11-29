package com.example.f23_3175_g4_marketeer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUsername;
    EditText editTextPassword;
    Button btnLogin, btnRegister;
    Intent intentHome, intentRegister;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String username = StoredDataHelper.get(this, "username");
        if (!username.isEmpty()) startActivity(new Intent(this, HomepageActivity.class));
        editTextUsername = findViewById(R.id.editTextUsernameLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        databaseHelper = new DatabaseHelper(this);
        Login();
    }

    private void Login() {
        intentHome = new Intent(this, HomepageActivity.class);
        btnLogin.setOnClickListener((View view) -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (username.isEmpty()) {
                editTextUsername.setError("Please type your username.");
            } else if (password.isEmpty()) {
                editTextPassword.setError("Please type your password.");
            } else if (databaseHelper.login(username, password)) {
                StoredDataHelper.save(this, "username", username);
                startActivity(intentHome);
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