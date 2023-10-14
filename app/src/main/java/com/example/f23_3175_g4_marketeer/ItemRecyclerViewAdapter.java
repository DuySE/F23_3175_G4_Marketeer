package com.example.f23_3175_g4_marketeer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.MyViewHolder> {
    Context context;
    List<ItemModel> itemModels;
    public ItemRecyclerViewAdapter(Context context, ArrayList<ItemModel> itemModels){
        this.context = context;
        this.itemModels = itemModels;
    }
    public void setFilteredList(List<ItemModel> filteredList){
        this.itemModels = filteredList;
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
        holder.textViewName.setText(itemModels.get(position).getName());
        holder.textViewPrice.setText(itemModels.get(position).getPrice());
        holder.imageView.setImageResource(itemModels.get(position).getImage());
    }
    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewName, textViewPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}