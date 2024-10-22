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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.f23_3175_g4_marketeer.databinding.ActivityEditProductBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProductActivity extends DrawerActivity {
    final int CAMERA_PERMISSION_CODE = 1;
    final int CAMERA_REQUEST_CODE = 2;
    final int GALLERY_REQUEST_CODE = 3;
    ImageView imgView;
    EditText editTxtProdName;
    EditText editTxtPrice;
    Button btnCamera;
    Button btnGallery;
    Button btnEditProd;
    String imgPath;
    RadioGroup radGroupStatus;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    String imgName;
    Uri imgUri;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditProductBinding editProductBinding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(editProductBinding.getRoot());
        allocateActivityTitle("Edit Your Product");

        imgView = findViewById(R.id.imgViewEditProduct);
        editTxtProdName = findViewById(R.id.editTxtProductNameEdit);
        editTxtPrice = findViewById(R.id.editTxtPriceEdit);
        btnCamera = findViewById(R.id.btnNewImageEdit);
        btnGallery = findViewById(R.id.btnGalleryImgEdit);
        btnEditProd = findViewById(R.id.btnEditProduct);
        radGroupStatus = findViewById(R.id.radGroupStatus);

        Bundle bundle = getIntent().getExtras();

        DatabaseHelper db = new DatabaseHelper(this);
        Product product = db.getProduct(bundle.getInt("ID"));
        editTxtProdName.setText(product.getName());
        editTxtPrice.setText(product.getPrice().replace("$",""));

        StorageReference img = storageReference.child("ProductImg/" + product.getImgName());
        imgName = product.getImgName();
        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgView);
                imgUri = uri;
            }
        });


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

        btnEditProd.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(EditProductActivity.this);
                if (editTxtProdName.getText().toString().isEmpty()) {
                    Toast.makeText(EditProductActivity.this, "Please name your product", Toast.LENGTH_SHORT).show();
                } else if (editTxtPrice.getText().toString().isEmpty()) {
                    Toast.makeText(EditProductActivity.this, "Please enter the price of your product", Toast.LENGTH_SHORT).show();
                } else if (imgView.getDrawable() == null) {
                    Toast.makeText(EditProductActivity.this, "Please select an image for your product", Toast.LENGTH_SHORT).show();
                } else {
                    UploadEditedProduct(imgName, imgUri);
                    if (radGroupStatus.getCheckedRadioButtonId() == R.id.radBtnAvailable) {
                        status = "Available";
                    } else if (radGroupStatus.getCheckedRadioButtonId() == R.id.radBtnSold) {
                        status = "Sold";
                    }

                    DecimalFormat df = new DecimalFormat("$#.##");
                    String price = df.format(Double.parseDouble(editTxtPrice.getText().toString()));
                    String username = StoredDataHelper.get(EditProductActivity.this, "username");
                    databaseHelper.updateProduct(product.getId(), editTxtProdName.getText().toString(),
                         price, username, status, imgName);
                    // Add to transaction if a product is sold
                    if (status.equals("Sold")) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String transactionDate = formatter.format(date);
                        Transaction transaction = new Transaction(
                                transactionDate,
                                editTxtProdName.getText().toString(),
                                imgName, username);
                        transaction.setAmount(1);
                        databaseHelper.addTransaction(transaction);

                    }

                }
                startActivity(new Intent(EditProductActivity.this, ManageProductActivity.class));
            }
        }));
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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

    private File createImgFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + time + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File imgFile = File.createTempFile(imgFileName, ".jpg", storageDirectory);
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

    private void UploadEditedProduct(String imgName, Uri imgUri) {
        StorageReference img = storageReference.child("ProductImg/" + imgName);
        img.putFile(imgUri);
    }
}