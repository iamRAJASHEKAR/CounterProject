package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class AddFriendServerObject
{
        @SerializedName("chant_id")
        public String chantId;

        @SerializedName("name")
        public String name;

        @SerializedName("email")
        public String email;

        @SerializedName("response")
        public String response;
}
