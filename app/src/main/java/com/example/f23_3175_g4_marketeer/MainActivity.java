package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.f23_3175_g4_marketeer.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

//Drawer activity must be extended to function with nav drawer
public class MainActivity extends DrawerActivity {
    //Binding used for navigation drawer
    ActivityMainBinding mainBinding;
    ArrayList<ItemModel> itemModels = new ArrayList<>();
    ItemRecyclerViewAdapter itemAdapter;
    int[] itemImages = {R.drawable.baseline_sentiment_very_satisfied_24,
            R.drawable.baseline_sentiment_satisfied_alt_24,
            R.drawable.baseline_sentiment_very_dissatisfied_24,
            R.drawable.baseline_sentiment_neutral_24};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        allocateActivityTitle("Main");

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