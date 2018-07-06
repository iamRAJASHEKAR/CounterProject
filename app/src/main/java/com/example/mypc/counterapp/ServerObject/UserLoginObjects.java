package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class UserLoginObjects {

    @SerializedName("device_token")
    public String device_token;
    @SerializedName("response")
    public String response;

    @SerializedName("Religion")
    public String Religion;

    public String getReligion() {
        return Religion;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    @SerializedName("email")
    public String email;
    @SerializedName("device_id")
    public String device_id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
