package com.example.tiktek_lior_sagi.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Book;
import com.example.tiktek_lior_sagi.model.SendBook;
import com.example.tiktek_lior_sagi.screens.Answers;

import java.util.List;

public class SendBookAdapter extends RecyclerView.Adapter<SendBookAdapter.UserViewHolder> {

    private List<SendBook> sendBookList;
    private List<String> sendBookIds;
    public Context context;

    public SendBookAdapter(List<SendBook> bookList,List<String> sendBookIds, Context context) {
        this.context = context;
        this.sendBookList = bookList;
        this.sendBookIds = sendBookIds;
    }

    public  class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvQuestionNumber, tvPageNumber;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvPageNumber = itemView.findViewById(R.id.tvPageNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go=new Intent(context, Answers.class);
                    go.putExtra("sendBook",sendBookList.get(getAdapterPosition()));
                   context.startActivity(go);
                }
            });
        }
    }
    @NonNull
    @Override
    public SendBookAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.send_book_row, parent, false);
        return new SendBookAdapter.UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        SendBook sendBook = sendBookList.get(position);

        holder.tvBookName.setText(sendBook.getBookName());
        holder.tvQuestionNumber.setText(sendBook.getQuestionNumber());
        holder.tvPageNumber.setText(String.valueOf(sendBook.getPage()));
    }

    @Override
    public int getItemCount() {
        return sendBookList.size();
    }
}
