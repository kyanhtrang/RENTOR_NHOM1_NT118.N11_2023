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
import com.example.carrenting.R;
import com.example.carrenting.databinding.ActivityMainBinding;


public class CustomerMainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new CustomerHomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.home:
                    replaceFragment(new CustomerHomeFragment());
                    break;
                case R.id.activity:
                    replaceFragment(new CustomerActivityFragment());
                    break;
                case R.id.notifications:
                    replaceFragment(new CustomerNotificationFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new CustomerSettingFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_customer, fragment);
        fragmentTransaction.commit();
    }

}