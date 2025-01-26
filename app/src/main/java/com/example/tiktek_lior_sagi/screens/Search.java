package com.example.tiktek_lior_sagi.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;


import java.util.ArrayList;

public class Search extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Book> subjects= new ArrayList<Book>();
    Spinner spSubject;
    Spinner spBook;
    Spinner spPage;
    Spinner spQuestion;
    Button btnSearch;
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
        spSubject=findViewById(R.id.spSubject);
        spBook=findViewById(R.id.spBook);
        spPage=findViewById(R.id.spPage);
        spQuestion=findViewById(R.id.spQuestion);
        btnSearch=findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}