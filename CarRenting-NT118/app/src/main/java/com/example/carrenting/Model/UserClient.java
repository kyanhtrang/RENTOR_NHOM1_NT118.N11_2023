package com.example.carrenting.Model;


import android.app.Application;

import com.example.carrenting.Model.User;


public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
