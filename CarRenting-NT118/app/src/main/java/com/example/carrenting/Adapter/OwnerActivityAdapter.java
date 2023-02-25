package com.example.carrenting.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Owner.OwnerActivityFragment;
import com.example.carrenting.Model.Activity;

import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.example.carrenting.Service.Activity.OwnerActivityDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OwnerActivityAdapter extends RecyclerView.Adapter<OwnerActivityAdapter.MyViewHolder>{
    OwnerActivityFragment ownerActivityFragment;
    Activity noti;
    ArrayList<Activity> mNoti;

    String CustomerID, Name;
    FirebaseFirestore dtb;

    public OwnerActivityAdapter(OwnerActivityFragment mContext, ArrayList<Activity>mNoti){
        this.ownerActivityFragment=mContext;
        this.mNoti=mNoti;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ownerActivityFragment.getActivity()).inflate(R.layout.item_activity, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        noti = mNoti.get(position);
        dtb = FirebaseFirestore.getInstance();
        CustomerID=noti.getCustomer_id();
        getuser(CustomerID);
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
            else
            if (noti.getStatus().equals("Da thanh toan"))
            {
                holder.status.setText("Đã thanh toán");
            }
            else {
                holder.status.setText("Đã xác nhận");
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ownerActivityFragment.getActivity(), OwnerActivityDetail.class);
                intent.putExtra("NotiID", noti.getNoti_id());
                ownerActivityFragment.startActivity(intent);
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




}
