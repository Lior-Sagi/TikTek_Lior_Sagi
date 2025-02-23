package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.BookSpinnerAdapter;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddAnswer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /// tag for logging
    private static final String TAG = "AddAnswerActivity";
    ImageView ivBookCover,ivQuestion;
    TextView tvBookName;
    Spinner spPages,spQuestion;
    Button btnCamera,btnGallery,btnAddAnswer;
    Intent takeit;
    Book book=null;
    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;
    int SELECT_PICTURE = 200;
    private DatabaseService databaseService;
    BookSpinnerAdapter bookSpinnerAdapter;
    protected List<ArrayList<Answer>> allpagesListAnswers=new ArrayList<>();
    private ArrayList<Book>allBooks=new ArrayList<>();
    private ArrayList<Book>selectedBooks=new ArrayList<>();
    private Spinner spbookSpinner, spSubject;
    String subject="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_answer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService=DatabaseService.getInstance();
        initViews();

        allBooks=new ArrayList<>();

        bookSpinnerAdapter=new BookSpinnerAdapter(AddAnswer.this, android.R.layout.simple_spinner_item,selectedBooks);
       spbookSpinner.setAdapter(bookSpinnerAdapter);

        databaseService.getBooks(new DatabaseService.DatabaseCallback<List<Book>>() {
            @Override
            public void onCompleted(List<Book> object) {
                allBooks.clear();
                allBooks.addAll(object);

                // Initially filter books based on the first selected subject
                selectedBooks.clear();
                for (Book book : allBooks) {
                    if (book.getSubject().equals(subject)) {
                        selectedBooks.add(book);
                    }
                }

                // Notify adapter instead of creating a new one
                bookSpinnerAdapter.notifyDataSetChanged();
                spbookSpinner.setOnItemSelectedListener(this);


            }
            @Override
            public void onFailed(Exception e) {

            }
        });
        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ivQuestion.setImageURI(selectedImage);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ivQuestion.setImageBitmap(bitmap);
                    }
                });

        takeit=getIntent();
        book= (Book) takeit.getSerializableExtra("book");
        if(book!=null){
            tvBookName.setText(book.getBookName());
            ivBookCover.setImageURI(Uri.parse(book.getBookCover()));
        }
    }
    /// select image from gallery
    private void selectImageFromGallery() {
        //   Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  selectImageLauncher.launch(intent);
        imageChooser();
    }

    /// capture image from camera

    private void initViews() {
        ivBookCover=findViewById(R.id.ivBookCover);
        ivQuestion=findViewById(R.id.ivQuestion);
        tvBookName=findViewById(R.id.tvBookName);
        spPages=findViewById(R.id.spPages);
        spQuestion=findViewById(R.id.spQuestion);
        btnCamera=findViewById(R.id.btnCamera);
        btnGallery=findViewById(R.id.btnGallery);
        btnAddAnswer=findViewById(R.id.btnAddAnswer);
        spbookSpinner = findViewById(R.id.spBooks);
        spSubject=findViewById(R.id.spSubjectAddAnswer);

        spSubject.setOnItemSelectedListener(this);

    }
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }
    public void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ivQuestion.setImageURI(selectedImageUri);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subject = (String) parent.getItemAtPosition(position);

        // Clear and update selectedBooks list
        selectedBooks.clear();
        for (Book book : allBooks) {
            if (book.getSubject().equals(subject)) {
                selectedBooks.add(book);
            }
        }

        // Notify adapter about data change
        bookSpinnerAdapter.notifyDataSetChanged();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}