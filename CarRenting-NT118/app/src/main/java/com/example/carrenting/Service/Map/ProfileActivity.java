package com.example.carrenting.Service.Map;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.carrenting.Model.User;
import com.example.carrenting.Model.UserClient;
import com.example.carrenting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity implements
        View.OnClickListener,
        IProfile
{

    private static final String TAG = "ProfileActivity";


    //widgets
    private CircleImageView mAvatarImage;

    //vars
    private ImageListFragment mImageListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mAvatarImage = findViewById(R.id.image_choose_avatar);

        findViewById(R.id.image_choose_avatar).setOnClickListener(this);
        findViewById(R.id.text_choose_avatar).setOnClickListener(this);

        retrieveProfileImage();
    }

    private void retrieveProfileImage(){
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.cwm_logo)
                .placeholder(R.drawable.cwm_logo);

        int avatar = 0;
        try{
            avatar = Integer.parseInt(((UserClient)getApplicationContext()).getUser().getAvatarURL());
        }catch (NumberFormatException e){
            Log.e(TAG, "retrieveProfileImage: no avatar image. Setting default. " + e.getMessage() );
        }

        Glide.with(ProfileActivity.this)
                .setDefaultRequestOptions(requestOptions)
                .load(avatar)
                .into(mAvatarImage);
    }

    @Override
    public void onClick(View v) {
        mImageListFragment = new ImageListFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                .replace(R.id.fragment_container, mImageListFragment, getString(R.string.fragment_image_list))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onImageSelected(int resource) {

        // remove the image selector fragment
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                .remove(mImageListFragment)
                .commit();

        // display the image
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.cwm_logo)
                .error(R.drawable.cwm_logo);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(resource)
                .into(mAvatarImage);

        // update the client and database
        User user = ((UserClient)getApplicationContext()).getUser();
        user.setAvatarURL(String.valueOf(resource));

        FirebaseFirestore.getInstance()
                .collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid())
                .set(user);
    }

}
