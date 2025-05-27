package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.BookAdapter;
import com.example.tiktek_lior_sagi.adapter.BookSpinnerAdapter;
import com.example.tiktek_lior_sagi.adapter.SendBookAdapter;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.SendBook;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener {
    Spinner spSubject, spBook, spPages, spQuestion;
    String subject;
    Button btnSearch;
    /// tag for logging
    private static final String TAG = "SearchBook";
    private DatabaseService databaseService;
    private ArrayList<Book> allBooks = new ArrayList<>();
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    BookSpinnerAdapter bookSpinnerAdapter;
    ArrayAdapter<String> bookPagesAdapter;
    Book book2=null;
    private FirebaseAuth mAuth;
    private User user=null;
    private String uid;
    RecyclerView recentSearchesRecyclerView;
    ListView lvUserSearch;
    ArrayList<SendBook> sendBooks;
    ArrayList<String> sendBooksIds;
    ArrayList<Book> userBooks =new ArrayList<Book>();
    ArrayAdapter<String> adapter;
    SendBookAdapter sendBookAdapter;
    ArrayList<String> userBooksString =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null)
        {
            Toast.makeText(getApplicationContext(), "התחבר למשתמש בשביל לגשת לדף", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        uid=mAuth.getCurrentUser().getUid();
        adapter=new ArrayAdapter<>(Search.this,android.R.layout.simple_list_item_1,userBooksString);
        sendBookAdapter = new SendBookAdapter(sendBooks,sendBooksIds,this);
        recentSearchesRecyclerView.setAdapter(sendBookAdapter);
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
        allBooks = new ArrayList<>();
        loadUserSearches(uid);
        /*databaseService.getUserSearches(uid, new DatabaseService.DatabaseCallback<List<Book>>() {

            @Override
            public void onCompleted(List<Book> object) {
                userBooks.addAll(object);
            }
            @Override
           public void onFailed(Exception e) {

            }
        });*/



        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = (String) parent.getItemAtPosition(position);
                databaseService.getBooks(new DatabaseService.DatabaseCallback<List<Book>>() {
                    @Override
                    public void onCompleted(List<Book> object) {
                        allBooks.clear();
                        allBooks.addAll(object);

                        // Initially filter books based on the first selected subject
                        selectedBooks.clear();
                        for (Book book : allBooks) {
                            if (book.getSubject().contains(subject)) {
                                selectedBooks.add(book);
                            }
                            bookSpinnerAdapter = new BookSpinnerAdapter(Search.this, android.R.layout.simple_spinner_item, selectedBooks);

                            // Notify adapter instead of creating a new one
                            spBook.setAdapter(bookSpinnerAdapter);
                            bookSpinnerAdapter.notifyDataSetChanged();
                            spBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Book book = (Book) parent.getItemAtPosition(position);
                                    book2 = book;
                                    String[] bookPages = new String[book.getMaxPages()];
                                    for (int i = 0; i < bookPages.length; i++) {
                                        bookPages[i] = ((i+1) + "");
                                    }
                                    bookPagesAdapter = new ArrayAdapter<>(Search.this, android.R.layout.simple_spinner_item, bookPages);
                                    spPages.setAdapter(bookPagesAdapter);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailed(Exception e) {

                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadUserSearches(String userId) {
        DatabaseReference userSearchesRef = FirebaseDatabase.getInstance().getReference("userBooks").child(userId);
        userSearchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Search", "DataSnapshot: " + snapshot.toString());

                for (DataSnapshot bookSnap : snapshot.getChildren()) {
                    SendBook sendBook = bookSnap.getValue(SendBook.class);
                    if (sendBook != null) {
                        Log.d("Search", "Loaded User Search: " + sendBook.getBookId() + " (" + sendBook.getBookName() + ")"+ " (" + sendBook.getQuestionNumber() + ")"+ " (" + sendBook.getPage() + ")");
                        String uid = bookSnap.getKey();
                        sendBooks.add(sendBook);
                        sendBooksIds.add(uid);
                    }
                }
                sendBookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Search.this, "Failed to load userSearches", Toast.LENGTH_SHORT).show();
                Log.e("Search", "Database error: " + error.getMessage());
            }
        });
    }
    private void saveUserSearches(SendBook sendBook) {
        databaseService.userSearches(uid, sendBook, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {

                Log.d("saveUserSearches", "User search saved successfully.");
            }

            @Override
            public void onFailed(Exception e) {

                Log.e("saveUserSearches", "Failed to save user search", e);
            }
        });
    }

    /*private void saveUserSearches(Book book) {
        databaseService.getUserSearches(uid, new DatabaseService.DatabaseCallback<List<Book>>() {
            @Override
            public void onCompleted(List<Book> object) {
                userBooks.addAll(object);
                //lvUserSearch.setAdapter(adapter);
            }
            @Override
            public void onFailed(Exception e) {

            }
        });

    }*/

    private void initViews () {
            spSubject = findViewById(R.id.spSubject);
            spBook = findViewById(R.id.spBook);
            spPages = findViewById(R.id.spPages);
            spQuestion = findViewById(R.id.spQuestion);
            btnSearch = findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(this);
            recentSearchesRecyclerView = findViewById(R.id.recentSearchesRecyclerView);
            //lvUserSearch=findViewById(R.id.lvUserBooks);
        }
    @Override
    public void onClick(View v) {
        SendBook sendBook= new SendBook(book2.getId(), book2.getBookName(), Integer.parseInt(spPages.getSelectedItem().toString()), spQuestion.getSelectedItem().toString());
        saveUserSearches(sendBook);
        Intent go=new Intent(getApplicationContext(), Answers.class);
        go.putExtra("sendBook",sendBook);
        startActivity(go);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        user= SharedPreferencesUtil.getUser(this);
        if(!user.getAdmin()){
            menu.removeGroup(R.id.adminMenu);
        }
        return true;
    }


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
        else if (id == R.id.menuLandingPage) {
            Intent go = new Intent(getApplicationContext(), LandingPage.class);
            startActivity(go);
        }
        else if (id == R.id.menuAddAnswer) {
            Intent go = new Intent(getApplicationContext(), AddAnswer.class);
            startActivity(go);
        }
        else if (id == R.id.menuLogOut) {
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
        return true;
    }
}
