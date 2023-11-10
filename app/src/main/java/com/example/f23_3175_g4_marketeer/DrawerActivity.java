package com.example.f23_3175_g4_marketeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigation;
    ImageButton backButton;
    @Override
    public void setContentView(View view) {
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        FrameLayout container = drawer.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawer);

        Toolbar toolbar = drawer.findViewById(R.id.drawerToolbar);
        setSupportActionBar(toolbar);

        SetUpBackButton();
        navigation = drawer.findViewById(R.id.drawerNavView);
        navigation.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navDrawOpen,
                R.string.navDrawClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    protected void SetUpBackButton() {
        backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener((View view1) -> {
                finish();
            });
        }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        //If else statement to change activities
        if (item.getItemId() == R.id.menuMainActivity) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        }
        else if (item.getItemId() == R.id.menuMyProfileActivity) {
            startActivity(new Intent(this, MyProfileActivity.class));
            overridePendingTransition(0, 0);
        }
        else if (item.getItemId() == R.id.menuChatActivity) {
            startActivity(new Intent(this, ChatActivity.class));
            overridePendingTransition(0, 0);
        }
        else if (item.getItemId() == R.id.menuNewProductActivity) {
            startActivity(new Intent(this, NewProductActivity.class));
            overridePendingTransition(0, 0);
        }
        else if (item.getItemId() == R.id.menuManageProductActivity) {
            startActivity(new Intent(this, ManageProductActivity.class));
            overridePendingTransition(0, 0);
        }
        else if (item.getItemId() == R.id.menuTransactionHistoryActivity) {
            startActivity(new Intent(this, TransactionActivity.class));
            overridePendingTransition(0, 0);
        }
        return false;
    }
    protected void allocateActivityTitle(String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
