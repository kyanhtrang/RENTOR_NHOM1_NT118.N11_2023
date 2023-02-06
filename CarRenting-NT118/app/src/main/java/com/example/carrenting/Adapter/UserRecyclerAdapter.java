package com.example.carrenting.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.Model.User;
import com.example.carrenting.R;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>{

    private ArrayList<User> mUsers = new ArrayList<>();
    private UserListRecyclerClickListener mClickListener;

    public UserRecyclerAdapter(ArrayList<User> users, UserListRecyclerClickListener clickListener) {
        mUsers = users;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ((ViewHolder)holder).username.setText(mUsers.get(position).getUsername());
        ((ViewHolder)holder).email.setText(mUsers.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView username, email;
        UserListRecyclerClickListener mClickListener;

        public ViewHolder(View itemView, UserListRecyclerClickListener clickListener) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.edtCity);

            mClickListener = clickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onUserClicked(getAdapterPosition());
        }
    }

    public interface UserListRecyclerClickListener{
        void onUserClicked(int position);
    }
}
















