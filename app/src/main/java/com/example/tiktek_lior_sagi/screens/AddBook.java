package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    Button btnCamera;
    Button btnGallery;
    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;
    private DatabaseService databaseService;
    // One Preview Image
    ImageView IVPreviewImage;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /// request permission for the camera and storage
        ImageUtil.requestPermission(this);

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
        btnCamera=findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        btnGallery=findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}