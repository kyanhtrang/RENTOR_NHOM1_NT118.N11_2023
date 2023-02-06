package com.example.carrenting.FragmentPages.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.carrenting.R;

public class CustomerSettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_setting,
                container, false);
        LinearLayout info = (LinearLayout) view.findViewById(R.id.layout_information);
        info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*Intent i = new Intent(getActivity(), OwnerMainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);*/
            }
        });
        return view;
/*        return inflater.inflate(R.layout.customer_fragment_user, container, false);*/
    }
}