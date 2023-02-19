package com.example.carrenting.Service.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrenting.ActivityPages.OwnerMainActivity;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.Model.User;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OwnerActivityDetail extends AppCompatActivity {

    FirebaseFirestore dtb;
    Intent intent;
    String CustomerID, vehicle_id;
    String NotiID,noti_status;
    ImageView vehicleImage;
    private Notification temp = new Notification();


    private ArrayList<Vehicle> ls = new ArrayList<Vehicle>();
    private TextView tv_id,name,email,phoneNumber, tv_status;// Thông tin nhà cung cấp
    private TextView tv_BrandCar,tv_Gia,tv_DiaDiem,pickup,dropoff,totalCost;// Thông tin xe
    private Button btn_xacnhan,btn_huy,btn_back;

//    APIService apiService;
//
//    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_provide);
        intent = getIntent();

        String OrderID = intent.getStringExtra("NotiID");
        NotiID = OrderID;

        init();

        dtb = FirebaseFirestore.getInstance();
        dtb.collection("Notification")
                .whereEqualTo("noti_id", NotiID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                            Notification temp = new Notification();
                                temp.setNoti_id(document.getId());
                                temp.setCustomer_id(document.get("customer_id").toString());
                                temp.setVehicle_id(document.get("vehicle_id").toString());
                                temp.setStatus(document.get("status").toString());

                                CustomerID = temp.getCustomer_id();
                                vehicle_id = temp.getVehicle_id();
                                noti_status=temp.getStatus();

                                tv_id.setText(NotiID);

                                if(noti_status.equals( "Dang cho"))
                                {
                                    tv_status.setText("Đang chờ");
                                }
                                else
                                {
                                    if(noti_status.equals( "Xac nhan"))
                                    {
                                        tv_status.setText("Đã xác nhận");
                                    }
                                    else
                                    {
                                        tv_status.setText("Không được xác nhận");
                                    }

                                }
                                getuser(CustomerID);
                                getvehicle(vehicle_id);

                            }
                        } else {
                            Toast.makeText(OwnerActivityDetail.this, "Không thể lấy thông báo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerActivityDetail.this, OwnerMainActivity.class);
                startActivity(intent);
            }
        });
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_noti_huy();
            }
        });

        btn_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update_noti_xacnhan();
            }
        });
    }

    private void getuser(String ProvideID){
        dtb.collection("Users")
                .whereEqualTo("user_id", CustomerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = new User();
                                user.setUser_id(document.get("user_id").toString());
                                user.setUsername(document.get("username").toString());
                                user.setEmail(document.get("email").toString());
                                user.setPhoneNumber(document.get("phoneNumber").toString());
                                name.setText(user.getUsername());
                                email.setText(user.getEmail());
                                phoneNumber.setText(user.getPhoneNumber());
                            }
                        } else {
                            Toast.makeText(OwnerActivityDetail.this, "Không thể lấy thông tin nhà cung cấp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void getvehicle(String vehicle_id){
        dtb.collection("Vehicles")
                .whereEqualTo("vehicle_id", vehicle_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Vehicle temp = new Vehicle();
                                temp.setVehicle_id(document.getId());
                                temp.setVehicle_name(document.get("vehicle_name").toString());
                                temp.setVehicle_availability(document.get("vehicle_availability").toString());
                                temp.setVehicle_price(document.get("vehicle_price").toString());


                                tv_BrandCar.setText(temp.getVehicle_name());
                                tv_Gia.setText(temp.getVehicle_price() + " Đ /ngày");
                                tv_DiaDiem.setText(temp.getProvider_address());
                                temp.setProvider_address(document.get("provider_address").toString());

                                temp.setVehicle_imageURL(document.get("vehicle_imageURL").toString());
                                if (!document.get("vehicle_imageURL").toString().isEmpty()) {
                                    Picasso.get().load(temp.getVehicle_imageURL()).into(vehicleImage);
                                }
                                else {
                                    temp.setVehicle_imageURL("");
                                }
                            }
                        } else {
                            Toast.makeText(OwnerActivityDetail.this, "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void update_noti_huy(){

        Map<String, Object> data = new HashMap<>();
        data.put("status", "Khong xac nhan");
        dtb.collection("Notification").document(temp.getNoti_id()).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OwnerActivityDetail.this, "Đã hủy đơn hàng", Toast.LENGTH_LONG).show();
                        tv_status.setText("Không được xác nhận");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OwnerActivityDetail.this, "Lỗi hủy đơn hàng", Toast.LENGTH_LONG).show();
                    }
                });

    }
    private void update_noti_xacnhan(){

        Map<String, Object> data = new HashMap<>();
        data.put("status", "Xac nhan");
        dtb.collection("Notification").document(temp.getNoti_id()).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OwnerActivityDetail.this, "Đã xác nhận", Toast.LENGTH_LONG).show();
                        tv_status.setText("Đã xác nhận");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OwnerActivityDetail.this, "Lỗi hủy đơn hàng", Toast.LENGTH_LONG).show();
                    }
                });

    }


    public void init(){
        tv_id=findViewById(R.id.txtview_noti_id);
        email=findViewById(R.id.txtview_noti_email);
        name=findViewById(R.id.txtview_noti_name);
        phoneNumber=findViewById(R.id.txtview_noti_phoneNumber);
        tv_BrandCar=findViewById(R.id.txtview_noti_BrandCar);
        tv_DiaDiem=findViewById(R.id.txt_checkout_address);

        tv_Gia=findViewById(R.id.txtview_noti_price);
        pickup=findViewById(R.id.tv_noti_pickup);
        dropoff=findViewById(R.id.tv_noti_dropoff);
        totalCost=findViewById(R.id.txtview_noti_totalCost);
        tv_status=findViewById(R.id.txtview_noti_status);

        btn_xacnhan=findViewById(R.id.btn_noti_XacNhan);
        btn_huy=findViewById(R.id.btn_noti_huy);
        btn_back=findViewById(R.id.btn_noti_back);
        vehicleImage=findViewById(R.id.img_noti_car);
    }
}
