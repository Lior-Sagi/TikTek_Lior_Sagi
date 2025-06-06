package com.example.tiktek_lior_sagi.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.AnswerAdapter;
import com.example.tiktek_lior_sagi.adapter.BookAdapter;
import com.example.tiktek_lior_sagi.adapter.BookSpinnerAdapter;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnswersManage extends AppCompatActivity implements View.OnClickListener {
    Spinner spSubject;
    static Spinner spBook;
    Spinner spPages;
    Spinner spQuestion;
    String subject;
    Button btnSearch;
    ListView lvAnswers;
    private User user;
    private DatabaseService databaseService;
    private ArrayList<Book> allBooks = new ArrayList<>();
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    BookSpinnerAdapter bookSpinnerAdapter;
    ArrayAdapter<String> bookPagesAdapter;
    RecyclerView answerRecyclerView;
    Book book=null;

    List<Answer> answerList = new ArrayList<>();
    List<String> answerIds = new ArrayList<>();
    AnswerAdapter adapter;

    private String quNumber;
    private int intQuNumber;
    private int pageNumber;

    public  static  String bookIdForChangeAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_answers_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        answerRecyclerView.setAdapter(null);
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
        allBooks = new ArrayList<>();

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
                            bookSpinnerAdapter = new BookSpinnerAdapter(AnswersManage.this, android.R.layout.simple_spinner_item, selectedBooks);

                            // Notify adapter instead of creating a new one
                            spBook.setAdapter(bookSpinnerAdapter);
                            bookSpinnerAdapter.notifyDataSetChanged();
                            spBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Book book = (Book) parent.getItemAtPosition(position);
                                    bookIdForChangeAnswer=book.getId();

                                    String[] bookPages = new String[book.getMaxPages()];
                                    for (int i = 0; i < bookPages.length; i++) {
                                        bookPages[i] = ((i+1) + "");
                                    }
                                    bookPagesAdapter = new ArrayAdapter<>(AnswersManage.this, android.R.layout.simple_spinner_item, bookPages);
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
    private void loadAnswers(String bookId) {
        DatabaseReference pagesRef = FirebaseDatabase.getInstance()
                .getReference("books")
                .child(bookId)
                .child("pagesList");

        pagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("AnswersManage", "DataSnapshot for book " + bookId + ": " + snapshot.toString());
                answerList.clear();
                answerIds.clear();

                for (DataSnapshot pageSnap : snapshot.getChildren()) {
                    for (DataSnapshot answerSnap : pageSnap.getChildren()) {
                        try {
                            Answer answer = answerSnap.getValue(Answer.class);
                            if (answer != null) {
                                String pageKey = pageSnap.getKey();
                                if (pageKey != null && answer.getPage() == 0) {
                                    try {
                                        int pageNum = Integer.parseInt(pageKey);
                                        answer.setPage(pageNum);
                                    } catch (NumberFormatException e) {
                                        Log.e("AnswersManage", "Invalid page number: " + pageKey, e);
                                        answer.setPage(0);
                                    }
                                }

                                Log.d("AnswersManage", "Loaded Answer: Page=" + answer.getPage() +
                                        ", Question=" + answer.getQuestionNumber() +
                                        ", ID=" + answer.getId() +
                                        ", Filtering for Page=" + pageNumber +
                                        ", Question=" + intQuNumber);

                                // Apply filters
                                boolean pageMatches = (answer.getPage() == pageNumber);
                                boolean questionMatches = (intQuNumber == 0) || (answer.getQuestionNumber() == intQuNumber);

                                if (pageMatches && questionMatches) {
                                    answerList.add(answer);
                                    answerIds.add(answerSnap.getKey());
                                    Log.d("AnswersManage", "Answer added to list: " + answer.getId());
                                } else {
                                    Log.d("AnswersManage", "Answer filtered out: Page match=" + pageMatches +
                                            ", Question match=" + questionMatches);
                                }
                            }
                        } catch (DatabaseException e) {
                            Log.e("AnswersManage", "Error deserializing answer: " + e.getMessage(), e);
                        }
                    }
                }
                Log.d("AnswersManage", "Total answers loaded: " + answerList.size());
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnswersManage.this, "Failed to load answers", Toast.LENGTH_SHORT).show();
                Log.e("AnswersManage", "Firebase error: " + error.getMessage(), error.toException());
            }
        });
    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        spSubject=findViewById(R.id.spSubject);
        spBook=findViewById(R.id.spBook);
        spPages=findViewById(R.id.spPages);
        spQuestion=findViewById(R.id.spQuestion);
        btnSearch=findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        lvAnswers=findViewById(R.id.lvAnswers);
        answerRecyclerView=findViewById(R.id.answerRecyclerView);
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

    @Override
    public void onClick(View view) {
        Book selectedBook = (Book) spBook.getSelectedItem();
        if (selectedBook == null) {
            Toast.makeText(this, "Please select a book", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get selected page from spinner
        String selectedPageStr = (String) spPages.getSelectedItem();
        if (selectedPageStr == null) {
            Toast.makeText(this, "Please select a page", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            pageNumber = Integer.parseInt(selectedPageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid page number", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get selected question
        quNumber = spQuestion.getSelectedItem().toString();
        if (quNumber.equals("כל העמוד")) {
            intQuNumber = 0;
        } else {
            try {
                intQuNumber = Integer.parseInt(quNumber);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid question number", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Log.d("AnswersManage", "Searching for: Book=" + selectedBook.getId() +
                ", Page=" + pageNumber + ", Question=" + intQuNumber);
        // Set up RecyclerView
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AnswerAdapter(selectedBook.getId(), answerList, answerIds, this);
        answerRecyclerView.setAdapter(adapter);
        // Load answers with the correct bookId
        loadAnswers(selectedBook.getId()); // Use selectedBook.getId() instead of bookIdForChangeAnswer
    }
}