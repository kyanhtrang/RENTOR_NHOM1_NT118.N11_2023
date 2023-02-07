package com.example.carrenting.ActivityPages;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.carrenting.FragmentPages.Customer.CustomerActivityFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerHomeFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.FragmentPages.Customer.CustomerSettingFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerActivityFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerNotificationsFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerSettingFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerVehicleFragment;
import com.example.carrenting.R;
import com.example.carrenting.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class OwnerMainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int FRAGMENT_VEHICLE = 0;
    private static final int FRAGMENT_ACTIVITY = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_SETTING = 3;
    private int mCurrentFragment = FRAGMENT_VEHICLE;
    private NavigationView mNavigationView;
    private OwnerMainActivity mMainActivity;
    private BottomNavigationView mbottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_main);

        mMainActivity = this;
        // Bottom Navigation
        mbottomNavigationView = findViewById(R.id.bottomNavigationView);
        mbottomNavigationView.setBackground(null);

        mbottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.home:
                    replaceFragment(new OwnerVehicleFragment());
                    mCurrentFragment = FRAGMENT_VEHICLE;
                    break;
                case R.id.activity:
                    replaceFragment(new OwnerActivityFragment());
                    mCurrentFragment = FRAGMENT_ACTIVITY;
                    break;
                case R.id.notifications:
                    replaceFragment(new OwnerNotificationsFragment());
                    mCurrentFragment = FRAGMENT_NOTIFICATION;
                    break;
                case R.id.setting:
                    replaceFragment(new OwnerSettingFragment());
                    mCurrentFragment = FRAGMENT_SETTING;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout_owner, fragment, null);
        fragmentTransaction.commit();
    }
}