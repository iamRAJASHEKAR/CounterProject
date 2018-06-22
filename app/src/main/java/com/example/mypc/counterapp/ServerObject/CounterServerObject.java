package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class CounterServerObject
{
     @SerializedName("response")
     String response;

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }
}
