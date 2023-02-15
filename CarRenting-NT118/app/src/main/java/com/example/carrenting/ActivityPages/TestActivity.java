package com.example.carrenting.ActivityPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TestActivity extends AppCompatActivity {

    FirebaseFirestore dtb;
    String id, owner, location, phone, time, Car, imageuri;
    private ImageView imgCar;
    private ConstraintLayout Layout;
    private TestActivity mMainActivity = this;
    TextView  textlocation, textphone, texttime, textCarname, textOwner;
    ImageView back_button, carimage;

    Vehicle temp = new Vehicle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car);
        Layout = findViewById(R.id.layout);
        back_button = findViewById(R.id.image_back);

        carimage = findViewById(R.id.image_car);
        textOwner = findViewById(R.id.tv_way);
        textCarname = findViewById(R.id.tv_car_name);
        textlocation = findViewById(R.id.tv_location);
        textphone = findViewById(R.id.tv_phone);
        texttime = findViewById(R.id.tv_schedule_time);

        dtb = FirebaseFirestore.getInstance();
        dtb.collection("Vehicles")
                .whereEqualTo("vehicle_id", "123")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                temp.setVehicle_id(document.getId());
//                                temp.setOwner_name(document.get("owner").toString());
//                                temp.setVehicle_name(document.get("name").toString());
//                                temp.setOwner_address(document.get("address").toString());
//                                temp.setVehicle_availability(document.get("schedule").toString());
//                                temp.setOwner_phone(document.get("phone").toString());
//
//                                textCarname.setText(temp.getVehicle_name());
//                                textOwner.setText(temp.getOwner_name());
//                                textlocation.setText(temp.getOwner_address());
//                                textphone.setText(temp.getOwner_phone());
//                                texttime.setText(temp.getVehicle_availability());


                                Toast.makeText(TestActivity.this, "Success", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(TestActivity.this, "Error getting documents: ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}