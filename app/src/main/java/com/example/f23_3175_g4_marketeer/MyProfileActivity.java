package com.example.f23_3175_g4_marketeer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.f23_3175_g4_marketeer.databinding.ActivityMyProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class MyProfileActivity extends DrawerActivity {
    ActivityMyProfileBinding myProfileBinding;
    TextView txtViewUsername;
    TextView txtViewPhone;
    TextView txtViewAddress;
    Button btnEditProfile;
    ImageView imgView;
    String imgName;
    User user;
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myProfileBinding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(myProfileBinding.getRoot());
        allocateActivityTitle("My Profile");

        txtViewUsername = findViewById(R.id.txtViewUsername);
        txtViewPhone = findViewById(R.id.txtViewPhone);
        txtViewAddress = findViewById(R.id.txtViewAddress);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        imgView = findViewById(R.id.imgViewPfp);
        setProfile();
      
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME", txtViewUsername.getText().toString());
                bundle.putString("PASSWORD", password);
                if (txtViewPhone.getText().toString().length() == 10) {
                    bundle.putString("PHONE", txtViewPhone.getText().toString());
                }

                String address = txtViewAddress.getText().toString();
                try {
                    String city = address.split(", ")[1];
                    String province = address.split(", ")[2];
                    String numberAndStreet = address.split(", ")[0];
                    String homeNumber = address.split(" ")[0];

                    String[] streetNames = numberAndStreet.split(" ");
                    String streetName = "";
                    for (int i = 1; i < streetNames.length; i++) {
                        streetName += streetNames[i] + " ";
                    }
                    bundle.putString("HOMENUMBER", homeNumber);
                    bundle.putString("STREET", streetName);
                    bundle.putString("CITY", city);
                    bundle.putString("PROVINCE", province);
                    bundle.putString("IMGNAME", imgName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
               
                Intent intent = new Intent(MyProfileActivity.this, MyProfileEditActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } 
        });
    }

    public void setProfile() {
        String hiddenPassword = "";
        try {
            DatabaseHelper db = new DatabaseHelper(this);
            user = db.getUser(StoredDataHelper.get(this, "username"));
            txtViewUsername.setText(user.getUsername());
            password = user.getPassword();
            txtViewAddress.setText(user.getAddress());
            txtViewPhone.setText(user.getPhone());
            imgName = user.getProfileImg();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    StorageReference img = storageReference.child("ProfileImg/" + imgName);
                    img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(imgView);
                        }
                    });
                }
            };

            Timer timer = new Timer();
            timer.schedule(timerTask,1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}