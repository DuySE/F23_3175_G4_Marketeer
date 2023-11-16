package com.example.f23_3175_g4_marketeer;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
        } else if (item.getItemId() == R.id.menuMyProfileActivity) {
            startActivity(new Intent(this, MyProfileActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.menuChatActivity) {
            startActivity(new Intent(this, UserListActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.menuNewProductActivity) {
            startActivity(new Intent(this, NewProductActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.menuManageProductActivity) {
            startActivity(new Intent(this, ManageProductActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.menuTransactionHistoryActivity) {
            startActivity(new Intent(this, TransactionActivity.class));
            overridePendingTransition(0, 0);
        } else if (item.getItemId() == R.id.menuLogout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_launcher_foreground);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Are you sure to logout?");
            builder.setPositiveButton("OK", (dialog, which) -> {
                StoredDataHelper.clear(this);
                startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
            });
            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return false;
    }

    protected void allocateActivityTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
