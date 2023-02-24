package com.example.carrenting.FragmentPages.Owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.ActivityPages.OwnerMainActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerSettingFragment extends Fragment {

    private TextView tvName;
    private CircleImageView imgAvatar;

    private FirebaseFirestore dtb_user;
    private FirebaseUser firebaseUser;
    private User user = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_fragment_setting,
                container, false);

        imgAvatar = view.findViewById(R.id.img_avatar);
        tvName = view.findViewById(R.id.tv_name);

        dtb_user = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setUser_id(firebaseUser.getUid());

        dtb_user.collection("Users")
                .whereEqualTo("user_id", user.getUser_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                tvName.setText(document.get("username").toString());
                                user.setAvatarURL(document.get("avatarURL").toString());
                                if (!document.get("avatarURL").toString().isEmpty()) {
                                    Picasso.get().load(user.getAvatarURL()).into(imgAvatar);
                                }
                                else {
                                    user.setAvatarURL("");
                                }
                            }
                        }
                        else {
                            //
                            Toast.makeText(view.getContext(), "Không thể lấy thông tin", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        LinearLayout connect = (LinearLayout) view.findViewById(R.id.layout_connect);
        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), CustomerMainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        return view;
    }
}