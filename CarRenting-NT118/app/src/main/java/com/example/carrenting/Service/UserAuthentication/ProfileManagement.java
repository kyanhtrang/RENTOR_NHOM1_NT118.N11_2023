package com.example.carrenting.Service.UserAuthentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carrenting.ActivityPages.OwnerMainActivity;
import com.example.carrenting.ActivityPages.ProfileActivity;
import com.example.carrenting.R;

public class ProfileManagement extends AppCompatActivity {

   Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        btnUpdate = (Button) findViewById(R.id.btn_update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileManagement.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }
}