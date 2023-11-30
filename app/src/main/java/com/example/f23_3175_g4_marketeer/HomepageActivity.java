package com.example.f23_3175_g4_marketeer;


import android.os.Bundle;
import android.widget.TextView;

import com.example.f23_3175_g4_marketeer.databinding.ActivityHomepageBinding;

//This activity is mainly here to add a buffer so user wont crash on back button press.
public class HomepageActivity extends DrawerActivity {
    ActivityHomepageBinding homepageBinding;
    User user;
    @Override
    protected void SetUpBackButton() {
        //Do nothing (to avoid a crash)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homepageBinding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(homepageBinding.getRoot());
        allocateActivityTitle("Homepage");

        DatabaseHelper db = new DatabaseHelper(this);
        user = db.getUser(StoredDataHelper.get(this, "username"));
        TextView textViewUsername = findViewById(R.id.textHomeUsername);
        textViewUsername.setText(user.getUsername());


    }
}