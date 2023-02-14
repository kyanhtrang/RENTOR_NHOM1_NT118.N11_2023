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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.Adapter.VehicleAdapter;
import com.example.carrenting.Model.RecyclerViewOnClickSupport;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.example.carrenting.Service.Map.MapMainActivity;
import com.example.carrenting.Service.Vehicle.VehicleDetailActivity;
import com.example.carrenting.databinding.ActivityDetailCarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CustomerHomeFragment extends Fragment {

    ImageView imageView;
    DocumentReference imageRef;
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

        try {
            EventChangeListener();
            //Toast.makeText(getContext(),"Catching...", Toast.LENGTH_LONG).show();
        } catch (Exception exception){
            Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
        }
        RecyclerViewOnClickSupport.addTo(recyclerView).setOnItemLongClickListener(new RecyclerViewOnClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                return true;
            }
        });
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
        dtb_vehicle.collection("Vehicles")
                .orderBy("vehicle_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Vehicle temp = new Vehicle();
                                temp.setVehicle_id(document.getId());
                                temp.setVehicle_name(document.get("vehicle_name").toString());
                                temp.setVehicle_price(document.get("vehicle_price").toString());
                                temp.setVehicle_imageURL(document.get("vehicle_imageURL").toString());
                                /*temp.setVehicle_availability(document.get("availability").toString());
                                temp.setVehicle_number(document.get("plate_number").toString());
                                temp.setVehicle_seats(document.get("seats").toString());
                                temp.setOwner_name(document.get("owner_name").toString());*/
                                vehicles.add(temp);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
