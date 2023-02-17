package com.example.carrenting.Service.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.example.carrenting.Service.Booking.WriteInformationCheckoutActivity;
import com.example.carrenting.Service.Notification.NotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehicleDetailActivity extends AppCompatActivity {

    private ImageView vehicleImage;
    private TextView providerName, providerGmail, providerPhone, providerAddress;
    private TextView vehicleName, vehiclePrice, vehicleNumber, vehicleSeats, vehicleOwner;
    private Button btnBook;

    private String vehicleID;

    private FirebaseFirestore dtb_vehicle;
    private Vehicle vehicle = new Vehicle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car);

        Intent intent = getIntent();
        vehicleID = intent.getStringExtra("vehicle_id");
        vehicle.setVehicle_id(vehicleID);

        init();
        getDetail();

        FirebaseFirestore dtb = FirebaseFirestore.getInstance();
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VehicleDetailActivity.this, WriteInformationCheckoutActivity.class);
                i.putExtra("vehicle_id", vehicleID);
                startActivity(i);
            }
        });

    }

    private void init()
    {
        btnBook = findViewById(R.id.btn_book);

        vehicleImage = findViewById(R.id.vehicle_img);
        providerName = findViewById(R.id.tv_provider_name);
        providerGmail = findViewById(R.id.tv_provider_gmail);
        providerAddress = findViewById(R.id.tv_provider_address);
        providerPhone = findViewById(R.id.tv_provider_phone);

        vehicleName = findViewById(R.id.tv_vehicle_name);
        vehicleNumber = findViewById(R.id.tv_vehicle_number);
        vehicleSeats = findViewById(R.id.tv_vehicle_seats);
        vehiclePrice = findViewById(R.id.tv_vehicle_price);
        vehicleOwner = findViewById(R.id.tv_vehicle_owner);

        dtb_vehicle = FirebaseFirestore.getInstance();
    }

    private void getDetail() {
        dtb_vehicle.collection("Vehicles")
                .whereEqualTo("vehicle_id", vehicle.getVehicle_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                    vehicle.setProvider_id(document.get("provider_id").toString());
                                    vehicle.setVehicle_id(document.get("vehicle_id").toString());
                                    vehicle.setVehicle_availability(document.get("vehicle_availability").toString());

                                    vehicle.setProvider_name(document.get("provider_name").toString());
                                    providerName.setText(vehicle.getProvider_name());

                                    vehicle.setProvider_phone(document.get("provider_phone").toString());
                                    providerPhone.setText(vehicle.getProvider_phone());

                                    vehicle.setProvider_address(document.get("provider_address").toString());
                                    providerAddress.setText(vehicle.getProvider_address());

                                    vehicle.setProvider_gmail(document.get("provider_gmail").toString());
                                    providerGmail.setText(vehicle.getProvider_gmail());

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