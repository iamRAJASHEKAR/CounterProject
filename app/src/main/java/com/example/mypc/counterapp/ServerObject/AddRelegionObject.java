package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class AddRelegionObject {
    @SerializedName("religion_name")
    public String relegion;
    @SerializedName("response")
    String response;

    public String getRelegion() {
        return relegion;
    }

    public void setRelegion(String relegion) {
        this.relegion = relegion;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


}
