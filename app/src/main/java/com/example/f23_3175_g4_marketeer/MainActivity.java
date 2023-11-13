package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.f23_3175_g4_marketeer.databinding.ActivityMainBinding;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        allocateActivityTitle("Main");

        SetUpSearchView();
        SetUpProductView();
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
}