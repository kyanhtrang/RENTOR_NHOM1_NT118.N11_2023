package com.example.carrenting.Service.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

    }
    private void init()
    {
        btnUpdate = findViewById(R.id.btn_updatevehicle);
// -------------------------------------------------
        vehicleName = findViewById(R.id.et_vehiclename);
        vehicleNumber = findViewById(R.id.et_platenumber);
        vehicleSeats = findViewById(R.id.et_vehicleseats);
        vehiclePrice = findViewById(R.id.et_vehicleprice);
        vehicleOwner = findViewById(R.id.et_vehicleowner);
//--------------------------------------------------
        vehicleImage = findViewById(R.id.img_view);
//--------------------------------------------------
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
    private void update(){
        Map<String, Object> data = new HashMap<>();
        Boolean flag = false;
        String nameupdate = vehicleName.getText().toString();
        String platenumber = vehicleNumber.getText().toString();
        String seats = vehicleSeats.getText().toString();
        String price = vehiclePrice.getText().toString();
        String ownername = vehicleOwner.getText().toString();

        if (!nameupdate.equals(vehicle.getVehicle_name())){
            data.put("vehicle_name", nameupdate);
            flag = true;
        }
        if (!platenumber.equals(vehicle.getVehicle_number())){
            data.put("vehicle_number",platenumber);
            flag = true;
        }
        if (!seats.equals(vehicle.getVehicle_seats())){
            data.put("vehicle_seats", seats);
            flag = true;
        }
        if (!price.equals(vehicle.getVehicle_price())){
            data.put("vehicle_price", price);
            flag = true;
        }
        if (!ownername.equals(vehicle.getOwner_name())){
            data.put("owner_name", ownername);
            flag = true;
        }
        if (flag) {
            dtb_vehicle.collection("Vehicles")
                    .document(vehicleID)
                    .set(data, SetOptions.merge())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateVehicle.this, "Không thể cập nhật thông tin", Toast.LENGTH_LONG).show();
                            return;
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UpdateVehicle.this, "Câp nhật thông tin thành công", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        }
    }
}