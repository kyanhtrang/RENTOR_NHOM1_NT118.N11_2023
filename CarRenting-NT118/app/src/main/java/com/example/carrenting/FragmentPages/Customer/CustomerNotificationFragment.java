package com.example.carrenting.FragmentPages.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.Adapter.NotificationAdapter;
import com.example.carrenting.Model.Activity;
import com.example.carrenting.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobsandgeeks.saripaar.adapter.TextViewStringAdapter;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;


public class CustomerNotificationFragment extends Fragment {
    ArrayList<String> listtitle = new ArrayList<String>();
    ArrayList<String> listcontent = new ArrayList<String>();
    private RecyclerView recyclerView;
    NotificationAdapter adapter;
    FirebaseFirestore dtb;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment_notification, container, false);

        dtb = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.frame_layout_noti);
        recyclerView.setHasFixedSize(true);

        listtitle.add("Bạn có 1 thông báo mới");
        listcontent.add("Thông báo abc");

        recyclerView.setAdapter(adapter);
        adapter = new NotificationAdapter(CustomerNotificationFragment.this, listtitle, listcontent);

        listtitle.add("Bạn có 1 thông báo mới");
        listcontent.add("Thông báo abc");

        getnoti();

        listtitle.add("Bạn có 1 thông báo mới");
        listcontent.add("Thông báo abc");

        return view;
    }

    private void getnoti(){
        dtb.collection("Notification");
        adapter.notifyDataSetChanged();
    }

}