package com.example.carrenting.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private String email;
    private String user_id;
    private String username;
    private String avatarURL;

    private String driverLicense;
    private String dateOfBirth;

    private String phoneNumber;

    private String street;
    private String city;
    private String postalCode;

    private String password;

    private String ciCardFront;
    private String ciCardBehind;


    public User() {

    }

    protected User(Parcel in) {
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        avatarURL = in.readString();
        driverLicense = in.readString();
        dateOfBirth = in.readString();
        phoneNumber = in.readString();
        street = in.readString();
        city = in.readString();
        postalCode = in.readString();
        ciCardFront = in.readString();
        ciCardBehind = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getCiCardFront() {
        return ciCardFront;
    }

    public void setCiCardFront(String ciCardFront) {
        this.ciCardFront = ciCardFront;
    }

    public String getCiCardBehind() {
        return ciCardBehind;
    }

    public void setCiCardBehind(String ciCardBehind) {
        this.ciCardBehind = ciCardBehind;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public User(String email, String user_id, String username, String avatarURL, String driverLicense, String dateOfBirth, String phoneNumber, String street, String city, String postalCode, String password, String ciCardFront, String ciCardBehind) {
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.avatarURL = avatarURL;
        this.driverLicense = driverLicense;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.password = password;
        this.ciCardFront = ciCardFront;
        this.ciCardBehind = ciCardBehind;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatarURL + '\'' +

                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(avatarURL);
        dest.writeString(driverLicense);
        dest.writeString(dateOfBirth);
        dest.writeString(phoneNumber);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(postalCode);
        dest.writeString(ciCardFront);
        dest.writeString(ciCardBehind);
    }
}

