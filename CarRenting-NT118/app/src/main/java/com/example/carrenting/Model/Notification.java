package com.example.carrenting.Model;


public class Notification {


    private String CustomerID;
    private String ProvideID;
    private String Name_Provide;
    private String Name_customer;
    private String Status;
    private String NotiID;
    private String vehicle_id;

    public Notification()
    {

    }

    public Notification(String customerID, String provideID, String name_Provide, String name_customer, String status, String notiID, String vehicl_id) {
        CustomerID = customerID;
        ProvideID = provideID;
        Name_Provide = name_Provide;
        Name_customer = name_customer;
        Status = status;
        NotiID = notiID;
        vehicle_id = vehicl_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getNotiID() {
        return NotiID;
    }

    public void setNotiID(String notiID) {
        NotiID = notiID;
    }

    public String getName_Provide() {
        return Name_Provide;
    }

    public void setName_Provide(String name_Provide) {
        Name_Provide = name_Provide;
    }

    public String getName_customer() {
        return Name_customer;
    }

    public void setName_customer(String name_customer) {
        Name_customer = name_customer;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getProvideID() {
        return ProvideID;
    }

    public void setProvideID(String provideID) {
        ProvideID = provideID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    @Override
    public String toString() {
        return "Notification{" +
                "Customer ID=" + CustomerID +
                "Name Customer=" + Name_customer+ '\'' +
                ", Provide ID='" + ProvideID + '\'' +
                "Name Provide=" + Name_Provide + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}
