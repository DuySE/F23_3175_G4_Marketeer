package com.example.f23_3175_g4_marketeer;

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
import com.example.f23_3175_g4_marketeer.databinding.ActivityUsersBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserListActivity extends DrawerActivity {
    ActivityUsersBinding usersBinding;

    ListView usersList;
    TextView noUsersText;
    List<String> al = new ArrayList<>();
    int totalUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersBinding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(usersBinding.getRoot());
        allocateActivityTitle("Messages");

        usersList = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);

        String url = "https://chat-b11e1.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                this::doOnSuccess, error -> System.out.println("" + error));
        RequestQueue queue = Volley.newRequestQueue(UserListActivity.this);
        queue.add(request);
        usersList.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putString("SELLER", al.get(i));
            Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void doOnSuccess(String response) {
        String username = StoredDataHelper.get(this, "username");
        try {
            JSONObject obj = new JSONObject(response);
            Iterator<String> i = obj.keys();
            String key;
            while (i.hasNext()) {
                key = i.next();
                if (key.equals(username)) {
                    JSONObject userObj = new JSONObject(obj.get(key).toString());
                    i = userObj.keys();
                    while (i.hasNext()) {
                        key = i.next();
                        al.add(key);
                    }
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
            usersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al));
        }
    }
}