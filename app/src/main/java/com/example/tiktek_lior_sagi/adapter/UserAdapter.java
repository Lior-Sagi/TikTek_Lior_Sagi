package com.example.tiktek_lior_sagi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    //user adapter for UsersManage
    private List<User> userList;
    private List<String> userIds;
    private Context context;

    public UserAdapter(Context context, List<User> userList, List<String> userIds) {
        this.context = context;
        this.userList = userList;
        this.userIds = userIds;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvFname, tvLname,tvPhone,tvEmail,tvIsAdmin;
        Button btnDeleteUser;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvFname = itemView.findViewById(R.id.tvFname);
            tvLname = itemView.findViewById(R.id.tvLname);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvIsAdmin = itemView.findViewById(R.id.tvIsAdmin);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        String uid = userIds.get(position);

        holder.tvFname.setText(user.getFname());
        holder.tvLname.setText(user.getLname());
        holder.tvPhone.setText(user.getPhone());
        holder.tvEmail.setText(user.getEmail());
        holder.tvIsAdmin.setText(user.getAdmin()+"");
        //deletes a user from firebase path and removes from  RecyclerView
        holder.btnDeleteUser.setOnClickListener(v -> {
            // Store the current position and user data to avoid index issues
            final int currentPosition = holder.getAdapterPosition();
            final String userId = uid;

            // Validate position before proceeding
            if (currentPosition == RecyclerView.NO_POSITION || currentPosition >= userList.size()) {
                Toast.makeText(context, "Error: Invalid position", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseDatabase.getInstance().getReference("Users").child(uid).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                        // Fallback: manual removal with bounds checking
                        if (currentPosition >= 0 && currentPosition < userList.size() && currentPosition < userIds.size()) {
                            userList.remove(currentPosition);
                            userIds.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            notifyItemRangeChanged(currentPosition, userList.size());
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

