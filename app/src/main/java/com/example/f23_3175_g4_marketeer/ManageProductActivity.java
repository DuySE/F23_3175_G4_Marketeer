package com.example.f23_3175_g4_marketeer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f23_3175_g4_marketeer.databinding.ActivityManageProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ManageProductActivity extends DrawerActivity implements ProductRecyclerViewAdapter.OnItemClickListener {
    ActivityManageProductBinding manageProductBinding;
    List<Product> products = new ArrayList<>();
    RecyclerView recyclerViewProduct;
    ProductRecyclerViewAdapter myAdapter;
    SearchView searchView;
    TextView txtViewNoProduct;
    List<Product> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manageProductBinding = ActivityManageProductBinding.inflate(getLayoutInflater());
        setContentView(manageProductBinding.getRoot());
        allocateActivityTitle("Manage Products");

        txtViewNoProduct = findViewById(R.id.textViewNoProductFound);
        searchView = findViewById(R.id.searchView);
        ImageView addNewProdImgView = findViewById(R.id.startNewProdActivityImgView);
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

        addNewProdImgView.setOnClickListener((View view) -> startActivity(new Intent(ManageProductActivity.this, NewProductActivity.class)));
    }

    private void filterProduct(String newText, List<Product> products) {
        filteredList = new ArrayList<>();
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
        if (filteredList.size() != 0) {
            bundle.putInt("ID", filteredList.get(i).getId());
        } else {
            bundle.putInt("ID", products.get(i).getId());
        }
        Intent intent = new Intent(ManageProductActivity.this, EditProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}