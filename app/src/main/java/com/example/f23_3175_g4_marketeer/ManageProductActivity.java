package com.example.f23_3175_g4_marketeer;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.f23_3175_g4_marketeer.databinding.ActivityMainBinding;
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
            searchView.setVisibility(View.GONE);
            txtViewNoProduct.setText(R.string.txtNoProductFound);
            txtViewNoProduct.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txtViewNoProduct.getLayoutParams();
            params.setMargins(0,500,0,0);
            txtViewNoProduct.setLayoutParams(params);
        } else {
            searchView.setVisibility(View.VISIBLE);
            txtViewNoProduct.setVisibility(View.GONE);
            recyclerViewProduct = findViewById(R.id.recyclerViewItems);
            myAdapter = new ProductRecyclerViewAdapter(products, this);
            recyclerViewProduct.setAdapter(myAdapter);
            recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        }

        addNewProdImgView.setOnClickListener((View view) -> startActivity(new Intent(ManageProductActivity.this, NewProductActivity.class)));
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
        bundle.putInt("ID", products.get(i).getId());
        Intent intent = new Intent(ManageProductActivity.this, EditProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}