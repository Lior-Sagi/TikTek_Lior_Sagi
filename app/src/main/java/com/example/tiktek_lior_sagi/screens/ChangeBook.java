package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.SendBook;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

public class ChangeBook extends AppCompatActivity implements View.OnClickListener {
    Book book;
    Spinner spSubject;
    EditText etBookName,etPagesNumber;
    ImageView ivBookCover;
    Button btnSave;
    String subject,bookName,pages;
    DatabaseService databaseService;
    String bookId,imageBase64;
    TextView tvCurrentSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ChangeBook), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        databaseService = new DatabaseService();
        book=getIntent().getSerializableExtra("Book", Book.class);
        this.retrieveData();
    }

    private void retrieveData() {
        bookId=book.getId();
        databaseService.getBook(bookId, new DatabaseService.DatabaseCallback<Book>() {
            @Override
            public void onCompleted(Book object) {
                book=object;
                if(book!=null) {
                    etBookName.setText(String.valueOf(book.getBookName()));
                    etPagesNumber.setText(String.valueOf(book.getMaxPages()));
                    tvCurrentSubject.setText(String.valueOf(book.getSubject()));
                    String base64Image = book.getBookCover();
                    Bitmap bitmap = ImageUtil.convertFrom64base(base64Image);
                    ivBookCover.setImageBitmap(bitmap);
                }
            }
            @Override
            public void onFailed(Exception e) {
            }
        });
    }

    private void initViews() {
        spSubject = findViewById(R.id.spSubject);
        etBookName = findViewById(R.id.etBookName);
        etPagesNumber = findViewById(R.id.etPagesNumber);
        ivBookCover = findViewById(R.id.ivBookCover);
        btnSave= findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        tvCurrentSubject= findViewById(R.id.tvCurrentSubject);
    }
    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            subject=spSubject.getSelectedItem().toString();
            bookName=etBookName.getText().toString();
            pages=etPagesNumber.getText().toString();
            book.setSubject(subject);
            book.setBookName(bookName);
            book.setMaxPages(Integer.parseInt(pages));
            databaseService.updateBook(book, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(getApplicationContext(), "פרטי הספר עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(getApplicationContext(), "חלה שגיאה,פרטי הספר לא עודכנו", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(ChangeBook.this, BooksManage.class);
            startActivity(intent);
        }
    }
}