package com.example.tiktek_lior_sagi.screens;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.utils.DataHolder;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

public class OneAnswer extends AppCompatActivity {
    ImageView ivAnswerPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_one_answer);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivAnswerPic = findViewById(R.id.ivAnswerPic);
        String imageBase64 = DataHolder.base64Image;
        Bitmap bitmap = ImageUtil.convertFrom64base(imageBase64);
        if (bitmap != null) {
            ivAnswerPic.setImageBitmap(bitmap);
        }
    }
}