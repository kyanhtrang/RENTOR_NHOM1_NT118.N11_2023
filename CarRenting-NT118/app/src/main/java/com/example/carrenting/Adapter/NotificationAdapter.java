package com.example.carrenting.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    CustomerNotificationFragment customerNotificationFragment;
    Notification noti;
    ArrayList<Notification> mNoti;


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

        noti=mNoti.get(position);
        holder.name.setText(noti.getName_Provide());
        holder.status.setText(noti.getStatus());
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

//
//    @NonNull
//    @Override
//    public VehicleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(customerHomeFragment.getActivity()).inflate(R.layout.vehicle_card, parent, false);
//
//        return new VehicleAdapter.MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        Notification notification = mNoti.get(position);
////        holder.username.setText(notification.getName_Provide());
//        holder.status.setText(notification.getStatus());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, NotificationActivity.class);
//                intent.putExtra("provideID",notification.getProvideID());
//                mContext.startActivities(new Intent[]{intent});
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNoti.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        public TextView username;
//        public TextView status;
//        public  ViewHolder(View itemView){
//            super(itemView);
//
//            username = itemView.findViewById(R.id.tv_name);
//            status = itemView.findViewById(R.id.tv_Status);
//
//        }
//    }
































//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_customer, parent, false);
////
////        return new NotificationAdapter.ViewHolder(view);
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.item_notification_customer,null);
//        return new ViewHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        Notification notification = mNoti.get(position);
//        holder.tv_Status.setText(notification.getStatus());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNoti.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
//
//        TextView tv_Status;
//        TextView tv_name;
//        ConstraintLayout card;
//        onNotiListener onNotiListener;
//        public ViewHolder(@NonNull View itemView, onNotiListener onNotiListener) {
//            super(itemView);
//            tv_name = itemView.findViewById(R.id.tv_provide_name);
//            tv_Status=itemView.findViewById(R.id.tv_Status);
//
//            this.onNotiListener = onNotiListener;
//            itemView.setOnClickListener(this);
//        }
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tv_Status=itemView.findViewById(R.id.tv_Status);
//        }
//
//        @Override
//        public void onClick(View view) {
//        }
//
//
//    }
//    public interface onNotiListener{
//        void onClick(int position);
//    }







//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
}
