package com.example.carrenting.FragmentPages.Owner;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carrenting.Adapter.NotificationAdapter;
import com.example.carrenting.Adapter.OwnerNotificationAdapter;
import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.Model.Activity;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class OwnerNotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    //    NotificationAdapter notificationAdapter;
    OwnerNotificationAdapter ownerNotificationAdapter;
    ArrayList<Activity> notifications;
    FirebaseFirestore dtb_noti;
    ProgressDialog progressDialog;
    String current_user_id;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.frame_layout_noti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        storageReference = FirebaseStorage.getInstance().getReference();
        dtb_noti = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        notifications = new ArrayList<Activity>();
        ownerNotificationAdapter = new OwnerNotificationAdapter(OwnerNotificationsFragment.this,notifications);
        recyclerView.setAdapter(ownerNotificationAdapter);

        EventChangeListener();
        return view;
    }
    private void EventChangeListener()
    {

        dtb_noti.collection("Notification")
                .whereEqualTo("provider_id", current_user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Activity temp = new Activity();
                                temp.setNoti_id(document.get("noti_id").toString());
                                temp.setProvider_id(document.get("provider_id").toString());
                                temp.setCustomer_id(document.get("customer_id").toString());
                                temp.setStatus(document.get("status").toString());
                                temp.setVehicle_id(document.get("vehicle_id").toString());

                                notifications.add(temp);
                                ownerNotificationAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Không thể lấy thông tin đơn hàng ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}