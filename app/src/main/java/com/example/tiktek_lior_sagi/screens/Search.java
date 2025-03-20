package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.BookSpinnerAdapter;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.SendBook;
import com.example.tiktek_lior_sagi.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener {
    Spinner spSubject, spBook, spPages, spQuestion;
    String subject;
    DatabaseService.DatabaseCallback<List<Book>> bookList;
    Button btnSearch;
    /// tag for logging
    private static final String TAG = "SearchBook";
    private DatabaseService databaseService;
    private ArrayList<Book> allBooks = new ArrayList<>();
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    BookSpinnerAdapter bookSpinnerAdapter;
    private Spinner spbookSpinner;
    ArrayAdapter<String> bookPagesAdapter;
    Book book2=null;

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

                            bookSpinnerAdapter = new BookSpinnerAdapter(Search.this, android.R.layout.simple_spinner_item, selectedBooks);

                            // Notify adapter instead of creating a new one

                            spBook.setAdapter(bookSpinnerAdapter);
                            bookSpinnerAdapter.notifyDataSetChanged();
                            spBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Book book = (Book) parent.getItemAtPosition(position);
                                    book2 = book;

                                    String[] bookPages = new String[book.getPagesList().size()];
                                    for (int i = 0; i < bookPages.length; i++) {
                                        bookPages[i] = (i + "");
                                    }
                                    bookPagesAdapter = new ArrayAdapter<>(Search.this, android.R.layout.simple_spinner_item, bookPages);
                                    spPages.setAdapter(bookPagesAdapter);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //   book = null;

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
        private void initViews () {
            spSubject = findViewById(R.id.spSubject);
            spBook = findViewById(R.id.spBook);
            spPages = findViewById(R.id.spPages);
            spQuestion = findViewById(R.id.spQuestion);
            btnSearch = findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        SendBook sendBook= new SendBook(book2.getId(), book2.getBookName(), Integer.parseInt(spPages.getSelectedItem().toString()), Integer.parseInt(spQuestion.getSelectedItem().toString()));
        Intent go=new Intent(getApplicationContext(), Answers.class);
        go.putExtra("sendBook",sendBook);
        startActivity(go);
    }
}
