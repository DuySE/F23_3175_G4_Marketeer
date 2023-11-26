package com.example.f23_3175_g4_marketeer;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.MyViewHolder> {
    List<Product> products;
    OnItemClickListener onItemClickListener;

    public ProductRecyclerViewAdapter(List<Product> products, OnItemClickListener onItemClickListener) {
        this.products = products;
        this.onItemClickListener = onItemClickListener;
    }

    public void setFilteredList(List<Product> filteredList) {
        products = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_recycler_view, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtViewName.setText(products.get(position).getName());
        holder.txtViewPrice.setText(products.get(position).getPrice());
        String imgName = products.get(position).getImgName();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                StorageReference img = storageReference.child("ProductImg/" + imgName);
                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(holder.imgView);
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,2000);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView, imgViewEdit;
        TextView txtViewName, txtViewPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgViewRecyclerImg);
            txtViewName = itemView.findViewById(R.id.txtViewRecyclerName);
            txtViewPrice = itemView.findViewById(R.id.txtViewRecyclerPrice);
            imgViewEdit = itemView.findViewById(R.id.imgViewRecyclerEdit);
            imgViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int i);
    }
}
