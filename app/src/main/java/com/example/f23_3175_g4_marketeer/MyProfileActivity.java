package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MyProfileActivity extends AppCompatActivity {
    TextView txtViewUsername;
    TextView txtViewPassword;
    TextView txtViewPhone;
    TextView txtViewAddress;
    Button btnEditProfile;
    Button btnShowPassword;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        txtViewUsername = findViewById(R.id.txtViewUsername);
        txtViewPassword = findViewById(R.id.txtViewUserPassword);
        txtViewPhone = findViewById(R.id.txtViewPhone);
        txtViewAddress = findViewById(R.id.txtViewAddress);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnShowPassword = findViewById(R.id.btnShowPassword);
        setProfile();
        btnEditProfile.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("USERNAME", txtViewUsername.getText().toString());
            bundle.putString("PASSWORD", txtViewPassword.getText().toString());
            if (txtViewPhone.getText().toString().length() == 10) {
                bundle.putString("PHONE", txtViewPhone.getText().toString());
            }
            String address = txtViewAddress.getText().toString();
            try {
                String city = address.split(", ")[1];
                String province = address.split(", ")[2];
                String numberAndStreet = address.split(", ")[0];
                String homeNumber = address.split(" ")[0];

                String[] streetNames = numberAndStreet.split(" ");
                String streetName = "";
                for (int i = 1; i < streetNames.length; i++) {
                    streetName += streetNames[i] + " ";
                }
                bundle.putString("HOMENUMBER", homeNumber);
                bundle.putString("STREET", streetName);
                bundle.putString("CITY", city);
                bundle.putString("PROVINCE", province);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(MyProfileActivity.this, MyProfileEditActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnShowPassword.setOnClickListener(view -> showPassword());
    }

    private void showPassword() {
        String password = "";
        String username = StoredDataHelper.get(this, "username");
        User user = databaseHelper.getUser(username);
        try {
            Bundle inBundle = getIntent().getExtras();
            password = user.getPassword();
            String passwordStr = "";
            if (btnShowPassword.getText().toString().equals("SHOW")) {
                txtViewPassword.setText(password);
                btnShowPassword.setText(R.string.txtHide);
            } else {
                for (int i = 0; i < password.length(); i++) {
                    passwordStr += "*";
                    txtViewPassword.setText(passwordStr);
                }
                btnShowPassword.setText(R.string.txtShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProfile() {
        String username = StoredDataHelper.get(this, "username");
        String hiddenPassword = "";
        User user = databaseHelper.getUser(username);
        try {
            Bundle inBundle = getIntent().getExtras();
            txtViewUsername.setText(user.getUsername());
            txtViewAddress.setText(user.getAddress());
            txtViewPhone.setText(user.getPhone());
            String inPassword = user.getPassword();
            if (inPassword.equals("error")) {
                txtViewPassword.setText(R.string.error);
            } else {
                for (int i = 0; i < inPassword.length(); i++) {
                    hiddenPassword += "*";
                }
                txtViewPassword.setText(hiddenPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}