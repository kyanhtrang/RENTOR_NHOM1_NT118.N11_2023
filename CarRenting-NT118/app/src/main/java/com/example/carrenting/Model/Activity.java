package com.example.carrenting.Model;


public class Activity {
    private String customer_id;
    private String provider_id;
    private String status;
    private String noti_id;
    private String vehicle_id;

    private String dropoff;

    private String pickup;

    public Activity()

    {

    }

    public Activity(String customer_id, String provide_id, String status, String noti_id, String vehicle_id) {
        this.customer_id = customer_id;
        this.provider_id = provide_id;
        this.status = status;
        this.noti_id = noti_id;
        this.vehicle_id = vehicle_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provide_id) {
        this.provider_id = provide_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(String noti_id) {
        this.noti_id = noti_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;

    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }

    public String getDropoff() {
        return dropoff;
    }

    public String getPickup() {
        return pickup;
    }

}
