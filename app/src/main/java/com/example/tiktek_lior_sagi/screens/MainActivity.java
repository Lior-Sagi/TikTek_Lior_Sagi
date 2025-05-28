package com.example.tiktek_lior_sagi.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.tiktek_lior_sagi.model.User;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnToAddAnswer,btnToSearch;
    TextView tvAnswerCounter,tvBookCounter;
    User user=null;
    private FirebaseAuth mAuth;

    private DatabaseService databaseService;

    ArrayList<Book> books=new ArrayList<>();
    private int numBooks=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
        mAuth=FirebaseAuth.getInstance();
        databaseService=DatabaseService.getInstance();


        databaseService.getBooks(new DatabaseService.DatabaseCallback<List<Book>>() {

            @Override
            public void onCompleted(List<Book> object) {
                numBooks=object.size();
                tvBookCounter.setText(numBooks+"");
            }
            @Override
            public void onFailed(Exception e) {

            }
        });
        countTotalAnswers();
    }

    private void countTotalAnswers() {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");

        booksRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    int totalAnswers = 0;
                    DataSnapshot booksSnapshot = task.getResult();

                    // Iterate through all books
                    for (DataSnapshot bookSnapshot : booksSnapshot.getChildren()) {
                        // Check if this book has a pagesList
                        DataSnapshot pagesListSnapshot = bookSnapshot.child("pagesList");

                        if (pagesListSnapshot.exists()) {
                            // Iterate through all pages in this book
                            for (DataSnapshot pageSnapshot : pagesListSnapshot.getChildren()) {
                                // Count answers in this page
                                totalAnswers += (int) pageSnapshot.getChildrenCount();
                            }
                        }
                    }
                    // Update the UI with the total count
                    tvAnswerCounter.setText(String.valueOf(totalAnswers));
                } else {
                    Log.e("MainActivity", "Error getting answers count", task.getException());
                    Toast.makeText(MainActivity.this, "Error loading answers count", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initviews()
    {

        btnToAddAnswer=findViewById(R.id.btnToAddAnswer);
        btnToAddAnswer.setOnClickListener(this);

        btnToSearch=findViewById(R.id.btnToSearch);
        btnToSearch.setOnClickListener(this);

        tvAnswerCounter=findViewById(R.id.tvAnswerCounter);
        tvBookCounter=findViewById(R.id.tvBookCounter);

    }

    @Override
    public void onClick(View view) {
        if(view==btnToAddAnswer)
        {
            Intent go = new Intent(getApplicationContext(), AddAnswer.class);
            startActivity(go);
        }
        if(view==btnToSearch)
        {
            Intent go = new Intent(getApplicationContext(), Search.class);
            startActivity(go);
        }
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
        else if (id == R.id.menuUserProfile) {
            Intent go = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(go);
        }
        return true;
    }
}




