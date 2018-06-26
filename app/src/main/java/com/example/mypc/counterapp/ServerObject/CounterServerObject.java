package com.example.mypc.counterapp.ServerObject;

import com.example.mypc.counterapp.Model.Religion;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CounterServerObject
{

    private ArrayList<Religion> Religions;

     @SerializedName("response")
     String response;

    public ArrayList<Religion> getReligions() {
        return Religions;
    }

    public void setReligions(ArrayList<Religion> religions) {
        Religions = religions;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }


}
