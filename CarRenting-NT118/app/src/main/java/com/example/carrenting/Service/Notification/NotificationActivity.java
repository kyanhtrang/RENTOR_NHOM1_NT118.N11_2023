package com.example.carrenting.Service.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseFirestore dtb, dtbVehicle;
    Intent intent;
    String ProvideID, vehicle_id;
    private ArrayList<Vehicle> ls = new ArrayList<Vehicle>();
    private TextView tv_id,name,email,phoneNumber, tv_status;// Thông tin nhà cung cấp
    private TextView tv_BrandCar,tv_Gia,tv_DiaDiem,pickup,dropoff,totalCost;// Thông tin xe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_custormer);
        init();

        intent = getIntent();

        String OrderID = intent.getStringExtra("OrderID");
        dtb = FirebaseFirestore.getInstance();
        dtb.collection("Order")
                .whereEqualTo("OrderID", OrderID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ProvideID = document.get("ProvideID").toString();
                                vehicle_id = document.get("vehicle_id").toString();
                                tv_id.setText(ProvideID);

                            }
                        } else {
                            Toast.makeText(NotificationActivity.this, "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dtbVehicle = FirebaseFirestore.getInstance();
        dtbVehicle.collection("Vehicles")
                .whereEqualTo("vehicle_id", vehicle_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                private TextView tv_BrandCar,tv_Gia,tv_DiaDiem,pickup,dropoff,totalCost;// Thông tin xe
                                tv_BrandCar.setText(document.get("vehicle_name").toString());
                                tv_Gia.setText(document.get("vehicle_price").toString());
                                totalCost.setText(document.get("vehicle_price").toString());
                                Toast.makeText(NotificationActivity.this, tv_BrandCar + " " + tv_Gia + " " + totalCost, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(NotificationActivity.this, "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





//        noti= FirebaseAuth.getInstance().getCurrentUser();


//        reference= FirebaseDatabase.getInstance().getReference("Notification").child(ProvideID);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
//                Notification notification= datasnapshot.getValue(Notification.class);
//                tv_status.setText(notification.getStatus());
//                tv_name.setText(notification.getName_Provide());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
    public void init(){
        tv_id=findViewById(R.id.txtview_noti_id);
        email=findViewById(R.id.txtview_noti_email);
        name=findViewById(R.id.txtview_noti_name);
        phoneNumber=findViewById(R.id.txtview_noti_phoneNumber);
        tv_BrandCar=findViewById(R.id.txtview_noti_BrandCar);
        tv_DiaDiem=findViewById(R.id.txtview_noti_DiaDiem);
        tv_Gia=findViewById(R.id.txtview_noti_Gia);
        pickup=findViewById(R.id.txtview_noti_pickup);
        dropoff=findViewById(R.id.txtview_noti_dropoff);
        totalCost=findViewById(R.id.txtview_noti_totalCost);
        tv_status=findViewById(R.id.txtview_noti_status);
    }


}
