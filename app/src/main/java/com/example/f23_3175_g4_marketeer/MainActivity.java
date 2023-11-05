package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<ItemModel> itemModels = new ArrayList<>();
    ItemRecyclerViewAdapter itemAdapter;
    int[] itemImages = {R.drawable.baseline_sentiment_very_satisfied_24,
            R.drawable.baseline_sentiment_satisfied_alt_24,
            R.drawable.baseline_sentiment_very_dissatisfied_24,
            R.drawable.baseline_sentiment_neutral_24};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetUpSearchView();
        SetUpItemModel();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewItems);

        itemAdapter = new ItemRecyclerViewAdapter(this, itemModels);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
    private void SetUpItemModel() {
        String[] itemNames = getResources().getStringArray(R.array.items_name);
        String[] itemPrices = getResources().getStringArray(R.array.items_price);

        for (int i = 0; i < itemNames.length; i++) {
            itemModels.add(new ItemModel(
                    itemNames[i],
                    itemPrices[i],
                    itemImages[i]));
        }
    }
    private void SetUpSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManageProductActivity.class));
            }
        });
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
        List<ItemModel> filteredList = new ArrayList<>();
        for (ItemModel item : itemModels) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            itemAdapter.setFilteredList(filteredList);
        }
    }
}