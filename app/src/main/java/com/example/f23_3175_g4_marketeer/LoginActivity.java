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

    //no create account feature yet

    TextView textViewLogin;
    EditText editTextUsername;
    EditText editTextPassword;
    Button btnLogin;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewLogin = findViewById(R.id.textViewLogin);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Login();
    }

    private void Login() {
        intent = new Intent(this, MainActivity.class);
        btnLogin.setOnClickListener((View view) -> {
            if(editTextUsername.getText().toString().isEmpty()) {
                Toast.makeText(this, "Must input username", Toast.LENGTH_SHORT).show();
            } else if(editTextPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Must input password", Toast.LENGTH_SHORT).show();
            } else {
                if(editTextUsername.getText().toString().equals("admin") && editTextPassword.getText().toString().equals("admin")) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Username or password not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}