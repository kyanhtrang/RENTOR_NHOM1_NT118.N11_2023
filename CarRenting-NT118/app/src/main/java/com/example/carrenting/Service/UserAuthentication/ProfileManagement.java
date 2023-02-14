package com.example.carrenting.Service.UserAuthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.carrenting.ActivityPages.ProfileActivity;
import com.example.carrenting.R;

public class ProfileManagement extends AppCompatActivity {

   private Button btnUpdate;
   private ImageView imgAvatar, imgFrontCCCD, imgBehindCCCD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        init();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileManagement.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void init(){
        btnUpdate = (Button) findViewById(R.id.btn_update);

        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        imgFrontCCCD = (ImageView) findViewById(R.id.img_front_CCCD);
        imgBehindCCCD = (ImageView) findViewById(R.id.img_behind_CCCD);


    }
}