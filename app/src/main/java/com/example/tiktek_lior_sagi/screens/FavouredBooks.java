package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.example.tiktek_lior_sagi.adapter.BookAdapter;
import com.example.tiktek_lior_sagi.model.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouredBooks extends AppCompatActivity {
    RecyclerView favouredBooksRecyclerView;
    List<Book> bookList = new ArrayList<>();
    List<String> bookIds = new ArrayList<>();
    FavouredBooks adapter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoured_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth= FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null)
        {
            Toast.makeText(getApplicationContext(), "התחבר למשתמש בשביל לגשת לדף", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        favouredBooksRecyclerView=findViewById(R.id.favouredBooksRecyclerView);
        favouredBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// Initialize adapter and attach to RecyclerView BEFORE loading books
        adapter = new FavouredBooks(bookList, bookIds, this);
        favouredBooksRecyclerView.setAdapter(adapter);
        loadBooks();
    }

    private void loadBooks() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("books");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FavouredBooks", "DataSnapshot: " + snapshot.toString());
                bookList.clear();
                bookIds.clear();

                for (DataSnapshot bookSnap : snapshot.getChildren()) {
                    Book book = bookSnap.getValue(Book.class);
                    Log.d("FavouredBooks", "Loaded Books: " + book.getBookName() + " (" + book.getId() + ")");
                    String uid = bookSnap.getKey();
                    bookList.add(book);
                    bookIds.add(uid);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavouredBooks.this, "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMain) {
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        else if (id == R.id.menuAddAnswer) {
            Intent go = new Intent(getApplicationContext(), AddAnswer.class);
            startActivity(go);
        }
        else if (id == R.id.menuLogOut) {
            mAuth.signOut();
        }
        else if (id == R.id.menuSearchAnswer) {
            Intent go = new Intent(getApplicationContext(), Search.class);
            startActivity(go);
        }
        return true;
    }
}