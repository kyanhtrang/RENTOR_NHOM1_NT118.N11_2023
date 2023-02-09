package com.example.carrenting.ActivityPages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.carrenting.FragmentPages.Customer.CustomerActivityFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerHomeFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerSettingFragment;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


public class CustomerMainActivity extends AppCompatActivity{

    public static final int AVATAR_REQUEST_CODE = 99;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_ACTIVITY = 1;
    private static final int FRAGMENT_MESSAGE = 2;
    private static final int FRAGMENT_SETTING = 3;
    private int mCurrentFragment = FRAGMENT_HOME;
    private BottomNavigationView mbottomNavigationView;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb = FirebaseFirestore.getInstance();
        Init();

        // Bottom Navigation
        mbottomNavigationView = findViewById(R.id.bottomNavigationView);
        mbottomNavigationView.setBackground(null);
        mbottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.home:
                    replaceFragment(new CustomerHomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;
                    break;
                case R.id.activity:
                    replaceFragment(new CustomerActivityFragment());
                    mCurrentFragment = FRAGMENT_ACTIVITY;
                    break;
                case R.id.message:
                    replaceFragment(new CustomerNotificationFragment());
                    mCurrentFragment = FRAGMENT_MESSAGE;
                    break;
                case R.id.setting:
                    replaceFragment(new CustomerSettingFragment());
                    mCurrentFragment = FRAGMENT_SETTING;
            }
            return true;
        }
        );
    }
    // Hàm thay trang
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_customer, fragment);
        fragmentTransaction.commit();
    }

    private void Init(){
      //  startActivity();
    }
}