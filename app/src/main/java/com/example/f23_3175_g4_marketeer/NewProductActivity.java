package com.example.f23_3175_g4_marketeer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.f23_3175_g4_marketeer.databinding.ActivityMainBinding;
import com.example.f23_3175_g4_marketeer.databinding.ActivityNewProductBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewProductActivity extends DrawerActivity {
    ActivityNewProductBinding newProductBinding;
    ImageView imgView;
    EditText editTxtProdName;
    EditText editTxtPrice;
    Button btnCamera;
    Button btnGallery;
    Button btnAddProd;
    String imgPath;
    final int CAMERA_PERMISSION_CODE = 1;
    final int CAMERA_REQUEST_CODE = 2;
    final int GALLERY_REQUEST_CODE = 3;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    String imgName;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newProductBinding = ActivityNewProductBinding.inflate(getLayoutInflater());
        setContentView(newProductBinding.getRoot());
        allocateActivityTitle("New Product");

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
                    UploadNewProduct(imgName, imgUri);
                    Toast.makeText(NewProductActivity.this, "Your product has been added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewProductActivity.this, ManageProductActivity.class));
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imgFile = null;
        try {
            imgFile = createImgFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (imgFile != null) {
            Uri imgURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", imgFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private File createImgFile() throws IOException{
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + time + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File imgFile = File.createTempFile(imgFileName,".jpg", storageDirectory);
        imgPath = imgFile.getAbsolutePath();
        return imgFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && imgPath != null) {
                File imgFile = new File(imgPath);
                imgUri = Uri.fromFile(imgFile);
                imgView.setImageURI(imgUri);
                imgName = imgFile.getName();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(imgUri);
                this.sendBroadcast(mediaScanIntent);
            } else {
                Toast.makeText(this, "Creating file failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imgUri = data.getData();
                String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                ContentResolver resolver = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String extension = mime.getExtensionFromMimeType(resolver.getType(imgUri));

                imgName = "JPEG_" + time + "." + extension;
                imgView.setImageURI(imgUri);
            }
        }
    }

    private void UploadNewProduct(String imgName, Uri imgUri){
        StorageReference img = storageReference.child("ProductImg/" + imgName);
        img.putFile(imgUri);
        DecimalFormat df = new DecimalFormat("$#.##");
        String price = df.format(Double.parseDouble(editTxtPrice.getText().toString()));

        DatabaseHelper databaseHelper = new DatabaseHelper(NewProductActivity.this);
        databaseHelper.addProduct(editTxtProdName.getText().toString(), price,
                StoredDataHelper.get(this,"username"), imgName);
    }
}