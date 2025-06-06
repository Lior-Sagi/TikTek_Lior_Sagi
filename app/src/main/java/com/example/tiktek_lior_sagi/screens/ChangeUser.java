package com.example.tiktek_lior_sagi.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;


public class ChangeUser extends AppCompatActivity implements View.OnClickListener {

    EditText etUserFnamec, etUserLnamec, etUserPhonec;
    String fname, lname,phone,uid;
    TextView tvUserMailc;
    Button btnSave;
    User userMenu;
    User user=null;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        initViews();
        this.retrieveData();
    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        databaseService= DatabaseService.getInstance();
        etUserFnamec = findViewById(R.id.etUserFnamec);
        etUserLnamec = findViewById(R.id.etUserLnamec);
        tvUserMailc = findViewById(R.id.tvUserMailc);
        etUserPhonec = findViewById(R.id.etUserPhonec);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    public void retrieveData() {
        uid= AuthenticationService.getInstance().getCurrentUserId();
        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<com.example.tiktek_lior_sagi.model.User>() {
            @Override
            public void onCompleted(User object) {
                user=object;
                if(user!=null) {
                    etUserFnamec.setText(user.getFname());
                    etUserLnamec.setText(user.getLname());
                    etUserPhonec.setText(user.getPhone());
                    tvUserMailc.setText(user.getEmail());
                }
            }
            @Override
            public void onFailed(Exception e) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == btnSave) {

            fname=etUserFnamec.getText().toString();
            lname=etUserLnamec.getText().toString();
            phone=etUserPhonec.getText().toString();
            user.setFname(fname);
            user.setLname(lname);
            user.setPhone(phone);
            databaseService.updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(getApplicationContext(), "פרטי המשתמש עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(getApplicationContext(), "חלה שגיאה,פרטי המשתמש לא עודכנו", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(ChangeUser.this, MainActivity.class);
            startActivity(intent);
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