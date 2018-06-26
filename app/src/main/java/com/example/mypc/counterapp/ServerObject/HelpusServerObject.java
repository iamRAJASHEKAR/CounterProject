package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class HelpusServerObject
{
   @SerializedName("name")
    public  String name;

   @SerializedName("email")
    public String email;

   @SerializedName("phone")
    public String phone;

   @SerializedName("message")
    public String message;

    @SerializedName("response")
    public String response;

}
