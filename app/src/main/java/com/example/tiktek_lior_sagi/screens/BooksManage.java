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
import com.example.tiktek_lior_sagi.adapter.UserAdapter;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
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
    private User user;

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

        // Initialize adapter and attach to RecyclerView BEFORE loading books
        adapter = new BookAdapter(bookList, bookIds, this);
        bookRecyclerView.setAdapter(adapter);
        loadBooks();
    }

    private void loadBooks() {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("BooksManage", "DataSnapshot: " + snapshot.toString());
                bookList.clear();
                bookIds.clear();

                for (DataSnapshot bookSnap : snapshot.getChildren()) {
                    Book book = bookSnap.getValue(Book.class);
                    if (book != null) {
                        Log.d("BooksManage", "Loaded Book: " + book.getBookName() + " (" + book.getId() + ")");
                        String uid = bookSnap.getKey();
                        bookList.add(book);
                        bookIds.add(uid);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BooksManage.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                Log.e("BooksManage", "Database error: " + error.getMessage());
            }
        });
    }

    // Method to safely remove book from lists (call this from your adapter)
    public void removeBook(int position) {
        if (position >= 0 && position < bookList.size() && position < bookIds.size()) {
            bookList.remove(position);
            bookIds.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, bookList.size());
        } else {
            Log.e("BooksManage", "Invalid position for removal: " + position + ", list size: " + bookList.size());
            Toast.makeText(this, "Error removing book", Toast.LENGTH_SHORT).show();
        }
    }
    //inflate the menu
    //if user logged in is not an admin remove all of the admin menu buttons
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        user = SharedPreferencesUtil.getUser(this);
        if (!user.getAdmin()) {
            menu.removeGroup(R.id.adminMenu);
        }
        return true;
    }

    //get the id of the item clicked and sends to the according page
    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMain) {
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        else if (id == R.id.menuUserGuide) {
            Intent go = new Intent(getApplicationContext(), UserGuide.class);
            startActivity(go);
        }
        else if (id == R.id.menuAddAnswer) {
            Intent go = new Intent(getApplicationContext(), AddAnswer.class);
            startActivity(go);
        }
        else if (id == R.id.menuLogOut) {
            //signs out the user and returns them to landing page
            AuthenticationService.getInstance().signOut();
            Intent go = new Intent(getApplicationContext(), LandingPage.class);
            startActivity(go);
        }
        else if (id == R.id.menuSearchAnswer) {
            Intent go = new Intent(getApplicationContext(), Search.class);
            startActivity(go);
        }
        else if (id == R.id.menuAdminAdminPage) {
            Intent go = new Intent(getApplicationContext(), AdminPage.class);
            startActivity(go);
        }
        else if (id == R.id.menuAdminAddBook) {
            Intent go = new Intent(getApplicationContext(), AddBook.class);
            startActivity(go);
        }
        else if (id == R.id.menuAdminManageUsers) {
            Intent go = new Intent(getApplicationContext(), UsersManage.class);
            startActivity(go);
        }
        else if (id == R.id.menuAdminManageBooks) {
            Intent go = new Intent(getApplicationContext(), BooksManage.class);
            startActivity(go);
        }
        else if (id == R.id.menuAdminManageAnswers) {
            Intent go = new Intent(getApplicationContext(), AnswersManage.class);
            startActivity(go);
        }
        else if (id == R.id.menuUserProfile) {
            Intent go = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(go);
        }
        return true;
    }
}