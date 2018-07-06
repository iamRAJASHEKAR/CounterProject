package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class MegaCount {

    @SerializedName("mycount")
    public String mycount;


    @SerializedName("chant_id")
    public String chant_id;
    @SerializedName("email")
    public String email;

    public String getMycount() {
        return mycount;
    }

    public void setMycount(String mycount) {
        this.mycount = mycount;
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

    public String getMegacount() {
        return megacount;
    }

    public void setMegacount(String megacount) {
        this.megacount = megacount;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("megacount")

    public String megacount;
    @SerializedName("response")
    public String response;
    @SerializedName("message")
    public String message;

}
