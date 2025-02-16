package com.example.tiktek_lior_sagi.screens;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

public class AddBook extends AppCompatActivity implements View.OnClickListener {


    EditText etBookName;
    EditText etPagesNumber;
    Spinner spSubject;
    Button btnCamera;
    Button btnGallery;
    Button btnAddBook;
    Button btnToSearch;
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
        btnToSearch=findViewById(R.id.btnToSearch);
        btnToSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //quick access to search page
        if(v==btnToSearch)
        {
            Intent goLog=new Intent(getApplicationContext(), Search.class);
            startActivity(goLog);
        }
        if (v.getId() == btnAddBook.getId()) {
            Log.d(TAG, "Add book button clicked");
            addBookToDatabase();
            return;
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



            /// create a new book object
        Book book = new Book(id, subject, bookName, numOfPages,imageBase64,null);

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
                Intent go=new Intent(getApplicationContext(), AddAnswer.class);
                go.putExtra("book",book);

                startActivity(go);


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
}