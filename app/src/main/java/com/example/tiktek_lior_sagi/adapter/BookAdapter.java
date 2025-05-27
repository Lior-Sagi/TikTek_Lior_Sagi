package com.example.tiktek_lior_sagi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.User;
import com.example.tiktek_lior_sagi.screens.BooksManage;
import com.example.tiktek_lior_sagi.utils.ImageUtil;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.UserViewHolder> {
    private List<Book> bookList;
    private List<String> bookIds;
    private Context context;

    public BookAdapter(List<Book> bookList, List<String> bookIds, Context context) {
        this.context = context;
        this.bookList = bookList;
        this.bookIds = bookIds;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvSubject, tvMaxPages;
        ImageView imgVBookCover;
        Button btnDeleteBook;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvMaxPages = itemView.findViewById(R.id.tvMaxPages);
            imgVBookCover = itemView.findViewById(R.id.imgVBookCover);
            btnDeleteBook = itemView.findViewById(R.id.btnDeleteBook);
        }
    }

    @NonNull
    @Override
    public BookAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.book_row, parent, false);
        return new BookAdapter.UserViewHolder(v);
    }

    private void loadImageFromUrl(String urlString, ImageView imageView) {
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                java.io.InputStream input = connection.getInputStream();
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(input);

                // Set the image on the UI thread
                ((android.app.Activity) context).runOnUiThread(() -> {
                    imageView.setImageBitmap(bitmap);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Book book = bookList.get(position);
        String uid = bookIds.get(position);

        holder.tvBookName.setText(book.getBookName());
        holder.tvSubject.setText(book.getSubject());
        holder.tvMaxPages.setText(String.valueOf(book.getMaxPages()));
        Log.d("BookAdapter", "Cover string: " + book.getBookCover());

        // Safely decode Base64 string to bitmap
        try {
            if (book.getBookCover() != null && !book.getBookCover().isEmpty()) {
                byte[] decodedString = android.util.Base64.decode(book.getBookCover(), android.util.Base64.DEFAULT);
                android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgVBookCover.setImageBitmap(decodedByte);
            } else {
                // Set a default image or placeholder
                holder.imgVBookCover.setImageResource(R.drawable.ic_launcher_background); // Replace with your default image
            }
        } catch (Exception e) {
            Log.e("BookAdapter", "Error decoding book cover: " + e.getMessage());
            holder.imgVBookCover.setImageResource(R.drawable.ic_launcher_background); // Replace with your default image
        }

        holder.btnDeleteBook.setOnClickListener(v -> {
            // Store the current position and book data to avoid index issues
            final int currentPosition = holder.getAdapterPosition();
            final String bookId = uid;

            // Validate position before proceeding
            if (currentPosition == RecyclerView.NO_POSITION || currentPosition >= bookList.size()) {
                Toast.makeText(context, "Error: Invalid position", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseDatabase.getInstance().getReference("books").child(bookId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show();

                        // Use the BooksManage method for safe removal
                        if (context instanceof BooksManage) {
                            ((BooksManage) context).removeBook(currentPosition);
                        } else {
                            // Fallback: manual removal with bounds checking
                            if (currentPosition >= 0 && currentPosition < bookList.size() && currentPosition < bookIds.size()) {
                                bookList.remove(currentPosition);
                                bookIds.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                notifyItemRangeChanged(currentPosition, bookList.size());
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to delete Book", Toast.LENGTH_SHORT).show();
                        Log.e("BookAdapter", "Failed to delete book: " + e.getMessage());
                    });
        });
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
}