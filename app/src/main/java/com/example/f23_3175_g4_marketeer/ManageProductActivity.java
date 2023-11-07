package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ManageProductActivity extends AppCompatActivity implements ProductRecyclerViewAdapter.OnItemClickListener {
    List<Product> products = new ArrayList<>();
    RecyclerView recyclerViewProduct;
    ProductRecyclerViewAdapter myAdapter;
    SearchView searchView;
    TextView txtViewNoProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtViewNoProduct = findViewById(R.id.textViewNoProductFound);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProduct(newText, products);
                return true;
            }
        });

        //query and set list
        DatabaseHelper db = new DatabaseHelper(this);
        products = db.getProducts(StoredDataHelper.get(this, "username"));

        if (products.size() == 0){
            txtViewNoProduct.setText(R.string.txtNoProductFound);
        } else {
            recyclerViewProduct = findViewById(R.id.recyclerViewItems);
            myAdapter = new ProductRecyclerViewAdapter(products, this);
            recyclerViewProduct.setAdapter(myAdapter);
            recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    private void filterProduct(String newText, List<Product> products) {
        List<Product> filteredList = new ArrayList<>();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(products.get(i));
                }
            }
        }
        if (filteredList.size() == 0) {
            txtViewNoProduct.setText(R.string.txtNoProductFound);
        } else {
            txtViewNoProduct.setText("");
        }
        myAdapter.setFilteredList(filteredList);
    }

    @Override
    public void onItemClick(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", i);
        Intent intent = new Intent(ManageProductActivity.this, EditProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}