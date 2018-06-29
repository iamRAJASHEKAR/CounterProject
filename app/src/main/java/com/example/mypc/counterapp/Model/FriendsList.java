package com.example.mypc.counterapp.Model;

import com.google.gson.annotations.SerializedName;

public class FriendsList {

    @SerializedName("isactive")
    public String isactive;


    @SerializedName("name")
    public String name;

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChant_name() {
        return chant_name;
    }

    public void setChant_name(String chant_name) {
        this.chant_name = chant_name;
    }

    public String getChant_count() {
        return chant_count;
    }

    public void setChant_count(String chant_count) {
        this.chant_count = chant_count;
    }

    public String getChant_id() {
        return chant_id;
    }

    public void setChant_id(String chant_id) {
        this.chant_id = chant_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("chant_name")
    public String chant_name;

    @SerializedName("chant_count")
    public String chant_count;

    @SerializedName("chant_id")
    public String chant_id;

    @SerializedName("email")
    public String email;



}