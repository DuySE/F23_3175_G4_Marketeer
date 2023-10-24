package com.example.f23_3175_g4_marketeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        usersList = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);

        String url = "https://chat-b11e1.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> doOnSuccess(response), error -> System.out.println("" + error));
        RequestQueue queue = Volley.newRequestQueue(Users.this);
        queue.add(request);
        usersList.setOnItemClickListener((adapterView, view, i, l) -> {
            UserDetail.receiver = al.get(i);
            startActivity(new Intent(Users.this, ChatActivity.class));
        });
    }

    private void doOnSuccess(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            Iterator i = obj.keys();
            String key = "";
            while (i.hasNext()) {
                key = i.next().toString();
                if (!key.equals(UserDetail.username)) {
                    al.add(key);
                }
                totalUsers++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (totalUsers <= 0) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }
    }
}