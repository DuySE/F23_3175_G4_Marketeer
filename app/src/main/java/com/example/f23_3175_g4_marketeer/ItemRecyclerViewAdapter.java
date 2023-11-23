package com.example.f23_3175_g4_marketeer;

import android.content.Context;
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

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.MyViewHolder> {
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    List<Product> productList;
    OnItemClickListener onItemClickListener;
    public ItemRecyclerViewAdapter(Context context, List<Product> productList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }
    public void setFilteredList(List<Product> filteredList){
        this.productList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new ItemRecyclerViewAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.textViewName.setText(productList.get(position).getName());
        holder.textViewPrice.setText(productList.get(position).getPrice());
        holder.textViewSeller.setText(productList.get(position).getSeller());
        StorageReference img = storageReference.child("ProductImg/" + productList.get(position).getImgName());
        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imageView);
            }
        });
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewName, textViewPrice, textViewSeller;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgViewRecyclerImg);
            textViewName = itemView.findViewById(R.id.txtViewRecyclerName);
            textViewPrice = itemView.findViewById(R.id.txtViewRecyclerPrice);
            textViewSeller = itemView.findViewById(R.id.txtViewRecyclerSeller);
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int i);
    }
}