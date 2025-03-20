package com.example.tiktek_lior_sagi.screens;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.ImageAdapter;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.SendBook;
import com.example.tiktek_lior_sagi.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class Answers extends AppCompatActivity {

    ListView lvAnswers;
    private ImageAdapter adapter;
    private DatabaseService databaseService;
    SendBook sendBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_answers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService = DatabaseService.getInstance();

        this.sendBook = getIntent().getSerializableExtra("sendBook", SendBook.class);
        if (this.sendBook == null) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new ImageAdapter(this);

        lvAnswers=findViewById(R.id.lvAnswers);
        lvAnswers.setAdapter(adapter);

        databaseService.getBook(sendBook.getBookId(), new DatabaseService.DatabaseCallback<Book>() {
            @Override
            public void onCompleted(Book book) {
                List<Answer> answers = book.getAnswerListByPage(sendBook.getPage());
                List<String> answersPictures = new ArrayList<>();
                for (Answer answer : answers) {
                    answersPictures.add(answer.getPicAnswer());
                }
                adapter.setItems(answersPictures);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }
}