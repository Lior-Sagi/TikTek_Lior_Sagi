package com.example.tiktek_lior_sagi.adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.screens.FavouredBooks;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavouredBookAdapter extends RecyclerView.Adapter<FavouredBookAdapter.UserViewHolder> {
    private List<Book> bookList;
    private List<String> bookIds;
    private Context context;

    public FavouredBookAdapter(List<Book> bookList, List<String> bookIds, Context context) {
        this.context = context;
        this.bookList = bookList;
        this.bookIds = bookIds;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvSubject, tvMaxPages;
        ImageView imgVBookCover;
        Button btnMarkAsFavoured;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvMaxPages = itemView.findViewById(R.id.tvMaxPages);
            imgVBookCover = itemView.findViewById(R.id.imgVBookCover);
            btnMarkAsFavoured = itemView.findViewById(R.id.btnMarkAsFavoured);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.book_row, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Book book = bookList.get(position);
        String uid = bookIds.get(position);

        holder.tvBookName.setText(book.getBookName());
        holder.tvSubject.setText(book.getSubject());
        holder.tvMaxPages.setText(String.valueOf(book.getMaxPages()));

        Log.d("BookAdapter", "Cover string: " + book.getBookCover());

        byte[] decodedString = Base64.decode(book.getBookCover(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imgVBookCover.setImageBitmap(decodedByte);

        holder.btnMarkAsFavoured.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("books").child(uid).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show();

                        bookList.remove(position);
                        bookIds.remove(position);
                        notifyItemRemoved(position);

                        sendNotification("Book Removed", "The book \"" + book.getBookName() + "\" was removed from your favorites.");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete Book", Toast.LENGTH_SHORT).show());
        });
    }

    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "favoured_book_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Book Updates", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for favoured book updates");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
