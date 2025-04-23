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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.car.ui.toolbar.MenuItem;
import com.example.tiktek_lior_sagi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.tiktek_lior_sagi.model.User;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnToRegister,btnToLogin,btnToAddAnswer,btnToSearch;
    
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
    }

    private void initviews()
    {
        btnToRegister=findViewById(R.id.btnToRegister);
        btnToRegister.setOnClickListener(this);

        btnToLogin=findViewById(R.id.btnToLogin);
        btnToLogin.setOnClickListener(this);

        btnToAddAnswer=findViewById(R.id.btnToAddAnswer);
        btnToAddAnswer.setOnClickListener(this);

        btnToSearch=findViewById(R.id.btnToSearch);
        btnToSearch.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        if(view==btnToRegister)
        {
            Intent go = new Intent(getApplicationContext(), Register.class);
            startActivity(go);
        }
        if(view==btnToLogin)
        {
            Intent go = new Intent(getApplicationContext(), Login.class);
            startActivity(go);
        }
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMain) {
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        else if (id == R.id.menuAddAnswer) {
            Intent go = new Intent(getApplicationContext(), AddAnswer.class);
            startActivity(go);
        }
        else if (id == R.id.menuLogOut) {
            Intent go = new Intent(getApplicationContext(), AddAnswer.class);
            startActivity(go);
        }
        else if (id == R.id.menuSearchAnswer) {
            Intent go = new Intent(getApplicationContext(), Search.class);
            startActivity(go);
        }
        return true;
    }

}

