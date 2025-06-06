package com.example.tiktek_lior_sagi.adapter;


import android.content.Context;
import android.content.Intent;
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
import com.example.tiktek_lior_sagi.screens.ChangeAnswer;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.UserViewHolder> {
    //adapter for the answers in AnswersManage
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
        TextView tvPage, tvQuestionNumber;
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

    public void onBindViewHolder(@NonNull UserViewHolder holder, int position, AnswerAdapter answerAdapter) {
        Answer answer = answerList.get(position);
        String bookId = answerAdapter.bookId;
        String uid = answerIds.get(position);
        String stPageNumber = answer.getPage() + "";

        holder.tvPage.setText(String.valueOf(answer.getPage()));
        holder.tvQuestionNumber.setText(String.valueOf(answer.getQuestionNumber()));
        Log.d("answerAdapter", "Cover string: " + answer.getPicAnswer());
        // Decode Base64 string to byte array
        byte[] decodedString = android.util.Base64.decode(answer.getPicAnswer(), android.util.Base64.DEFAULT);

// Convert byte array to Bitmap
        android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

// Set the Bitmap to the ImageView
        holder.ivAnswerPic.setImageBitmap(decodedByte);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, ChangeAnswer.class);
                go.putExtra("Answer", answer);
                context.startActivity(go);
            }
        });

        //goes to firebase path and deletes the answer
        //after answer is deleted from firebase remove from RecyclerView
        holder.btnDeleteAnswer.setOnClickListener(v -> {
            // Store the current position and answer data to avoid index issues
            final int currentPosition = holder.getAdapterPosition();
            FirebaseDatabase.getInstance().getReference("books/" + bookId + "/pagesList")
                    .child(stPageNumber)
                    .child(uid)
                    .removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Answer deleted", Toast.LENGTH_SHORT).show();
                        // Fallback: manual removal with bounds checking
                        if (currentPosition >= 0 && currentPosition < answerList.size() && currentPosition < answerIds.size()) {
                            answerList.remove(currentPosition);
                            answerIds.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            notifyItemRangeChanged(currentPosition, answerList.size());
                        }
                    });

        });
    };
    @Override
    public int getItemCount () {
        return answerList.size();
    }
}

