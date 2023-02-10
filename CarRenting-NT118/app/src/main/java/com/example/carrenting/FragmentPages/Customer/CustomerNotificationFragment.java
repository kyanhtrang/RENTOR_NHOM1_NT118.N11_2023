package com.example.carrenting.FragmentPages.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.Adapter.NotificationAdapter;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.example.carrenting.Service.Notification.NotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CustomerNotificationFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    ArrayList<Notification> notifications;
    FirebaseFirestore dtb_noti;
<<<<<<< Updated upstream


=======
    ProgressDialog progressDialog;
    FirebaseUser user;
    String NotiID="3";
>>>>>>> Stashed changes

    NotificationActivity notificationActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.frame_layout_noti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mNoti=new ArrayList<>();

<<<<<<< Updated upstream
        dtb_noti=FirebaseFirestore.getInstance();
=======
//        user = FirebaseAuth.getInstance().getCurrentUser();
        dtb_noti = FirebaseFirestore.getInstance();
>>>>>>> Stashed changes
        notifications = new ArrayList<Notification>();
        notificationAdapter=new NotificationAdapter(CustomerNotificationFragment.this,notifications);
        recyclerView.setAdapter(notificationAdapter);
//        readNotification();

        EventChangeListener();

<<<<<<< Updated upstream


=======
>>>>>>> Stashed changes
//        return inflater.inflate(R.layout.customer_fragment_notification, container, false);
        return view;
    }


    private void EventChangeListener()
    {

        dtb_noti.collection("Notification")
//                .orderBy("ProvideID", Query.Direction.ASCENDING)
//                .WhereEqualTo("CustomerID","2")

                .whereEqualTo("CustomerID", "2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Notification temp = new Notification();
                                temp.setNotiID(document.get("NotiID").toString());
                                temp.setProvideID(document.get("ProvideID").toString());
                                temp.setName_Provide(document.get("Name_Provide").toString());
                                temp.setCustomerID(document.get("CustomerID").toString());
                                temp.setName_customer(document.get("Name_customer").toString());
                                temp.setStatus(document.get("Status").toString());


                                notifications.add(temp);
                                notificationAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Không thể lấy thông tin đơn hàng ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public String getNotiID()
    {

//        String notiID = notificationAdapter.getNotiID();
//        NotiID = notiID;
        return NotiID;
    }



}