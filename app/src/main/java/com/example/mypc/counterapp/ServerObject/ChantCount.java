package com.example.mypc.counterapp.ServerObject;

import com.google.gson.annotations.SerializedName;

public class ChantCount {
    @SerializedName("chant_id")
    public String chantid;

    @SerializedName("megacount")
    public String megacount;

    public String getChantid() {
        return chantid;
    }

    public void setChantid(String chantid) {
        this.chantid = chantid;
    }

    public String getMegacount() {
        return megacount;
    }

    public void setMegacount(String megacount) {
        this.megacount = megacount;
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
