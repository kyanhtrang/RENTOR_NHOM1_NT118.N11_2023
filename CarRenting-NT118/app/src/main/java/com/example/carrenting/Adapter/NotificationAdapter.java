package com.example.carrenting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private CustomerNotificationFragment customerNotificationFragment;
    private ArrayList<String> title, content;

    public NotificationAdapter(ArrayList<String> a, ArrayList<String> b) {
        this.title = a;
        this.content = b;
    }
    public NotificationAdapter(CustomerNotificationFragment mContext, ArrayList<String> a, ArrayList<String> b){
        this.customerNotificationFragment = mContext;
        this.title = a;
        this.content =  b;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.vtitle.setText(title.get(position));
        holder.vcontent.setText(content.get(position));
    }

    @Override
    public int getItemCount() {
        if(title != null)
            return title.size();
        return 0;
    }
    public  class  NotificationViewHolder extends RecyclerView.ViewHolder{
        private TextView vtitle;
        private TextView vcontent;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            vtitle = itemView.findViewById(R.id.item_notification_tv_title);
            vcontent = itemView.findViewById(R.id.item_notification_tv_content);
        }
    }
}
