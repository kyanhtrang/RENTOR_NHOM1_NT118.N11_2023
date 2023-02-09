package com.example.carrenting.Model;


public class Notification {


    private String CustomerID;
    private String ProvideID;
    private String Name_Provide;
    private String Name_customer;
    private String Status;
    private String OrderID;

    
    public Notification()
    {

    }
    public Notification(String customerID, String name_customer, String provideID, String name_Provide, String status, String orderID)
    {
        this.CustomerID=customerID;
        this.ProvideID=provideID;
        this.Name_customer=name_customer;
        this.Name_Provide=name_Provide;
        this.Status=status;
        this.OrderID=orderID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
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
