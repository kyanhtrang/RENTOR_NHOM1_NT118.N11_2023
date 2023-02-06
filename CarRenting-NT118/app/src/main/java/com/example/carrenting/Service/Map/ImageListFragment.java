package com.example.carrenting.Service.Map;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.carrenting.Adapter.ImageListRecyclerAdapter;
import com.example.carrenting.R;

import java.util.ArrayList;


public class ImageListFragment extends Fragment implements ImageListRecyclerAdapter.ImageListRecyclerClickListener{

    private static final String TAG = "ImageListFragment";
    private static final int NUM_COLUMNS = 2;

    //widgets
    private RecyclerView mRecyclerView;


    //vars
    private ArrayList<Integer> mImageResources = new ArrayList<>();
    private IProfile mIProfile;

    public ImageListFragment() {
        // Required empty public constructor
    }


    public static ImageListFragment newInstance() {
        return new ImageListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);
        mRecyclerView = view.findViewById(R.id.image_list_recyclerview);

        getImageResouces();
        initRecyclerview();

        return view;
    }

    private void getImageResouces(){
        mImageResources.add(R.drawable.cwm_logo);
        mImageResources.add(R.drawable.cartman_cop);
        mImageResources.add(R.drawable.eric_cartman);
        mImageResources.add(R.drawable.ike);
        mImageResources.add(R.drawable.kyle);
        mImageResources.add(R.drawable.satan);
        mImageResources.add(R.drawable.chef);
        mImageResources.add(R.drawable.tweek);
    }

    private void initRecyclerview(){
        ImageListRecyclerAdapter mAdapter = new ImageListRecyclerAdapter(getActivity(), mImageResources, this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIProfile = (IProfile) getActivity();
    }

    @Override
    public void onImageSelected(int position) {
        mIProfile.onImageSelected(mImageResources.get(position));
    }
}








