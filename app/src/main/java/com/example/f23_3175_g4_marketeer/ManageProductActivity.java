package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ManageProductActivity extends AppCompatActivity implements ProductRecyclerViewAdapter.OnItemClickListener {
    List<Product> products = new ArrayList<>();
    RecyclerView recyclerViewProduct;
    ProductRecyclerViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtViewNoProduct = findViewById(R.id.textViewNoProductFound);

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

    @Override
    public void onItemClick(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", i);
        Intent intent = new Intent(ManageProductActivity.this, EditProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}