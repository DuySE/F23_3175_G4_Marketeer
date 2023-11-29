package com.example.f23_3175_g4_marketeer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Drawer activity must be extended to function with nav drawer
public class MainActivity extends DrawerActivity implements LocationListener, ItemRecyclerViewAdapter.OnItemClickListener {
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
    private Geocoder geocoder;
    float maxDistance;
    Spinner distanceFilter;
    Toast currToast = null;
    List<Product> filteredList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        allocateActivityTitle("Market");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        userLocation = new Location(LocationManager.GPS_PROVIDER);
        geocoder = new Geocoder(this, Locale.getDefault());

        SetUpSearchView();
        SetUpProductView();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    AskLocationPermission();
                    if (productList.size() != 0) {
                        SetUpDistanceFilter();
                        CalculateDistance();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 2000);
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, (float) 0, (android.location.LocationListener) this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            userLocation.setLatitude(location.getLatitude());
                            userLocation.setLongitude(location.getLongitude());
                            Log.d("ABC", location.getLatitude() + " " + location.getLongitude());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("ERR", "Error trying to get last GPS location");
                        e.printStackTrace();
                    });
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
        productList = db.getProductsNotSeller(StoredDataHelper.get(this, "username"));

        txtViewNoProduct = findViewById(R.id.textViewNoProductFound);
        if (productList.size() == 0){
            txtViewNoProduct.setText(R.string.txtNoProductFound);
        } else {
            recyclerView = findViewById(R.id.recyclerViewItems);
            itemAdapter = new ItemRecyclerViewAdapter(this, productList, this);
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
        filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty()) {
            if (currToast != null){
                currToast.cancel();
            }
            currToast = Toast.makeText(MainActivity.this,"Not Found",Toast.LENGTH_SHORT);
            currToast.show();
        } else {
            itemAdapter.setFilteredList(filteredList);
        }
    }

    private void SetUpDistanceFilter() {
        distanceFilter = findViewById(R.id.spinnerDistanceFilter);
        distanceFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filteredList = new ArrayList<>();
                maxDistance = 0;
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
                    for (int i = 0; i < productList.size(); i++) {
                        if (productList.get(i).getDistanceToUser() > 0 &&
                                (productList.get(i).getDistanceToUser() / 1000) < maxDistance) {
                            filteredList.add(productList.get(i));
                        }
                        Log.d("DISTANCE", productList.get(i).getDistanceToUser() + "");
                    }
                    itemAdapter.setFilteredList(filteredList);
                    if (filteredList.size() == 0) {
                        if (currToast != null){
                            currToast.cancel();
                        }
                        currToast = Toast.makeText(MainActivity.this,"Not Found",Toast.LENGTH_SHORT);
                        currToast.show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void CalculateDistance() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            DatabaseHelper db = new DatabaseHelper(this);

            for (int i=0; i<productList.size(); i++) {
                User user = db.getUser(productList.get(i).getSeller());
                if (user.getAddress() != null) {
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(user.getAddress(), 1);
                        double endLatitude = addressList.get(0).getLatitude();
                        double endLongitude = addressList.get(0).getLongitude();
                        float[] results = new float[1];
                        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                                endLatitude, endLongitude, results);
                        Log.d("DISTANCE", results[0] + "");
                        Log.d("SELLER_COORDINATE", endLatitude + ", " + endLongitude);
                        productList.get(i).setDistanceToUser(results[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());
    }

    @Override
    public void onItemClick(int i) {
        Bundle bundle = new Bundle();
        if (filteredList.size() != 0) {
            bundle.putInt("ID", filteredList.get(i).getId());
        } else {
            bundle.putInt("ID", productList.get(i).getId());
        }
        Intent intent = new Intent(MainActivity.this, ProductInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}