package com.example.f23_3175_g4_marketeer;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TransactionAdapter extends BaseAdapter {
    List<Transaction> adapterTransactions;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public TransactionAdapter(List<Transaction> adapterTransactions) {
        this.adapterTransactions = adapterTransactions;
    }

    public void setFilteredList(List<Transaction> filteredList) {
        this.adapterTransactions = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adapterTransactions.size();
    }

    @Override
    public Object getItem(int i) {
        return adapterTransactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.layout_transaction, viewGroup, false);
        }
        TextView txtViewTransactionProductName = view.findViewById(R.id.txtViewTransactionProductName);
        TextView txtViewTransactionDate = view.findViewById(R.id.txtViewTransactionDate);
        ImageView productImage = view.findViewById(R.id.productImage);
        txtViewTransactionProductName.setText(adapterTransactions.get(i).getProductName());
        txtViewTransactionDate.setGravity(Gravity.RIGHT);
        txtViewTransactionDate.setText(adapterTransactions.get(i).getDate());
        StorageReference img = storageReference.child("ProductImg/" +
                adapterTransactions.get(i).getImageName());
        img.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(productImage);
        });
        return view;
    }
}