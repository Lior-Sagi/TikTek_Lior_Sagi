package com.example.tiktek_lior_sagi.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    /// @see Log
    private static final String TAG = "DatabaseService";

    /// callback interface for database operations
    /// @param <T> the type of the object to return
    /// @see DatabaseCallback#onCompleted(Object)
    /// @see DatabaseCallback#onFailed(Exception)
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully
        void onCompleted(T object);

        /// called when the operation fails with an exception
        void onFailed(Exception e);
    }

    /// the instance of this class
    /// @see #getInstance()
    private static DatabaseService instance;

    /// the reference to the database
    /// @see DatabaseReference
    /// @see FirebaseDatabase#getReference()
    private final DatabaseReference databaseReference;

    /// use getInstance() to get an instance of this class
    /// @see DatabaseService#getInstance()
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /// get an instance of this class
    /// @return an instance of this class
    /// @see DatabaseService
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }


    // private generic methods to write and read data from the database

    /// write data to the database at a specific path
    /// @param path the path to write the data to
    /// @param data the data to write (can be any object, but must be serializable, i.e. must have a default constructor and all fields must have getters and setters)
    /// @param callback the callback to call when the operation is completed
    /// @return void
    /// @see DatabaseCallback
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(task.getResult());
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    /// read data from the database at a specific path
    /// @param path the path to read the data from
    /// @return a DatabaseReference object to read the data from
    /// @see DatabaseReference

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }


    /// get data from the database at a specific path
    /// @param path the path to get the data from
    /// @param clazz the class of the object to return
    /// @param callback the callback to call when the operation is completed
    /// @return void
    /// @see DatabaseCallback
    /// @see Class
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    /// generate a new id for a new object in the database
    /// @param path the path to generate the id for
    /// @return a new id for the object
    /// @see String
    /// @see DatabaseReference#push()

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // end of private methods for reading and writing data

    // public methods to interact with the database

    /// create a new user in the database
    /// @param user the user object to create
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive void
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see User
    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback);
    }

    /// create a new book in the database
    /// @param book the book object to create
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive void
    ///             if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Book
    public void createNewBook(@NotNull final Book book, @Nullable final DatabaseCallback<Void> callback) {
        writeData("books/" + book.getId(), book, callback);
    }

    /// create a new answer in the database
    /// @param answer the answer object to create
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive void
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Answer
   public void createNewAnswer(@NotNull final Answer answer, @NotNull final Book book,  @Nullable final DatabaseCallback<Void> callback ) {
     writeData("books/" + book.getId()+"/pagesList/\"" + answer.getPage()+"\"/"+answer.getId(), answer, callback);
     }

    /// get a user from the database
    /// @param uid the id of the user to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the user object
    ///             if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see User
    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("Users/" + uid, User.class, callback);
    }



    /// get a book from the database
    /// @param bookId the id of the book to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the book object
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Book
    public void getBook(@NotNull final String bookId, @NotNull final DatabaseCallback<Book> callback) {
        getData("books/" + bookId, Book.class, callback);
    }



    /// generate a new id for a new book in the database
    /// @return a new id for the book
    /// @see #generateNewId(String)
    /// @see Book
    public String generateBookId() {
        return generateNewId("books");
    }

    /// generate a new id for a new answer in the database
    /// @return a new id for the answer
    /// @see #generateNewId(String)
    /// @see Answer
    public String generateAnswerId() {
        return generateNewId("answers");
    }




    /// get all the books from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of book objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    /// @see Book
    /// @see #getData(String, Class, DatabaseCallback)
    public void getBooks(@NotNull final DatabaseCallback<List<Book>> callback) {
        readData("books").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Book> books = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Book book = dataSnapshot.getValue(Book.class);
                Log.d(TAG, "Got book: " + book);
                books.add(book);
            });

            callback.onCompleted(books);
        });
    }

    /// get all the users from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of book objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    /// @see Book
    /// @see #getData(String, Class, DatabaseCallback)
    public void getUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        readData("Users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<User> users = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Got user: " + user);
                users.add(user);
            });

            callback.onCompleted(users);
        });
    }


    /// get all the books from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of book objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    /// @see Book
    /// @see #getData(String, Class, DatabaseCallback)
    public void getAnswer(  @NotNull final String path,    @NotNull final DatabaseCallback<List<Answer>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Answer> answerList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Answer answer = dataSnapshot.getValue(Answer.class);
                Log.d(TAG, "Got book: " + answer);
                answerList.add(answer);
            });

            callback.onCompleted(answerList);
        });
    }


    /// create a new user in the database
    /// @param user the user object to create
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive void
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see User
    public void userSearches(@NotNull final String id, @Nullable final Book book,  @Nullable final DatabaseCallback<Void> callback) {
        writeData("usersBooks/" + id +"/" + book.getId(), book, callback);
    }

    public void getUserSearches(  @NotNull final String uid,    @NotNull final DatabaseCallback<List<Book>> callback) {
        readData("usersBooks/"+uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Book> bookList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Book book = dataSnapshot.getValue(Book.class);
                Log.d(TAG, "Got book: " + book);
                bookList.add(book);
            });

            callback.onCompleted(bookList);
        });
    }




}
