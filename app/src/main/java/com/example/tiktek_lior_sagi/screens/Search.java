package com.example.tiktek_lior_sagi.screens;

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
import com.example.tiktek_lior_sagi.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spSubject, spBook, spPage, spQuestion;
    String subject;
    //List<Book> bookList=new ArrayList<>();
    DatabaseService.DatabaseCallback<List<Book>> bookList;
    Button btnSearch;
    /// tag for logging
    private static final String TAG = "SearchBook";
    private BookSpinnerAdapter bookSpinnerAdapter;
    //  private BooksAdapter booksAdapter;
    private List<Book> selectedBooks;
    List<Book> allBooks;
    private DatabaseService databaseService;
    private ArrayList<Book> serchSubject;
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
        bookSpinnerAdapter = new BookSpinnerAdapter(Search.this, android.R.layout.simple_spinner_item, allBooks);
        //   bookSpinner.setAdapter(bookSpinnerAdapter);
        /// get all the books from the database
        databaseService.getBooks(new DatabaseService.DatabaseCallback<List<Book>>() {
            @Override
            public void onCompleted(List<Book> object) {
                Log.d(TAG, "onCompleted: " + object);
                allBooks.clear();
                allBooks.addAll(object);
                /// notify the adapter that the data has changed
                /// this specifies that the data has changed
                /// and the adapter should update the view
                /// @see BookSpinnerAdapter#notifyDataSetChanged()
                // bookSpinnerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }
        });
    }
    private void initViews() {
        spSubject = findViewById(R.id.spSubject);
        spSubject.setOnItemSelectedListener(this);
        spBook = findViewById(R.id.spBook);
        spPage = findViewById(R.id.spPage);
        spQuestion = findViewById(R.id.spQuestion);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subject = (String) parent.getItemAtPosition(position);
        //   eSearch1 = etSearch1.getText().toString() + "";
        for (int i = 0; i < allBooks.size(); i++) {
            Book current1 = allBooks.get(i);
            if (current1.getSubject().equals(subject)) {
                selectedBooks.add(current1);
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}