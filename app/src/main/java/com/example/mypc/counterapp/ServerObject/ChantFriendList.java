package com.example.mypc.counterapp.ServerObject;

import com.example.mypc.counterapp.Model.FriendsList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChantFriendList {
    @SerializedName("FriendsList")
    ArrayList<FriendsList> FriendsList;

    @SerializedName("response")
    public String response;

    public ArrayList<FriendsList> getFriendsList() {
        return FriendsList;
    }

    public void setFriendsList(ArrayList<FriendsList> friendsList) {
        this.FriendsList = friendsList;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getChant_id() {
        return chant_id;
    }

    public void setChant_id(String chant_id) {
        this.chant_id = chant_id;
    }

    @SerializedName("chant_id")
    public String chant_id;
}
