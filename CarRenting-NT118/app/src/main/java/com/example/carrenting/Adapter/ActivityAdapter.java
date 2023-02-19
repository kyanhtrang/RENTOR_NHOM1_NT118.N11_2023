package com.example.carrenting.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Customer.CustomerActivityFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.example.carrenting.Service.Activity.CustomerActivityDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder>{

    CustomerActivityFragment customerActivityFragment;
    Notification noti;
    ArrayList<Notification> mNoti;

    FirebaseFirestore dtb;
    String Name, ProvideID;


    public ActivityAdapter(CustomerActivityFragment mContext, ArrayList<Notification>mNoti){
        this.customerActivityFragment=mContext;
        this.mNoti=mNoti;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(customerActivityFragment.getActivity()).inflate(R.layout.item_notification_customer, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        noti = mNoti.get(position);

        dtb = FirebaseFirestore.getInstance();
        ProvideID=noti.getProvider_id();
        getuser(ProvideID);
        holder.name.setText(Name);
        holder.id.setText(noti.getNoti_id());


        if(noti.getStatus().equals( "Dang cho"))
        {
            holder.status.setText("Nhà cung cấp chưa xác nhận");
        }
        else
        {
            if(noti.getStatus().equals( "Thanh toan"))
            {
                holder.status.setText("Đang chờ thanh toán");
            }
            else
            if (noti.getStatus().equals("Khong xac nhan"))
            {
                holder.status.setText("Nhà cung cấp không xác nhận");
            }
            else {
                holder.status.setText("Đã xác nhận thuê xe");
            }

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(customerActivityFragment.getActivity(), CustomerActivityDetail.class);
                intent.putExtra("NotiID", noti.getNoti_id());
                customerActivityFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, status,id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_noti_name);
            status=itemView.findViewById(R.id.tv_Status);
            id=itemView.findViewById(R.id.tv_noti_ID);


        }
    }

    private void getuser(String ProvideID){
        dtb.collection("Users")
                .whereEqualTo("user_id", ProvideID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = new User();
                                user.setUser_id(document.get("user_id").toString());
                                user.setUsername(document.get("username").toString());
                                user.setEmail(document.get("email").toString());
                                user.setPhoneNumber(document.get("phoneNumber").toString());
                                Name=user.getUsername();
                            }
                        } else {

                        }
                    }
                });
    }




}
