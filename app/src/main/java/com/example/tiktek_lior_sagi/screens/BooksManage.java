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
import com.example.tiktek_lior_sagi.adapter.BookAdapter;
import com.example.tiktek_lior_sagi.adapter.UserAdapter;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BooksManage extends AppCompatActivity {
    RecyclerView bookRecyclerView;
    List<Book> bookList = new ArrayList<>();
    List<String> bookIds = new ArrayList<>();
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bookRecyclerView=findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// Initialize adapter and attach to RecyclerView BEFORE loading users
        adapter = new BookAdapter(bookList, bookIds, this);
        bookRecyclerView.setAdapter(adapter);
        loadBooks();
    }

    private void loadBooks() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("books");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("UsersManage", "DataSnapshot: " + snapshot.toString());
                bookList.clear();
                bookIds.clear();

                for (DataSnapshot bookSnap : snapshot.getChildren()) {
                    Book book = bookSnap.getValue(Book.class);
                    Log.d("BooksManage", "Loaded Books: " + book.getBookName() + " (" + book.getId() + ")");
                    String uid = bookSnap.getKey();
                    bookList.add(book);
                    bookIds.add(uid);
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BooksManage.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}