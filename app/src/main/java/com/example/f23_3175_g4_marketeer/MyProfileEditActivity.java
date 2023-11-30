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

import com.example.f23_3175_g4_marketeer.databinding.ActivityMyProfileEditBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyProfileEditActivity extends DrawerActivity {
    EditText editTxtUsername;
    EditText editTxtHomeNumber;
    EditText editTxtStreetName;
    EditText editTxtCity;
    EditText editTxtProvince;
    EditText editTxtPhone;
    ImageView imgView;
    Button btnCamera;
    Button btnGallery;
    EditText editTxtNewPassword;
    EditText editTxtCurrentPassword;
    String imgPath;
    final int CAMERA_PERMISSION_CODE = 1;
    final int CAMERA_REQUEST_CODE = 2;
    final int GALLERY_REQUEST_CODE = 3;
    DatabaseHelper databaseHelper;
    String imgName;
    Uri imgUri;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMyProfileEditBinding myProfileEditBinding = ActivityMyProfileEditBinding.inflate(getLayoutInflater());
        setContentView(myProfileEditBinding.getRoot());
        allocateActivityTitle("Edit Profile");

        editTxtUsername = findViewById(R.id.editTxtUsername);
        editTxtNewPassword = findViewById(R.id.editTxtPassword);
        editTxtHomeNumber = findViewById(R.id.editTxtHomeNumber);
        editTxtStreetName = findViewById(R.id.editTxtStreet);
        editTxtCity = findViewById(R.id.editTxtCity);
        editTxtProvince = findViewById(R.id.editTxtProvince);
        editTxtPhone = findViewById(R.id.editTxtPhone);
        editTxtCurrentPassword = findViewById(R.id.editTxtCurrentPassword);
        Button btnSave = findViewById(R.id.btnSaveChanges);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        imgView = findViewById(R.id.imgViewEditPfp);

        databaseHelper = new DatabaseHelper(this);

        Bundle inBundle = getIntent().getExtras();
        editTxtUsername.setText(inBundle.getString("USERNAME", "New Username"));
        editTxtPhone.setText(inBundle.getString("PHONE"));
        try {
            editTxtHomeNumber.setText(inBundle.getString("HOMENUMBER"));
            editTxtStreetName.setText(inBundle.getString("STREET"));
            editTxtCity.setText(inBundle.getString("CITY"));
            editTxtProvince.setText(inBundle.getString("PROVINCE"));
            imgName = inBundle.getString("IMGNAME");

            if (imgName == null) {
                imgView.setImageResource(R.drawable.defaultpfp);
            } else {
                StorageReference img = storageReference.child("ProfileImg/" + inBundle.getString("IMGNAME"));
                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imgView);
                        imgUri = uri;
                    }
                });
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnCamera.setOnClickListener(view -> askPermission());

        btnGallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });

        btnSave.setOnClickListener(view -> {
            User user = databaseHelper.getUser(editTxtUsername.getText().toString());
            if (editTxtUsername.getText().toString().isEmpty() ||
                    editTxtCurrentPassword.getText().toString().isEmpty() ||
                    editTxtHomeNumber.getText().toString().isEmpty() ||
                    editTxtStreetName.getText().toString().isEmpty() ||
                    editTxtCity.getText().toString().isEmpty() ||
                    editTxtProvince.getText().toString().isEmpty() ||
                    editTxtPhone.getText().toString().isEmpty()) {
                Toast.makeText(MyProfileEditActivity.this, "Please enter all the required fields", Toast.LENGTH_SHORT).show();
            } else if (editTxtPhone.getText().toString().length() != 10) {
                Toast.makeText(MyProfileEditActivity.this, "Please enter a 10-digit phone number", Toast.LENGTH_SHORT).show();
            } else if (user != null){
                Toast.makeText(this, "That username is taken. Try another.", Toast.LENGTH_SHORT).show();
            } else {
                String address = editTxtHomeNumber.getText().toString() + " " +
                        editTxtStreetName.getText().toString() + ", " +
                        editTxtCity.getText().toString() + ", " +
                        editTxtProvince.getText().toString();
                String username = editTxtUsername.getText().toString();

                if (imgName != null) {
                    UploadEditedProfile(imgName,imgUri);
                }
                String storedUsername = StoredDataHelper.get(this, "username");
                user = databaseHelper.getUser(storedUsername);

                if (!BCrypt.checkpw(editTxtCurrentPassword.getText().toString(), user.getPassword())){
                    Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                } else {
                        user.setUsername(username);
                        if (!editTxtNewPassword.getText().toString().isEmpty()){
                            user.setPassword(editTxtNewPassword.getText().toString());
                        } else {
                            user.setPassword(editTxtCurrentPassword.getText().toString());
                        }
                        user.setAddress(address);
                        user.setPhone(editTxtPhone.getText().toString());
                        user.setProfileImg(imgName);

                    databaseHelper.updateUser(user, this);
                    StoredDataHelper.save(this, "username", username);
                    startActivity(new Intent(MyProfileEditActivity.this, MyProfileActivity.class));
                }
            }
        });
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

    private void UploadEditedProfile(String imgName, Uri imgUri){
        StorageReference img = storageReference.child("ProfileImg/" + imgName);
        img.putFile(imgUri);
    }
}