package com.example.carrenting.ActivityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

import com.example.carrenting.R;
import com.example.carrenting.databinding.OwnerActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;

import com.example.carrenting.FragmentPages.Customer.CustomerActivityFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerHomeFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerSettingFragment;
import com.example.carrenting.R;
import com.example.carrenting.databinding.OwnerActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OwnerMainActivity extends AppCompatActivity {

    OwnerActivityMainBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_main);

    }

        private void replaceFragment(Fragment fragment)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_customer, fragment);
            fragmentTransaction.commit();
        }

}