package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class LogoutServerObjects {
    @SerializedName("device_id")
    public String device_id;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @SerializedName("email")

    public String user_email;
}
