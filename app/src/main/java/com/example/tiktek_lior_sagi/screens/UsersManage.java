package com.example.tiktek_lior_sagi.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.UserAdapter;
import com.example.tiktek_lior_sagi.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersManage extends AppCompatActivity {

    RecyclerView userRecyclerView;
    List<User> userList = new ArrayList<>();
    List<String> userIds = new ArrayList<>();
    UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRecyclerView=findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// Initialize adapter and attach to RecyclerView BEFORE loading users
        adapter = new UserAdapter(this, userList, userIds);
        userRecyclerView.setAdapter(adapter);
        loadUsers();
    }

    private void loadUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("UsersManage", "DataSnapshot: " + snapshot.toString());
                userList.clear();
                userIds.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    Log.d("UsersManage", "Loaded user: " + user.getFname() + " (" + user.getEmail() + ")");
                    String uid = userSnap.getKey();
                    userList.add(user);
                    userIds.add(uid);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UsersManage.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}