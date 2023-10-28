package com.example.f23_3175_g4_marketeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class DrawerActivity extends AppCompatActivity{
    public DrawerLayout drawer;
    public NavigationView navigation;
    Intent intentMain, intentEditProduct, intentMyProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        SetUpDrawer();
    }
    public void SetUpDrawer() {
        intentMain = new Intent(this, MainActivity.class);
        intentEditProduct = new Intent(this, EditProductActivity.class);
        intentMyProfile = new Intent(this, MyProfileActivity.class);
        Toolbar toolbar = findViewById(R.id.drawerToolbar);
        navigation = findViewById(R.id.drawerNavView);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.activity_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navDrawOpen,
                R.string.navDrawClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if (id == R.id.menuMainActivity){
                    Toast.makeText(DrawerActivity.this, "Main Selected", Toast.LENGTH_SHORT).show();
                    startActivity(intentMain);
                } else if(id==R.id.menuEditProductActivity) {
                    startActivity(intentEditProduct);
                } else if(id==R.id.menuMyProfileActivity) {
                    startActivity(intentMyProfile);
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}