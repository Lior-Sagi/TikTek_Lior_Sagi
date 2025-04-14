package com.example.tiktek_lior_sagi.adapter;

import android.content.Context;
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
        TextView tvBookName, tvSubject,tvMaxPages;
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

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Book book = bookList.get(position);
        String uid = bookIds.get(position);

        holder.tvBookName.setText(book.getBookName());
        holder.tvSubject.setText(book.getSubject());
        holder.tvMaxPages.setText(String.valueOf(book.getMaxPages()));

        holder.btnDeleteBook.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("books").child(uid).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show();
                        bookList.remove(position);
                        bookIds.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete Book", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
