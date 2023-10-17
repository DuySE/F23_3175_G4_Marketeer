package com.example.f23_3175_g4_marketeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class NewProductActivity extends AppCompatActivity {
    ImageView imgView;
    EditText editTxtProdName;
    EditText editTxtPrice;
    Button btnCamera;
    Button btnGallery;
    Button btnAddProd;

    String imgPath;
    final int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        imgView = findViewById(R.id.imgViewProductImg);
        editTxtPrice = findViewById(R.id.editTxtPrice);
        editTxtProdName = findViewById(R.id.editTxtProductName);
        btnCamera = findViewById(R.id.btnNewImg);
        btnGallery = findViewById(R.id.btnGalleryImg);
        btnAddProd = findViewById(R.id.btnAddNewProduct);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAddProd.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTxtProdName.getText().toString().isEmpty()) {
                    Toast.makeText(NewProductActivity.this, "Please name your product", Toast.LENGTH_SHORT).show();
                } else if (editTxtPrice.getText().toString().isEmpty()) {
                    Toast.makeText(NewProductActivity.this, "Please enter the price of your product", Toast.LENGTH_SHORT).show();
                } else if (imgView.getDrawable() == null) {
                    Toast.makeText(NewProductActivity.this, "Please select an image for your product", Toast.LENGTH_SHORT).show();
                } else {
                    //put the product in the database
                }
            }
        }));
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            takePicture();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePicture() {

    }

}