package com.example.tiktek_lior_sagi.screens;

import static android.content.ContentValues.TAG;
import static android.opengl.ETC1.isValid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.car.ui.toolbar.MenuItem;
import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.adapter.BookSpinnerAdapter;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.ImageUtil;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddAnswer extends AppCompatActivity implements View.OnClickListener {

    /// tag for logging
    private static final String TAG = "AddAnswerActivity";
    ImageView ivQuestion;
    Spinner spPages, spQuestion;
    Button btnCamera, btnGallery, btnAddAnswer;
    Book book2 = null;
    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;
    int SELECT_PICTURE = 200;
    private DatabaseService databaseService;
    BookSpinnerAdapter bookSpinnerAdapter;
    private ArrayList<Book> allBooks = new ArrayList<>();
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    private Spinner spbookSpinner, spSubject;
    String subject = "";
    ArrayAdapter<String> bookPagesAdapter;
    private FirebaseAuth mAuth;
    private User user;


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

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
        /// request permission for the camera and storage
        ImageUtil.requestPermission(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(), "התחבר למשתמש בשביל לגשת לדף", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        }
        initViews();


        allBooks = new ArrayList<>();


        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = (String) parent.getItemAtPosition(position);
                databaseService.getBooks(new DatabaseService.DatabaseCallback<List<Book>>() {
                    @Override
                    public void onCompleted(List<Book> object) {
                        allBooks.clear();
                        allBooks.addAll(object);

                        // Initially filter books based on the first selected subject
                        selectedBooks.clear();
                        for (Book book : allBooks) {
                            if (book.getSubject().contains(subject)) {
                                selectedBooks.add(book);
                            }

                            bookSpinnerAdapter = new BookSpinnerAdapter(AddAnswer.this, android.R.layout.simple_spinner_item, selectedBooks);

                            spbookSpinner.setAdapter(bookSpinnerAdapter);
                            // Notify adapter of the changes
                            bookSpinnerAdapter.notifyDataSetChanged();
                            spbookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Book book = (Book) parent.getItemAtPosition(position);
                                    book2 = book;
                                    String[] bookPages = new String[book.getMaxPages()];
                                    for (int i = 0; i < bookPages.length; i++) {
                                        bookPages[i] = ((i + 1) + "");
                                    }
                                    Log.d(TAG, Arrays.toString(bookPages));
                                    bookPagesAdapter = new ArrayAdapter<>(AddAnswer.this, android.R.layout.simple_spinner_item, bookPages);
                                    spPages.setAdapter(bookPagesAdapter);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onFailed(Exception e) {

                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


    }
    /// select image from gallery
    private void selectImageFromGallery() {
        imageChooser();
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        ivQuestion = findViewById(R.id.ivQuestion);
        spPages = findViewById(R.id.spPages);
        spQuestion = findViewById(R.id.spQuestion);
        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);
        btnAddAnswer = findViewById(R.id.btnAddAnswer);
        btnAddAnswer.setOnClickListener(this);
        spbookSpinner = findViewById(R.id.spBooks);
        spSubject = findViewById(R.id.spSubjectAddAnswer);
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

    private void addAnswerToDatabase() {
        /// get the values from the input fields
        int page = Integer.parseInt(spPages.getSelectedItem().toString());
        int questionNumber = Integer.parseInt(spQuestion.getSelectedItem().toString());
        String imageBase64 = ImageUtil.convertTo64Base(ivQuestion);

        /// validate the input
        /// stop if the input is not valid
        //if (!isValid(page, questionNumber, imageBase64)) return;
        /// generate a new id for the book
        String id = databaseService.generateAnswerId();

        Log.d(TAG, "Adding book to database");
        Log.d(TAG, "ID: " + id);
        Log.d(TAG, "page: " + page);
        Log.d(TAG, "questionNumber: " + questionNumber);
        Log.d(TAG, "Image: " + imageBase64);

        /// create a new answer object
        Answer answer = new Answer(id, book2.getId(), page, questionNumber, imageBase64);
        /// save the answer to the database and get the result in the callback
        databaseService.createNewAnswer(answer, book2, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "Book added successfully");
                Toast.makeText(AddAnswer.this, "תשובה נוספה בהצלחה", Toast.LENGTH_SHORT).show();
                /// clear the input fields after adding the book for the next answer
                ivQuestion.setImageBitmap(null);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to add answer", e);
                Toast.makeText(AddAnswer.this, "תקלה,תשובה לא נוספה", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ;

    @Override
    public void onClick(View v) {
        if (v.getId() == btnGallery.getId()) {
            // select image from gallery
            Log.d(TAG, "Select image button clicked");
            selectImageFromGallery();
            return;
        }
        if (v.getId() == btnCamera.getId()) {
            // capture image from camera
            Log.d(TAG, "Capture image button clicked");
            captureImageFromCamera();

        }
        if (v.getId() == btnAddAnswer.getId()) {
            if (ivQuestion.getDrawable() == null) {
                Toast.makeText(AddAnswer.this, "need to add a picture", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "Add answer button clicked");
            addAnswerToDatabase();
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