package com.example.carrenting.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carrenting.FragmentPages.Customer.CustomerHomeFragment;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.Model.onClickInterface;
import com.example.carrenting.R;
import com.example.carrenting.Service.Vehicle.VehicleDetailActivity;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder>{
    CustomerHomeFragment customerHomeFragment;
    Vehicle vehicle;
    ArrayList<Vehicle> vehicles;
    onClickInterface onClickInterface;
    public VehicleAdapter(CustomerHomeFragment context, ArrayList<Vehicle> vehicles, onClickInterface onClickInterface) {
        this.customerHomeFragment = context;
        this.vehicles = vehicles;
        this.onClickInterface = onClickInterface;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(customerHomeFragment.getActivity()).inflate(R.layout.vehicle_card, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        vehicle = vehicles.get(position);
        holder.name.setText(vehicle.getVehicle_name());
        holder.price.setText(vehicle.getVehicle_price());
        holder.provider.setText(vehicle.getProvider_name());
        Glide.with(customerHomeFragment.getActivity()).load(vehicle.getVehicle_imageURL()).into(holder.vehicleImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                vehicle = vehicles.get(position);
                Intent intent = new Intent(customerHomeFragment.getActivity(), VehicleDetailActivity.class);
                intent.putExtra("vehicle_id", vehicle.getVehicle_id());
                customerHomeFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, provider;
        ImageView vehicleImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vehicle_name);
            price = itemView.findViewById(R.id.tv_vehicle_price);
            provider = itemView.findViewById(R.id.provider_name);
            vehicleImage = itemView.findViewById(R.id.img_vehicle);
        }

    }
}