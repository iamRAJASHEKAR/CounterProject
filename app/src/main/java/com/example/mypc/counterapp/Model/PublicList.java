package com.example.mypc.counterapp.Model;

import com.google.gson.annotations.SerializedName;

public class PublicList {
    @SerializedName("time_stamp")
    public String time_stamp;
    @SerializedName("created_email")
    public String created_email;
    @SerializedName("privacy")
    public String privacy;
    @SerializedName("chant_name")
    public String chant_name;

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getCreated_email() {
        return created_email;
    }

    public void setCreated_email(String created_email) {
        this.created_email = created_email;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getChant_name() {
        return chant_name;
    }

    public void setChant_name(String chant_name) {
        this.chant_name = chant_name;
    }

    public String getChant_description() {
        return chant_description;
    }

    public void setChant_description(String chant_description) {
        this.chant_description = chant_description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getChant_id() {
        return chant_id;
    }

    public void setChant_id(String chant_id) {
        this.chant_id = chant_id;
    }

    @SerializedName("chant_description")
    public String chant_description;
    @SerializedName("created_by")
    public String created_by;
    @SerializedName("chant_id")
    public String chant_id;
}
