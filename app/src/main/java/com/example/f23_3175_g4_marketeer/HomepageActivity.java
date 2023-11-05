package com.example.f23_3175_g4_marketeer;


import android.os.Bundle;

import com.example.f23_3175_g4_marketeer.databinding.ActivityHomepageBinding;

//This activity is mainly here to add a buffer so user wont crash on back button press
public class HomepageActivity extends DrawerActivity {
    ActivityHomepageBinding homepageBinding;

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
    }
}