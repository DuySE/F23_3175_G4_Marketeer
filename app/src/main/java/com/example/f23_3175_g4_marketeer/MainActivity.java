package com.example.f23_3175_g4_marketeer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.f23_3175_g4_marketeer.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

//Drawer activity must be extended to function with nav drawer
public class MainActivity extends DrawerActivity {
    //Binding used for navigation drawer
    ActivityMainBinding mainBinding;
    List<Product> productList = new ArrayList<>();
    ItemRecyclerViewAdapter itemAdapter;
    TextView txtViewNoProduct;
    RecyclerView recyclerView;
    private Location userLocation;
    final int LOCATION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        allocateActivityTitle("Main");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SetUpSearchView();
        SetUpProductView();
        if (productList.size() != 0) {
            SetUpDistanceFilter();
        }
        AskLocationPermission();
    }

    private void AskLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            //get current location
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   //part 1
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Marketeer may need to be restarted for Distance Filter to work", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission is needed to use Distance Filter. Please turn it on in Settings to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetUpProductView() {
        // refactored some code from ManageProductActivity
        DatabaseHelper db = new DatabaseHelper(this);
        productList = db.getProducts();

        txtViewNoProduct = findViewById(R.id.textViewNoProductFound);
        if (productList.size() == 0){
            txtViewNoProduct.setText(R.string.txtNoProductFound);
        } else {
            recyclerView = findViewById(R.id.recyclerViewItems);
            itemAdapter = new ItemRecyclerViewAdapter(this, productList);
            recyclerView.setAdapter(itemAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }
    private void SetUpSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }
    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            itemAdapter.setFilteredList(filteredList);
        }
    }

    private void SetUpDistanceFilter() {
        Spinner distanceFilter = findViewById(R.id.spinnerDistanceFilter);
        distanceFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Product> filteredList = new ArrayList<>();
                float maxDistance = 0;
                if (position == 0) {
                    itemAdapter.setFilteredList(productList);
                } else if (position == 1) {
                    maxDistance = 2;
                } else if (position == 2) {
                    maxDistance = 5;
                } else if (position == 3) {
                    maxDistance = 10;
                } else if (position == 4) {
                    maxDistance = 20;
                }
                if (position != 0) {
                    //loop and compare the products.getDistanceToUser with maxDistance, adding to filteredList if met condition
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}