package com.example.carrenting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carrenting.FragmentPages.Owner.OwnerVehicleFragment;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;

import java.util.ArrayList;

public class OwnerVehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder>{
    OwnerVehicleFragment ownerVehicleFragment;
    Vehicle vehicle;
    ArrayList<Vehicle> vehicles;

    public OwnerVehicleAdapter(OwnerVehicleFragment context, ArrayList<Vehicle> vehicles) {
        this.ownerVehicleFragment = context;
        this.vehicles = vehicles;
    }

    @NonNull
    @Override
    public VehicleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ownerVehicleFragment.getActivity()).inflate(R.layout.vehicle_card, parent, false);

        return new VehicleAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.MyViewHolder holder, int position) {
        vehicle = vehicles.get(position);
        holder.name.setText(vehicle.getVehicle_name());
        holder.price.setText(vehicle.getVehicle_price());
        holder.provider.setText(vehicle.getProvider_name());
        Glide.with(ownerVehicleFragment.getActivity()).load(vehicle.getVehicle_imageURL()).into(holder.vehicleImage);
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
            price = itemView.findViewById(R.id.vehicle_price);
            provider = itemView.findViewById(R.id.provider_name);
            vehicleImage = itemView.findViewById(R.id.img_vehicle);
        }
    }
}
