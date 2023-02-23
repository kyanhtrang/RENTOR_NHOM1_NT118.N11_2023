package com.example.carrenting.Adapter;

import android.content.Intent;
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
import com.example.carrenting.Model.onClickInterface;
import com.example.carrenting.R;
import com.example.carrenting.Service.Vehicle.UpdateVehicle;

import java.util.ArrayList;

public class OwnerVehicleAdapter extends RecyclerView.Adapter<OwnerVehicleAdapter.MyViewHolder>{
    OwnerVehicleFragment ownerVehicleFragment;
    Vehicle vehicle;
    ArrayList<Vehicle> vehicles;
    onClickInterface onClickInterface;
    public OwnerVehicleAdapter(OwnerVehicleFragment context, ArrayList<Vehicle> vehicles, onClickInterface onClickInterface) {
        this.ownerVehicleFragment = context;
        this.vehicles = vehicles;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public OwnerVehicleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ownerVehicleFragment.getActivity()).inflate(R.layout.vehicle_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerVehicleAdapter.MyViewHolder holder, int position) {
        vehicle = vehicles.get(position);
        holder.name.setText(vehicle.getVehicle_name());
        holder.price.setText(vehicle.getVehicle_price());
        holder.provider.setText(vehicle.getProvider_name());
        Glide.with(ownerVehicleFragment.getActivity()).load(vehicle.getVehicle_imageURL()).into(holder.vehicleImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                vehicle = vehicles.get(position);
                Intent intent = new Intent(ownerVehicleFragment.getActivity(), UpdateVehicle.class);
                intent.putExtra("vehicle_id", vehicle.getVehicle_id());
                ownerVehicleFragment.startActivity(intent);
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
