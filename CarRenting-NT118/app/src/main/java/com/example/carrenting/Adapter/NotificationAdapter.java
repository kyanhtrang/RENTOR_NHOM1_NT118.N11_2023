package com.example.carrenting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.Model.Activity;
import com.example.carrenting.R;
import com.example.carrenting.Service.Activity.CustomerActivityDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private CustomerNotificationFragment customerNotificationFragment;
//    private ArrayList<String> title, content;
    Activity noti;
    ArrayList<Activity> mNoti;
    FirebaseFirestore dtb;

//    public NotificationAdapter(ArrayList<String> a, ArrayList<String> b) {
//        this.title = a;
//        this.content = b;
//    }
    public NotificationAdapter(CustomerNotificationFragment mContext, ArrayList<Activity> mNoti){
        this.customerNotificationFragment = mContext;
        this.mNoti=mNoti;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(customerNotificationFragment.getActivity()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        noti = mNoti.get(position);

        if(noti.getStatus().equals( "Xac nhan"))
        {
            holder.status.setText("Nhà cung cấp đã xác nhận");
        }
        else
        {
//            if(noti.getStatus().equals( "Khong xac nhan"))
//            {
            holder.status.setText("Nhà cung cấp không xác nhận");
//            }

        }



    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }
    public  class  NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView status;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.tv_noti_text);

        }
    }


}
