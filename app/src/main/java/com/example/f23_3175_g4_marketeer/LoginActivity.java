package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //no create account feature yet

    TextView textViewLogin;
    EditText editTextUsername;
    EditText editTextPassword;
    Button btnLogin, btnRegister;
    Intent intentMain, intentRegister;
    public static String username;
    String password;

    //couldnt figure out how to store new accounts in array, so i will come back to this when the database is implemented
    //List<String> usernameList = new ArrayList<>();
    //List<String> passwordList = new ArrayList<>();
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
        Login();
    }

    private void Login() {
        intentMain = new Intent(this, MainActivity.class);
        btnLogin.setOnClickListener((View view) -> {
            if(editTextUsername.getText().toString().isEmpty()) {
                Toast.makeText(this, "Must input username", Toast.LENGTH_SHORT).show();
            } else if(editTextPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Must input password", Toast.LENGTH_SHORT).show();
            } else if(editTextPassword.getText().toString().equals("admin") && editTextUsername.getText().toString().equals("admin")) {
                startActivity(intentMain);
            }
            else if(editTextUsername.getText().toString().equals(username) && editTextPassword.getText().toString().equals(password)) {
                startActivity(intentMain);
            }
            username = editTextUsername.getText().toString();
                /*for(int i = 0; i < usernameList.size(); i++) {
                    if(editTextUsername.getText().toString().equals(usernameList.get(i)) && editTextPassword.getText().toString().equals(passwordList.get(i))) {
                        startActivity(intentMain);
                    }
                }*/
        });
        btnRegister.setOnClickListener((View view) -> {
            intentRegister = new Intent(this, RegisterActivity.class);
            startActivity(intentRegister);
        });
    }

}