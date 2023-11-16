package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.f23_3175_g4_marketeer.databinding.ActivityMainBinding;
import com.example.f23_3175_g4_marketeer.databinding.ActivityUsersBinding;
import com.example.f23_3175_g4_marketeer.databinding.LayoutChatBinding;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends DrawerActivity {
    LayoutChatBinding chatBinding;
    LinearLayout layout;
    ImageView btnSend;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2, usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String seller = bundle.get("SELLER").toString();
        chatBinding = LayoutChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        allocateActivityTitle(seller);
        layout = findViewById(R.id.layout_chat);
        btnSend = findViewById(R.id.btnSend);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrView);
        String storedUsername = StoredDataHelper.get(this, "username");
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chat-b11e1.firebaseio.com/messages/" +
                storedUsername + "_" + seller);
        reference2 = new Firebase("https://chat-b11e1.firebaseio.com/messages/" +
                seller + "_" + storedUsername);
        usersList = new Firebase("https://chat-b11e1.firebaseio.com/users/");
        btnSend.setOnClickListener(view -> {
            String message = messageArea.getText().toString();
            if (!message.isEmpty()) {
                Map<String, String> map = new HashMap<>();
                map.put("message", message);
                map.put("user", storedUsername);
                reference1.push().setValue(map);
                reference2.push().setValue(map);
                usersList.child(storedUsername).child(seller).setValue("");
                usersList.child(seller).child(storedUsername).setValue("");
            }
            addNotification();
            messageArea.setText("");
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                Object message = map.get("message");
                Object username = map.get("user");
                if (username != null && username.equals(storedUsername))
                    addMessageBox("You: " + message, 1);
                else
                    addMessageBox(seller.toLowerCase() + ": " + message, 2);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 15, 15, 15);
        textView.setLayoutParams(lp);
        if (type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void addNotification() {
        String id = getString(R.string.channel_id);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(id,
                    name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Marketeer")
                .setContentText("New message from " + User.receiver)
                .setAutoCancel(true);
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());
    }
}