package com.example.carrenting.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Owner.OwnerNotificationsFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.R;
import com.example.carrenting.Service.Notification.NotificationActivity;
import com.example.carrenting.Service.Notification.OwnerNotificationActivity;

import java.util.ArrayList;

public class OwnerNotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    OwnerNotificationsFragment ownerNotificationsFragment;
    Notification noti;
    ArrayList<Notification> mNoti;
    NotificationActivity notificationActivity;
    String NotiIDAdapter="";


    public OwnerNotificationAdapter(OwnerNotificationsFragment mContext, ArrayList<Notification>mNoti){
        this.ownerNotificationsFragment=mContext;
        this.mNoti=mNoti;

    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ownerNotificationsFragment.getActivity()).inflate(R.layout.item_notification_customer, parent, false);
        return new NotificationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        noti = mNoti.get(position);
        holder.name.setText(noti.getName_Provide());
//        holder.status.setText(noti.getStatus());
        if(noti.getStatus().equals( "Dang cho"))
        {
            holder.status.setText("Đang chờ");
        }
        else
        {
            if(noti.getStatus().equals( "Xac nhan"))
            {
                holder.status.setText("Nhà cung cấp đã xác nhận");
            }
            else
            {
                holder.status.setText("Nhà cung cấp không xác nhận");
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ownerNotificationsFragment.getActivity(), OwnerNotificationActivity.class);
                intent.putExtra("NotiID", noti.getNotiID());
                ownerNotificationsFragment.startActivity(intent);
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
