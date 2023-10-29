package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyProfileActivity extends AppCompatActivity {
    TextView txtViewUsername;
    TextView txtViewPassword;
    TextView txtViewPhone;
    TextView txtViewAddress;
    Button btn;
    Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        txtViewUsername = findViewById(R.id.txtViewUsername);
        txtViewPassword = findViewById(R.id.txtViewUserPassword);
        txtViewPhone = findViewById(R.id.txtViewPhone);
        txtViewAddress = findViewById(R.id.txtViewAddress);
        btn = findViewById(R.id.btnEditProfile);
        btn2 = findViewById(R.id.btnShowPassword);
        setProfile();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME",txtViewUsername.getText().toString());
                bundle.putString("PASSWORD",txtViewPassword.getText().toString());
                if (txtViewPhone.getText().toString().length()==10){
                    bundle.putString("PHONE",txtViewPhone.getText().toString());
                }

                String address = txtViewAddress.getText().toString();
                try {
                    String city = address.split(", ")[1];
                    String province = address.split(", ")[2];
                    String numberAndStreet = address.split(", ")[0];
                    String homeNumber = address.split(" ")[0];

                    String[] streetNames = numberAndStreet.split(" ");
                    String streetName = "";
                    for (int i=1; i<streetNames.length; i++){
                        streetName += streetNames[i] + " ";
                    }
                    bundle.putString("HOMENUMBER",homeNumber);
                    bundle.putString("STREET",streetName);
                    bundle.putString("CITY",city);
                    bundle.putString("PROVINCE",province);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MyProfileActivity.this, MyProfileEditActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword();
            }
        });
    }

    private void showPassword() {
        String password = "";
        try{
            Bundle inBundle = getIntent().getExtras();
            password = inBundle.getString("PASSWORD");
            String passwordStr = "";
            if (btn2.getText().toString().equals("SHOW")){
                txtViewPassword.setText(password);
                btn2.setText(R.string.txtHide);
            } else {
                for (int i=0; i < password.length(); i++){
                    passwordStr+="*";
                    txtViewPassword.setText(passwordStr);
                }
                btn2.setText(R.string.txtShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setProfile(){
        String password = "";
        try {
            Bundle inBundle = getIntent().getExtras();
            String username = StoredDataHelper.get(this, "username");
            txtViewUsername.setText(username);
            txtViewAddress.setText(inBundle.getString("ADDRESS","None"));
            txtViewPhone.setText(inBundle.getString("PHONE","None"));
            String inPassword = inBundle.getString("PASSWORD", "error");

            if (inPassword.equals("error")){
                txtViewPassword.setText(R.string.error);
            } else {
                for (int i=0; i<inPassword.length(); i++){
                    password += "*";
                }
                txtViewPassword.setText(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}