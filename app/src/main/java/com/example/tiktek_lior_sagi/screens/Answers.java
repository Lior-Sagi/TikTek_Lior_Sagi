package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.ImageAdapter;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.SendBook;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.DataHolder;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Answers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static String TAG = "Answers";

    ListView lvAnswers;
    private ImageAdapter adapter;
    private DatabaseService databaseService;
    SendBook sendBook;
    private FirebaseAuth mAuth;
    private User user;

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
        initViews();
        databaseService = DatabaseService.getInstance();
        mAuth=FirebaseAuth.getInstance();
        this.sendBook = getIntent().getSerializableExtra("sendBook", SendBook.class);
        if (this.sendBook == null) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new ImageAdapter(this);
        databaseService.getBook(sendBook.getBookId(), new DatabaseService.DatabaseCallback<Book>() {
            @Override
            public void onCompleted(Book book) {
                Log.d(TAG, "book: " + book.toString());
                Log.d(TAG, "sendBook: " + sendBook.toString());
                if (!sendBook.getQuestionNumber().equals("כל העמוד")){
                    List<Answer> answers = book.getAnswerListByQuestionNumber( sendBook.getPage(), Integer.parseInt(sendBook.getQuestionNumber()));
                    List<String> answersPictures = new ArrayList<>();
                    for (Answer answer : answers) {
                        answersPictures.add(answer.getPicAnswer());
                    }
                    Log.d(TAG, "answers: " + answers);
                    Log.d(TAG, "answersPictures: " + answersPictures);
                    adapter.setItems(answersPictures);
                }
                else {
                    List<Answer> answers = book.getAnswerListByPage( sendBook.getPage());
                    List<String> answersPictures = new ArrayList<>();
                    for (Answer answer : answers) {
                        answersPictures.add(answer.getPicAnswer());
                    }
                    Log.d(TAG, "answers: " + answers);
                    Log.d(TAG, "answersPictures: " + answersPictures);
                    adapter.setItems(answersPictures);
                }
            }
            @Override
            public void onFailed(Exception e) {

            }
        });
    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        lvAnswers=findViewById(R.id.lvAnswers);
        lvAnswers.setOnItemClickListener(this);
        lvAnswers.setAdapter(adapter);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String base64Image = (String) adapter.getItem(position);
        DataHolder.base64Image = base64Image;
        Intent go = new Intent(getApplicationContext(), OneAnswer.class);
        startActivity(go);
    }
}