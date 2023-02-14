package com.example.carrenting.Service.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VehicleDetailActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Vehicle> ls = new ArrayList<Vehicle>();
    Vehicle temp = new Vehicle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_fragment_home);

        recyclerView = (RecyclerView) findViewById(R.id.vehicle_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //VehicleAdapter adapter = new VehicleAdapter(VehicleDetailActivity.this, ls);
        //recyclerView.setAdapter(adapter);
        FirebaseFirestore dtb = FirebaseFirestore.getInstance();

        dtb.collection("Vehicles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        temp.setVehicle_id(document.getId());
                        temp.setVehicle_name(document.get("vehicle_name").toString());
                        temp.setVehicle_price(document.get("vehicle_price").toString());
                        temp.setVehicle_imageURL(document.get("imageURL").toString());
                        temp.setVehicle_availability(document.get("availability").toString());
                        temp.setVehicle_number(document.get("plate_number").toString());
                        temp.setVehicle_seats(document.get("seats").toString());
                        temp.setOwner_name(document.get("owner_name").toString());
                        ls.add(temp);
                    }
                } else {
                    Toast.makeText(VehicleDetailActivity.this, "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}