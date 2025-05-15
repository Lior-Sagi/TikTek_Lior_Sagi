package com.example.tiktek_lior_sagi.adapter;

import android.content.Context;
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
import com.example.tiktek_lior_sagi.model.Answer;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.UserViewHolder> {
    private String bookId;
    private List<Answer> answerList;
    private List<String> answerIds;
    private Context context;

    public AnswerAdapter(String bookId, List<Answer> answerList, List<String> answerIds, Context context) {
        this.bookId = bookId;
        this.answerList = answerList;
        this.answerIds = answerIds;
        this.context = context;
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView  tvPage,tvQuestionNumber;
        ImageView ivAnswerPic;
        Button btnDeleteAnswer;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvPage = itemView.findViewById(R.id.tvPage);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            ivAnswerPic = itemView.findViewById(R.id.ivAnswerPic);
            btnDeleteAnswer = itemView.findViewById(R.id.btnDeleteAnswer);
        }
    }

    @NonNull
    @Override
    public AnswerAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.answer_row, parent, false);
        return new AnswerAdapter.UserViewHolder(v);
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
    // Required method that RecyclerView calls
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        onBindViewHolder(holder, position, this); // delegate to your custom version
    }
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position,AnswerAdapter answerAdapter) {
        Answer answer = answerList.get(position);
        String bookId= answerAdapter.bookId;
        String uid = answerIds.get(position);

        holder.tvPage.setText(answer.getPage());
        holder.tvQuestionNumber.setText(String.valueOf(answer.getQuestionNumber()));
        Log.d("answerAdapter", "Cover string: " + answer.getPicAnswer());
        // Decode Base64 string to byte array
        byte[] decodedString = android.util.Base64.decode(answer.getPicAnswer(), android.util.Base64.DEFAULT);

// Convert byte array to Bitmap
        android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

// Set the Bitmap to the ImageView
        holder.ivAnswerPic.setImageBitmap(decodedByte);



        holder.btnDeleteAnswer.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("books/"+bookId+"pagesList").child(uid).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "answer deleted", Toast.LENGTH_SHORT).show();
                        answerList.remove(position);
                        answerIds.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete answer", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
