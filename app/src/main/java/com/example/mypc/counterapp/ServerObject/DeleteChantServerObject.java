package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class DeleteChantServerObject {
    @SerializedName("chant_id")
    public String chantId;
    @SerializedName("email")
    public String email;

    public String getChantId() {
        return chantId;
    }

    public void setChantId(String chantId) {
        this.chantId = chantId;
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

    @SerializedName("response")
    public String response;
}
