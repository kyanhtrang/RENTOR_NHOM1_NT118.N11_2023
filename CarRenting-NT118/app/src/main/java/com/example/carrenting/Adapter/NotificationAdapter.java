package com.example.carrenting.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.R;
import com.example.carrenting.Service.Notification.NotificationActivity;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    CustomerNotificationFragment customerNotificationFragment;
    Notification noti;
    ArrayList<Notification> mNoti;
    NotificationActivity notificationActivity;
    String NotiIDAdapter="";


    public NotificationAdapter(CustomerNotificationFragment mContext, ArrayList<Notification>mNoti){
        this.customerNotificationFragment=mContext;
        this.mNoti=mNoti;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(customerNotificationFragment.getActivity()).inflate(R.layout.item_notification_customer, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        noti = mNoti.get(position);
        holder.name.setText(noti.getName_Provide());
        holder.status.setText(noti.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(customerNotificationFragment.getActivity(), NotificationActivity.class);
                intent.putExtra("NotiID", noti.getNotiID());
                customerNotificationFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            status=itemView.findViewById(R.id.tv_Status);


        }
    }


    public String getNotiID()
    {
        return NotiIDAdapter;
    }



}
