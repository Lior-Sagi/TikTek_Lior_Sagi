package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;

import kotlin.jvm.Synchronized;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Thread mSplashThread= new Thread(){
            @Override
            public void run()
            {
                try{
                    synchronized (this){
                        wait(3000);
                    }
                } catch (InterruptedException e) {
                }
                finish();
                Intent go = new Intent(getApplicationContext(), LandingPage.class);
                startActivity(go);
            }
        };
        mSplashThread.start();
    }
}