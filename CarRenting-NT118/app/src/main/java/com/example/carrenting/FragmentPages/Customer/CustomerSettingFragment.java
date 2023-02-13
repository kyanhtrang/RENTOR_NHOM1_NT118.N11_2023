package com.example.carrenting.FragmentPages.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.carrenting.ActivityPages.OwnerMainActivity;
import com.example.carrenting.R;
import com.example.carrenting.Service.UserAuthentication.LoginActivity;
import com.example.carrenting.Service.UserAuthentication.ProfileManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerSettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_setting,
                container, false);

        LinearLayout connect = (LinearLayout) view.findViewById(R.id.layout_connect);
        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), OwnerMainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        LinearLayout profile = (LinearLayout) view.findViewById(R.id.layout_information);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileManagement.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        LinearLayout delete = (LinearLayout) view.findViewById(R.id.layout_delete_account);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(view.getContext(), "User account deleted.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                                    } else {
                                        Toast.makeText(view.getContext(), "Failed to delete user account.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        LinearLayout signOut = (LinearLayout) view.findViewById(R.id.layout_sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
        return view;
/*        return inflater.inflate(R.layout.customer_fragment_user, container, false);*/
    }
}