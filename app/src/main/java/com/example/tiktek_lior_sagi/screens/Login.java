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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail, etPassword;
    Button btnLog;
    String email, pass;
    DatabaseService databaseService;
    private User user2=null;
    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        databaseService=DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();
        user2= SharedPreferencesUtil.getUser(this);
        if(user2!=null){
            etEmail.setText( user2.getEmail());
           etPassword.setText(user2.getPassword());
        }
    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        etEmail= findViewById(R.id.etEmailLogin);
        etPassword= findViewById(R.id.etPasswordLogin);
        btnLog = findViewById(R.id.btnLogin);
        btnLog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        email = etEmail.getText().toString();
        pass = etPassword.getText().toString();
        AuthenticationService.getInstance().signIn(email, pass,
                new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String userId) {
                databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User user) {
                        if(user!=null) {
                            SharedPreferencesUtil.saveUser(Login.this, user);
                            if (user.getAdmin()) {
                                Intent go = new Intent(getApplicationContext(), AdminPage.class);
                                startActivity(go);
                            } else {
                                Intent go = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(go);
                            }
                        }
                    }
                    @Override
                    public void onFailed(Exception e) {
                        Log.w("TAG", "signInWithEmail:failure", e);
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailed(Exception e) {
                Log.w("TAG", "signInWithEmail:failure", e);
                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //inflate the menu
    //if user logged in is not an admin remove all of the admin menu buttons
    public boolean onCreateOptionsMenu(Menu menu) {
        if(authenticationService.isUserSignedIn()) {
            getMenuInflater().inflate(R.menu.menu, menu);
            user2 = SharedPreferencesUtil.getUser(this);
            if (!user2.getAdmin()) {
                menu.removeGroup(R.id.adminMenu);
            }
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