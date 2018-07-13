package com.example.mypc.counterapp.ServerObject;

import com.example.mypc.counterapp.Model.Contact;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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


    @SerializedName("religion")
    public String religion;

    public String getChantName() {
        return chantName;
    }

    public void setChantName(String chantName) {
        this.chantName = chantName;
    }

    public String getChantDescription() {
        return chantDescription;
    }

    public void setChantDescription(String chantDescription) {
        this.chantDescription = chantDescription;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreated_email() {
        return created_email;
    }

    public void setCreated_email(String created_email) {
        this.created_email = created_email;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("time_stamp")
    public String timestamp;

    @SerializedName("privacy")
    public String privacy;

    @SerializedName("contacts")
    public ArrayList<Contact> contacts;


    @SerializedName("response")
    public String response;


}
