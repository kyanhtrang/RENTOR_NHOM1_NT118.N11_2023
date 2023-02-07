package com.example.carrenting.ActivityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.carrenting.FragmentPages.Owner.OwnerActicityFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerNotificationsFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerSettingFragment;
import com.example.carrenting.FragmentPages.Owner.OwnerVehicleFragment;
import com.example.carrenting.R;
import com.example.carrenting.databinding.ActivityMainBinding;


public class OwnerMainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.vehicle:
                    replaceFragment(new OwnerVehicleFragment());
                    break;
                case R.id.activity:
                    replaceFragment(new OwnerActicityFragment());
                    break;
                case R.id.notifications:
                    replaceFragment(new OwnerNotificationsFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new OwnerSettingFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_owner, fragment);
        fragmentTransaction.commit();
    }
}