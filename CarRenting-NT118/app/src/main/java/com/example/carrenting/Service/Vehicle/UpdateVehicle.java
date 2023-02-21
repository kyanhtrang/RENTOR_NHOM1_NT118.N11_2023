package com.example.carrenting.Service.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class UpdateVehicle extends AppCompatActivity {

    private ImageView vehicleImage;
    private TextView vehicleName, vehiclePrice, vehicleNumber, vehicleSeats, vehicleOwner;
    private Button btnUpdate;

    private String vehicleID;

    private FirebaseFirestore dtb_vehicle;
    private Vehicle vehicle = new Vehicle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle);

        Intent intent = getIntent();
        vehicleID = intent.getStringExtra("vehicle_id");
        vehicle.setVehicle_id(vehicleID);

        init();

        getDetail();

    }

    private void init()
    {
        btnUpdate = findViewById(R.id.btn_update);

        vehicleName = findViewById(R.id.et_name);
        vehicleNumber = findViewById(R.id.et_number);
        vehicleSeats = findViewById(R.id.et_seats);
        vehiclePrice = findViewById(R.id.et_price);
        vehicleOwner = findViewById(R.id.et_owner);

        vehicleImage = findViewById(R.id.img_view);

        dtb_vehicle = FirebaseFirestore.getInstance();
    }

    private void getDetail() {
        dtb_vehicle.collection("Vehicles")
                .whereEqualTo("vehicle_id", vehicle.getVehicle_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                vehicle.setProvider_id(document.get("provider_id").toString());
                                vehicle.setVehicle_id(document.get("vehicle_id").toString());
                                vehicle.setVehicle_availability(document.get("vehicle_availability").toString());

                                vehicle.setVehicle_name(document.get("vehicle_name").toString());
                                vehicleName.setText(vehicle.getVehicle_name());

                                vehicle.setVehicle_price(document.get("vehicle_price").toString());
                                vehiclePrice.setText(vehicle.getVehicle_price());

                                vehicle.setVehicle_seats(document.get("vehicle_seats").toString());
                                vehicleSeats.setText(vehicle.getVehicle_seats());

                                vehicle.setOwner_name(document.get("owner_name").toString());
                                vehicleOwner.setText(vehicle.getOwner_name());

                                vehicle.setVehicle_number(document.get("vehicle_number").toString());
                                vehicleNumber.setText(vehicle.getVehicle_number());

                                vehicle.setVehicle_imageURL(document.get("vehicle_imageURL").toString());

                                if (!document.get("vehicle_imageURL").toString().isEmpty()) {
                                    vehicle.setVehicle_imageURL(document.get("vehicle_imageURL").toString());
                                    Picasso.get().load(vehicle.getVehicle_imageURL()).into(vehicleImage);
                                }
                                else {
                                    vehicle.setVehicle_imageURL("");
                                }

                            }

                        }
                    }
                });
    }
}