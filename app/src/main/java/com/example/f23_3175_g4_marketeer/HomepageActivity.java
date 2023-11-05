package com.example.f23_3175_g4_marketeer;


import android.os.Bundle;

import com.example.f23_3175_g4_marketeer.databinding.ActivityHomepageBinding;

public class HomepageActivity extends DrawerActivity {
    //will possibly change this out for main activity in the future
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