package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class AddChantServerObject
{
    @SerializedName("chant_name")
    public String chantName;

    @SerializedName("chant_description")
    public String chantDescription;

    @SerializedName("created_by")
    public String createdBy;

    @SerializedName("created_email")
    public String created_email;

    @SerializedName("time_stamp")
    public String timestamp;

    @SerializedName("privacy")
    public String privacy;

    @SerializedName("response")
    public String response;


}
