package com.example.carrenting.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Owner.OwnerNotificationsFragment;
import com.example.carrenting.Model.Activity;

import com.example.carrenting.R;
import com.example.carrenting.Service.Activity.OwnerActivityDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OwnerNotificationAdapter extends RecyclerView.Adapter<OwnerNotificationAdapter.MyViewHolder>{
//    OwnerActivityFragment ownerActivityFragment;
    OwnerNotificationsFragment ownerNotificationsFragment;
    Activity noti;
    ArrayList<Activity> mNoti;

    String CustomerID, Name;
    FirebaseFirestore dtb;



    public OwnerNotificationAdapter(OwnerNotificationsFragment mContext, ArrayList<Activity>mNoti){
        this.ownerNotificationsFragment=mContext;
        this.mNoti=mNoti;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ownerNotificationsFragment.getActivity()).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        noti = mNoti.get(position);


        holder.status.setText("Bạn vừa nhận 1 yêu cầu");

    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  status;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            status=itemView.findViewById(R.id.tv_noti_text);




        }
    }





}
