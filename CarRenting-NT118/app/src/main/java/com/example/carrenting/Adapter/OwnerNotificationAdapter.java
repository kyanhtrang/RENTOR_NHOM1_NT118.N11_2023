package com.example.carrenting.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Owner.OwnerNotificationsFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.example.carrenting.Service.Notification.NotificationActivity;
import com.example.carrenting.Service.Notification.OwnerNotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OwnerNotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    OwnerNotificationsFragment ownerNotificationsFragment;
    Notification noti;
    ArrayList<Notification> mNoti;
    NotificationActivity notificationActivity;
    String NotiIDAdapter="", CustomerID, Name;
    FirebaseFirestore dtb;


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
        dtb = FirebaseFirestore.getInstance();
        CustomerID=noti.getCustomerID();
        getuser(CustomerID);
        holder.name.setText(Name);

        holder.id.setText(noti.getNotiID());

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
    private void getuser(String Customerid){
        dtb.collection("Users")
                .whereEqualTo("user_id",Customerid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = new User();
                                user.setUser_id(document.get("user_id").toString());
                                user.setUsername(document.get("username").toString());

                                Name=user.getUsername();
                            }
                        } else {

                        }
                    }
                });
    }


    public String getNotiID()
    {
        return NotiIDAdapter;
    }


}
