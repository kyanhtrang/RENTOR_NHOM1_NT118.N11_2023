package com.example.carrenting.Service.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

   private Button btnUpdate;
   private ImageView imgAvatar, imgFrontCCCD, imgBehindCCCD;
   private TextView tvPhone, tvEmail, tvName, tvAddress, tvCity, tvBirthday;
   private FirebaseFirestore dtb_user;
   private FirebaseUser firebaseUser;
   private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        init();
        getInfor();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this, ProfileManagement.class);
                startActivity(i);
            }
        });
    }

    private void getInfor() {
        dtb_user.collection("Users")
            .whereEqualTo("user_id", user.getUser_id())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            tvName.setText(document.get("username").toString());
                            tvEmail.setText(document.get("email").toString());
                            tvPhone.setText(document.get("phoneNumber").toString());
                            tvAddress.setText(document.get("address").toString());
                            tvCity.setText(document.get("city").toString());
                            tvBirthday.setText(document.get("birthday").toString());
                            user.setAvatarURL(document.get("avatarURL").toString());

                            if (!document.get("avatarURL").toString().isEmpty()) {
                                Picasso.get().load(user.getAvatarURL()).into(imgAvatar);
                            }
                            else {
                                user.setAvatarURL("");
                            }

                            user.setCiCardFront(document.get("ciCardFront").toString());
                            if (!document.get("ciCardFront").toString().isEmpty()) {
                                Picasso.get().load(user.getCiCardFront()).into(imgFrontCCCD);
                            }
                            else {
                                user.setCiCardFront("");
                            }

                            user.setCiCardBehind(document.get("ciCardBehind").toString());
                            if (!document.get("ciCardBehind").toString().isEmpty()) {
                                Picasso.get().load(user.getCiCardBehind()).into(imgBehindCCCD);
                            }
                            else {
                                user.setCiCardBehind("");
                            }

                        }
                    }
                    else {
                        //
                        Toast.makeText(UserProfile.this, "Không thể lấy thông tin", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    private void init(){
        btnUpdate = findViewById(R.id.btn_update);

        imgAvatar = findViewById(R.id.img_avatar);
        imgFrontCCCD = findViewById(R.id.img_front_CCCD);
        imgBehindCCCD = findViewById(R.id.img_behind_CCCD);

        tvName = findViewById(R.id.fullname);
        tvEmail = findViewById(R.id.email);
        tvPhone = findViewById(R.id.phone);
        tvAddress = findViewById(R.id.address);
        tvCity = findViewById(R.id.city);
        tvBirthday = findViewById(R.id.birthday);

        dtb_user = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setUser_id(firebaseUser.getUid());
    }
}