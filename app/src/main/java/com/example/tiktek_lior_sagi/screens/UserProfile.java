package com.example.tiktek_lior_sagi.screens;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    TextView tvFname, tvLname, tvphone,  tvEmail;
    Button btnChange;
    String mail, fname, lname, phone;
    private User theUser=null;
    String uid;
    DatabaseService databaseService;
    private User user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        uid= AuthenticationService.getInstance().getCurrentUserId();
        databaseService=DatabaseService.getInstance();
        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                theUser=object;
                if (theUser != null) {
                    fname = theUser.getFname() + "";
                    lname = theUser.getLname() + "";
                    mail = theUser.getEmail() + "";
                    phone = theUser.getPhone() + "";
                    tvFname.setText("" + fname);
                    tvLname.setText("" + lname);
                    tvEmail.setText("" + mail);
                    tvphone.setText("" + phone);
                }
            }
            @Override
            public void onFailed(Exception e) {
            }
        });
        initViews();
    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        tvFname = findViewById(R.id.tvUserFname);
        tvLname = findViewById(R.id.tvUserLname);
        tvphone = findViewById(R.id.tvUserPhone);
        tvEmail = findViewById(R.id.tvUserMail);
        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (btnChange == view) {
            Intent goToChange = new Intent(UserProfile.this, ChangeUser.class);
            startActivity(goToChange);
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