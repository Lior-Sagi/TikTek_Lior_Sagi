package com.example.tiktek_lior_sagi.screens;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.services.AuthenticationService;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.ImageUtil;
import com.example.tiktek_lior_sagi.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddBook extends AppCompatActivity implements View.OnClickListener {
    EditText etBookName,etPagesNumber;
    Spinner spSubject;
    Button btnCamera,btnGallery,btnAddBook;
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
    private User user;

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
        initviews();
        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        IVPreviewImage.setImageURI(selectedImage);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        IVPreviewImage.setImageBitmap(bitmap);
                    }
                });
    }
    private void initviews() {
        databaseService = DatabaseService.getInstance();
        btnAddBook=findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(this);
        btnCamera=findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        btnGallery=findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);
        IVPreviewImage=findViewById(R.id.IVPreviewImage);
        etBookName=findViewById(R.id.etBookName);
        etPagesNumber=findViewById(R.id.etPagesNumber);
        spSubject=findViewById(R.id.spSubject);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAddBook.getId()) {
            if(IVPreviewImage.getDrawable() == null) {
                Toast.makeText(AddBook.this, "need to add a picture", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "Add book button clicked");
            addBookToDatabase();
        }
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
            return;
        }
    }
    /// add the book to the database
    /// @see Book
    private void addBookToDatabase() {
        /// get the values from the input fields
        String bookName = etBookName.getText().toString();
        String pages = etPagesNumber.getText().toString();
        String imageBase64 = ImageUtil.convertTo64Base(IVPreviewImage);

        /// validate the input
        /// stop if the input is not valid
        if (!isValid(bookName, pages, imageBase64)) return;

        /// convert the price to double
        int numOfPages = Integer.parseInt(pages);

        String subject=spSubject.getSelectedItem().toString();

        /// generate a new id for the book
        String id = databaseService.generateBookId();

        Log.d(TAG, "Adding book to database");
        Log.d(TAG, "ID: " + id);
        Log.d(TAG, "bookName: " + bookName);
        Log.d(TAG, "Pages: " + numOfPages);
        Log.d(TAG, "Image: " + imageBase64);


        Map<Integer, Map<String, Answer>> pagesList = new HashMap<>();

//        Answer answer = new Answer();
//        pagesList.getOrDefault(2, new HashMap<>()).put(answer.getId(), answer);

        /// create a new book object
        Book book = new Book(id, subject, bookName,imageBase64, new HashMap<>(), numOfPages);

        /// save the book to the database and get the result in the callback
        databaseService.createNewBook(book, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "Book added successfully");
                Toast.makeText(AddBook.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                /// clear the input fields after adding the book for the next book
                Log.d(TAG, "Clearing input fields");
                etBookName.setText("");
                etPagesNumber.setText("");
                IVPreviewImage.setImageBitmap(null);
            }
            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to add book", e);
                Toast.makeText(AddBook.this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /// select image from gallery
    private void selectImageFromGallery() {
        //   Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  selectImageLauncher.launch(intent);

        imageChooser();
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }


    /// validate the input
    private boolean isValid(String name, String priceText, String imageBase64) {
        if (name.isEmpty()) {
            Log.e(TAG, "Name is empty");
            etBookName.setError("Name is required");
            etPagesNumber.requestFocus();
            return false;
        }

        if (priceText.isEmpty()) {
            Log.e(TAG, "Price is empty");
            etPagesNumber.setError("Pages is required");
            etBookName.requestFocus();
            return false;
        }

        if (imageBase64 == null) {
            Log.e(TAG, "Image is required");
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        user= SharedPreferencesUtil.getUser(this);
        if(!user.getAdmin()){
            menu.removeGroup(R.id.adminMenu);
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