package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

public class LandingPage extends AppCompatActivity implements View.OnClickListener {
    Button btnToRegister,btnToLogin,btnToUserGuide;
    User user=null;
    private FirebaseAuth mAuth;
    AuthenticationService authenticationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
        mAuth=FirebaseAuth.getInstance();
        authenticationService = AuthenticationService.getInstance();
    }
    private void initviews() {
        btnToRegister=findViewById(R.id.btnToRegister);
        btnToRegister.setOnClickListener(this);

        btnToLogin=findViewById(R.id.btnToLogin);
        btnToLogin.setOnClickListener(this);

        btnToUserGuide=findViewById(R.id.btnToUserGuide);
        btnToUserGuide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnToRegister)
        {
            Intent go = new Intent(getApplicationContext(), Register.class);
            startActivity(go);
        }
        if(v==btnToLogin)
        {
            Intent go = new Intent(getApplicationContext(), Login.class);
            startActivity(go);
        }
        if(v==btnToUserGuide)
        {
            Intent go = new Intent(getApplicationContext(), UserGuide.class);
            startActivity(go);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if(authenticationService.isUserSignedIn())
        {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        if(user != null)
        {
            user= SharedPreferencesUtil.getUser(this);
            if(!user.getAdmin()){
                menu.removeGroup(R.id.adminMenu);
            }
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
        return true;
    }
}