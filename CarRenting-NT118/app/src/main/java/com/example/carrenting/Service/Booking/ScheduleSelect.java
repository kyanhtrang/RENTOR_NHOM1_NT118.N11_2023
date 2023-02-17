package com.example.carrenting.Service.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrenting.Model.Notification;
import com.example.carrenting.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ScheduleSelect extends AppCompatActivity {
    Button btn_request, btn_back;
    TextView NgayNhan, NgayTra;
    TextView GioNhan, GioTra;
    // Ngày giờ nhận
    Calendar Nhan;
    // Ngày giờ trả
    Calendar Tra;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    Intent intent;
    String vehicle_id;
    String ProvideID;
    FirebaseFirestore dtb_Vehicle, dtb_Noti,dtb_update;
    private Notification noti = new Notification();
    String current_user_id;
    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    //
//    APIService apiService;
//    boolean notify = false;
//    String provideID;
    //


    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_select);

        intent = getIntent();

        String Vehicle_ID = intent.getStringExtra("vehicle_id");
        vehicle_id = Vehicle_ID;
        toast("vehicle id: " + vehicle_id);

        storageReference = FirebaseStorage.getInstance().getReference();
        dtb_Vehicle = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();
        dtb_Noti = FirebaseFirestore.getInstance();
        dtb_update = FirebaseFirestore.getInstance();

        //
//        user= FirebaseAuth.getInstance().getCurrentUser();
//        apiService= Client.getClient("http:/fcm.googleapis.com/").create(APIService.class);
        //

        initComponents();

        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);



        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                notify=true;
                setNotiFirebase();
                Intent Writeinfor=new Intent(ScheduleSelect.this, RequestSuccessActivity.class);
                startActivity(Writeinfor);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Back_DetailCar=new Intent(ScheduleSelect.this, CarDetailActivity.class);
                intent.putExtra("vehicle_id", vehicle_id);
                startActivity(Back_DetailCar);
            }
        });
    }
    private void setNotiFirebase()
    {
        dtb_Vehicle.collection("Vehicles")
                .whereEqualTo("vehicle_id", vehicle_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                noti.setVehicle_id(document.get("vehicle_id").toString());
                                noti.setProvider_id(document.get("provider_id").toString());
                                noti.setStatus("Dang cho");
                                noti.setCustomer_id(current_user_id);
//                                provideID=noti.getProvider_id();

                                dtb_Noti.collection("Notification")
                                        .add(noti)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                noti.setNoti_id(documentReference.getId());
                                                Log.e("", noti.getNoti_id());
                                                updateData(noti.getNoti_id());
                                                Intent intent = new Intent(ScheduleSelect.this, RequestSuccessActivity.class);
                                                startActivity(intent);
                                                toast("Thêm noti thành công");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                toast("Thêm noti thất bại");
                                            }
                                        });

                            }
                        } else {
                            Toast.makeText(ScheduleSelect.this, "Không thể lấy thông báo", Toast.LENGTH_SHORT).show();
                        }


                    }
                    private void updateData(String NotiID) {
                        Log.e("", NotiID);
                        Map<String, Object> data = new HashMap<>();
                        data.put("noti_id", NotiID);

                        dtb_update.collection("Notification").document(NotiID)
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ScheduleSelect.this, "DocumentSnapshot successfully updated!", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ScheduleSelect.this, "Error updating document", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

//        final String not="Thông báo";
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                sendNotification(provideID,user.getUsername(),not);
//
//                notify=false;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
//    private void sendNotification(String receiver, String username, String noti)
//    {
//        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        Query query = tokens.orderByKey().equalTo(receiver);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
//                    Token token = snapshot.getValue(Token.class);
//                    Data data = new Data(user.getUid(),R.mipmap.ic_launcher,username+": " +noti, "Thong bao",current_user_id);
//
//                    Sender sender = new Sender(data,token.getToken());
//
//                    apiService.sendNotification(sender)
//                            .enqueue(new Callback<MyReponse>() {
//                                @Override
//                                public void onResponse(Call<MyReponse> call, Response<MyReponse> response) {
//                                    if(response.code()==200){
//                                        if(response.body().success!=1){
//                                            Toast.makeText(ScheduleSelect.this,"Failed!",Toast.LENGTH_LONG);
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MyReponse> call, Throwable t) {
//
//                                }
//                            });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }



    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }

    private void initComponents(){
        btn_request=findViewById(R.id.btn_requestbooking);
        btn_back=findViewById(R.id.btn_noti_back);
        NgayNhan=findViewById(R.id.edt_NgayNhan);
        NgayTra=findViewById(R.id.edt_NgayTra);
        GioNhan=findViewById(R.id.edt_GioNhan);
        GioTra=findViewById(R.id.edt_GioTra);
        Nhan=Calendar.getInstance();
        Tra = Calendar.getInstance();
        NgayNhan.setText(dateFormat.format(Nhan.getTime()));
        GioNhan.setText(dateFormat.format(Nhan.getTime()));

        NgayTra.setText(dateFormat.format(Tra.getTime()));
        GioTra.setText(dateFormat.format(Tra.getTime()));
    }

}
