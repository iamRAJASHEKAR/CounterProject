package com.example.mypc.counterapp.ServerObject;

import com.example.mypc.counterapp.Model.PublicList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FetchingFriendsChants {
    @SerializedName("FriendList")
    ArrayList<PublicList> publicList;

    public ArrayList<PublicList> getPublicList(){
        return publicList;
    }

    public void setPublicList(ArrayList<PublicList> publicList) {
        this.publicList = publicList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("email")

    public String email;

    @SerializedName("response")
    public String response;

}
