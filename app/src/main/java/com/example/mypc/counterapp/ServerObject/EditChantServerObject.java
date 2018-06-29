package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class EditChantServerObject
{
    @SerializedName("chant_name")
    public String ChantName;

    @SerializedName("chant_description")
    public String chantdescription;

    @SerializedName("chant_id")
    public String chantID;

    @SerializedName("created_email")
    public String createdEmail;

    @SerializedName("response")
    public String reposnse;

    @SerializedName("message")
    public String message;
}
