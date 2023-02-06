package com.example.carrenting.FragmentPages.Customer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carrenting.Adapter.VehicleAdapter;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.example.carrenting.Service.Map.MapMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CustomerHomeFragment extends Fragment {

    ImageView imageView;
    DocumentReference imageRef;
    String documentId;
    RecyclerView recyclerView;
    ArrayList<Vehicle> vehicles;
    VehicleAdapter adapter;
    FirebaseFirestore dtb_vehicle;
    ProgressDialog progressDialog;

    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.customer_fragment_home, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang lấy dữ liệu...");
        progressDialog.show();
        recyclerView = mView.findViewById(R.id.vehicle_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dtb_vehicle = FirebaseFirestore.getInstance();
        vehicles = new ArrayList<Vehicle>();
        adapter = new VehicleAdapter(CustomerHomeFragment.this, vehicles);

        recyclerView.setAdapter(adapter);
        EventChangeListener();
        progressDialog.dismiss();
        return  mView;
    }


    private void initUI() {
        /*btnMap =  mView.findViewById(R.id.locate_card);*/
    }

    private void LoadImage(String docId) {
        if (docId == null) {
            Log.e("LoadImage", "docId is null, returning");
            return;
        }
        // Reference to the image document in Firestore
        imageRef = FirebaseFirestore.getInstance().collection("Image").document(docId);

        // Get the image document
        imageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Get the image URL from the document
                    String imageUrl = documentSnapshot.getString("Image");

                    // Load the image into the ImageView
                    Glide.with(getView()).load(imageUrl).into(imageView);
                }else{
                    Log.e("LoadImage", "documentSnapshot is null or does not exists");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LoadImage", "Error: " + e.getMessage());
            }
        });
    }

    private void EventChangeListener()
    {
        dtb_vehicle.collection("Image").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            Log.e("Lỗi", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                vehicles.add(dc.getDocument().toObject(Vehicle.class));
                            }

                            adapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}
