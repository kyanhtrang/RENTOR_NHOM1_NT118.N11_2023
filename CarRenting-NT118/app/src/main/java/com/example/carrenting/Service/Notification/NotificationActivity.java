package com.example.carrenting.Service.Notification;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.Model.Order;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.example.carrenting.Service.Vehicle.VehicleDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    DatabaseReference reference;
<<<<<<< Updated upstream
    FirebaseFirestore dtb;
=======
    FirebaseFirestore dtb, dtb_vehicle;
>>>>>>> Stashed changes
    Intent intent;
    String ProvideID, vehicle_id;
    String NotiID;
    FragmentManager fragmentManager;

    private CustomerNotificationFragment customerNotificationFragment;


    private ArrayList<Vehicle> ls = new ArrayList<Vehicle>();
    private TextView tv_id,name,email,phoneNumber, tv_status;// Thông tin nhà cung cấp
    private TextView tv_BrandCar,tv_Gia,tv_DiaDiem,pickup,dropoff,totalCost;// Thông tin xe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_custormer);
        diachi();

        intent=getIntent();

        String OrderID=intent.getStringExtra("OrderID");

        dtb=FirebaseFirestore.getInstance();

<<<<<<< Updated upstream
        dtb.collection("Order")
                .whereEqualTo("OrderID", OrderID)
=======
        init();
        customerNotificationFragment.getActivity();

        NotiID=customerNotificationFragment.getNotiID();
//        intent = getIntent();
//        NotiID = intent.getStringExtra("NotiID");
        if(NotiID==null)
        {
            Toast.makeText(NotificationActivity.this, "Không thể lấy NotiID", Toast.LENGTH_SHORT).show();
        }

        dtb = FirebaseFirestore.getInstance();
        dtb.collection("Notification")
                .whereEqualTo("NotiID", NotiID)
>>>>>>> Stashed changes
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

<<<<<<< Updated upstream
                                ProvideID=document.get("ProvideID").toString();
                                vehicle_id=document.get("vehicle_id").toString();
=======
                                Notification temp = new Notification();
                                temp.setNotiID(document.getId());
                                temp.setProvideID(document.get("ProvideID").toString());
                                temp.setVehicle_id(document.get("vehicle_id").toString());
                                ProvideID=temp.getProvideID();
                                vehicle_id=temp.getVehicle_id();
>>>>>>> Stashed changes

                                Toast.makeText(NotificationActivity.this, temp.getVehicle_id(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NotificationActivity.this, "Không thể lấy thông tin noti", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

<<<<<<< Updated upstream
        dtb.collection("Vehicles")
=======
        dtb_vehicle = FirebaseFirestore.getInstance();
        dtb_vehicle.collection("Vehicles")
>>>>>>> Stashed changes
                .whereEqualTo("vehicle_id", vehicle_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

<<<<<<< Updated upstream

=======
                                Vehicle temp = new Vehicle();
                                temp.setVehicle_id(document.getId());
                                temp.setOwner_name(document.get("owner").toString());
                                temp.setVehicle_name(document.get("name").toString());
                                temp.setOwner_address(document.get("address").toString());
                                temp.setVehicle_availability(document.get("schedule").toString());
                                temp.setOwner_phone(document.get("phone").toString());
                                tv_BrandCar.setText(temp.getVehicle_name());
                                tv_Gia.setText(temp.getVehicle_price());
                                Toast.makeText(NotificationActivity.this, temp.getVehicle_id(), Toast.LENGTH_LONG).show();
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    public void diachi(){
        tv_id=findViewById(R.id.tv_id);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phoneNumber);
        tv_BrandCar=findViewById(R.id.tv_BrandCar);
        tv_DiaDiem=findViewById(R.id.tv_DiaDiem);
        tv_Gia=findViewById(R.id.tv_Gia);
        pickup=findViewById(R.id.pickup);
        dropoff=findViewById(R.id.dropoff);
        totalCost=findViewById(R.id.totalCost);
        tv_status=findViewById(R.id.tv_status);
=======
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

//        fragmentManager=getSupportFragmentManager();
//        customerNotificationFragment= (CustomerNotificationFragment) fragmentManager.findFragmentById(R.id.frame_layout_customer);
    }
    public String getNotiID(String noti)
    {
        NotiID=noti;
        return NotiID;
>>>>>>> Stashed changes
    }
}
