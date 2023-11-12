package com.example.f23_3175_g4_marketeer;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f23_3175_g4_marketeer.databinding.LayoutChatBinding;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    LayoutChatBinding chatBinding;
    LinearLayout layout;
    ImageView btnSend;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(chatBinding.getRoot());

        layout = findViewById(R.id.layout_chat);
        btnSend = findViewById(R.id.btnSend);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrView);
        String storedUsername = StoredDataHelper.get(this, "username");
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chat-b11e1.firebaseio.com/messages/" +
                storedUsername + "_" + User.receiver);
        reference2 = new Firebase("https://chat-b11e1.firebaseio.com/messages/" +
                User.receiver + "_" + storedUsername);
        btnSend.setOnClickListener(view -> {
            String message = messageArea.getText().toString();
            if (!message.equals("")) {
                Map<String, String> map = new HashMap<>();
                map.put("message", message);
                map.put("user", storedUsername);
                reference1.push().setValue(map);
                reference2.push().setValue(map);
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String username = map.get("user").toString();
                if (username.equals(storedUsername))
                    addMessageBox("You: " + message, 1);
                else
                    addMessageBox(User.receiver.toLowerCase() + ": " + message, 2);
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

    public void addMessageBox(String message, int type) {
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
}