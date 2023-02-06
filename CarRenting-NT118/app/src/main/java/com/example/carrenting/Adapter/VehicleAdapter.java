package com.example.carrenting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carrenting.FragmentPages.Customer.CustomerHomeFragment;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder>{

    CustomerHomeFragment customerHomeFragment;
    Vehicle vehicle;
    ArrayList<Vehicle> vehicles;

    public VehicleAdapter(CustomerHomeFragment context, ArrayList<Vehicle> vehicles) {
        this.customerHomeFragment = context;
        this.vehicles = vehicles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(customerHomeFragment.getActivity()).inflate(R.layout.vehicle_card, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        vehicle = vehicles.get(position);
        holder.name.setText(vehicle.getVehicle_name());
        holder.price.setText(vehicle.getVehicle_price());
        Glide.with(customerHomeFragment.getActivity()).load(vehicle.getVehicle_imageURL()).into(holder.vehicleImage);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView vehicleImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vehicle_name);
            price = itemView.findViewById(R.id.vehicle_price);
            vehicleImage = itemView.findViewById(R.id.img_vehicle);
        }
    }
}
