package com.example.carrenting.Service.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VehicleCardActivity extends AppCompatActivity {

    FirebaseFirestore dtb;
    String id;
    private ImageView imgCar;
    private VehicleCardActivity mMainActivity = this;
    private FirebaseStorage storage;
    TextView vehicle_name, vehicle_price;

    Vehicle temp = new Vehicle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_card);

        StorageReference storageRef = storage.getReference();


        vehicle_name = findViewById(R.id.vehicle_name);
        vehicle_price = findViewById(R.id.tv_vehicle_price);
        imgCar = findViewById(R.id.img_vehicle);

        dtb = FirebaseFirestore.getInstance();
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

                                Picasso.get().load(temp.getVehicle_imageURL()).into(imgCar);

                                vehicle_name.setText(temp.getVehicle_name());
                                vehicle_price.setText(temp.getVehicle_price());

                            }
                        } else {
                            Toast.makeText(VehicleCardActivity.this, "Error getting documents ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}