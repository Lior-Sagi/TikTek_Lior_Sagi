package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPage extends AppCompatActivity implements View.OnClickListener {

    Button btnToMainActivity,btnToAddBook,btnToUsersManage,btnToBooksManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
    }

    private void initviews() {
        btnToMainActivity=findViewById(R.id.btnToMainActivity);
        btnToMainActivity.setOnClickListener(this);

        btnToAddBook=findViewById(R.id.btnToAddBook);
        btnToAddBook.setOnClickListener(this);

        btnToUsersManage=findViewById(R.id.btnToUsersManage);
        btnToUsersManage.setOnClickListener(this);

        btnToBooksManage=findViewById(R.id.btnToBooksManage);
        btnToBooksManage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnToMainActivity)
        {
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        if(view==btnToAddBook)
        {
            Intent go = new Intent(getApplicationContext(), AddBook.class);
            startActivity(go);
        }
        if(view==btnToUsersManage)
        {
            Intent go = new Intent(getApplicationContext(), UsersManage.class);
            startActivity(go);
        }
        if(view==btnToBooksManage)
        {
            Intent go = new Intent(getApplicationContext(), BooksManage.class);
            startActivity(go);
        }
    }
}

