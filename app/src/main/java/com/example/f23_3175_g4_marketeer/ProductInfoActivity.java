package com.example.f23_3175_g4_marketeer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.f23_3175_g4_marketeer.databinding.ActivityProductInfoBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProductInfoActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductInfoBinding productInfoBinding = ActivityProductInfoBinding.inflate(getLayoutInflater());
        setContentView(productInfoBinding.getRoot());
        allocateActivityTitle("Product Information");

        ImageView imgView = findViewById(R.id.imgViewProdInfo);
        TextView txtViewName = findViewById(R.id.txtViewProdNameInfo);
        TextView txtViewPrice = findViewById(R.id.txtViewProdPriceInfo);
        TextView txtViewSeller = findViewById(R.id.txtViewProdSellerInfo);
        ImageView imgViewChat = findViewById(R.id.imgViewChatWithSeller);

        DatabaseHelper db = new DatabaseHelper(ProductInfoActivity.this);
        Product product = db.getProduct(6); //will be the id passed from MainActivity
        txtViewName.setText(product.getName());
        txtViewPrice.setText(product.getPrice());
        txtViewSeller.setText("Sold by: " + product.getSeller());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference img = storageReference.child("ProductImg/" + product.getImgName());
        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgView);
            }
        });

        imgViewChat.setOnClickListener(v -> {
            //start chatActivity with the seller of this product
            Bundle bundle = new Bundle();
            bundle.putString("SELLER",product.getSeller());
            Intent intent = new Intent(ProductInfoActivity.this, ChatActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}