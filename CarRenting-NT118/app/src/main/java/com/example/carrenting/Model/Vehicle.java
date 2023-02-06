package com.example.carrenting.Model;

public class Vehicle {
    String vehicle_id;
    String owner_name, owner_gmail, owner_phone, owner_address;
    String vehicle_name, vehicle_price, vehicle_seats, vehicle_number, vehicle_availability, vehicle_imageURL;

    public Vehicle() {
    }

    public Vehicle(String vehicle_id, String owner_name, String owner_gmail, String owner_phone, String owner_address, String vehicle_name, String vehicle_price, String vehicle_seats, String vehicle_number, String vehicle_availability, String vehicle_imageURL) {
        this.vehicle_id = vehicle_id;
        this.owner_name = owner_name;
        this.owner_gmail = owner_gmail;
        this.owner_phone = owner_phone;
        this.owner_address = owner_address;
        this.vehicle_name = vehicle_name;
        this.vehicle_price = vehicle_price;
        this.vehicle_seats = vehicle_seats;
        this.vehicle_number = vehicle_number;
        this.vehicle_availability = vehicle_availability;
        this.vehicle_imageURL = vehicle_imageURL;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_gmail() {
        return owner_gmail;
    }

    public void setOwner_gmail(String owner_gmail) {
        this.owner_gmail = owner_gmail;
    }

    public String getOwner_phone() {
        return owner_phone;
    }

    public void setOwner_phone(String owner_phone) {
        this.owner_phone = owner_phone;
    }

    public String getOwner_address() {
        return owner_address;
    }

    public void setOwner_address(String owner_address) {
        this.owner_address = owner_address;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_price() {
        return vehicle_price;
    }

    public void setVehicle_price(String vehicle_price) {
        this.vehicle_price = vehicle_price;
    }

    public String getVehicle_seats() {
        return vehicle_seats;
    }

    public void setVehicle_seats(String vehicle_seats) {
        this.vehicle_seats = vehicle_seats;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getVehicle_availability() {
        return vehicle_availability;
    }

    public void setVehicle_availability(String vehicle_availability) {
        this.vehicle_availability = vehicle_availability;
    }

    public String getVehicle_imageURL() {
        return vehicle_imageURL;
    }

    public void setVehicle_imageURL(String vehicle_imageURL) {
        this.vehicle_imageURL = vehicle_imageURL;
    }
}
