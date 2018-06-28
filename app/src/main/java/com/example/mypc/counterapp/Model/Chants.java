package com.example.mypc.counterapp.Model;

public class Chants
{
    String chantTitle;

    String chantText;



    public Chants(String chantTitle, String chantText)
    {
        this.chantTitle = chantTitle;
        this.chantText = chantText;
    }

    public String getChantTitle() {
        return chantTitle;
    }

    public void setChantTitle(String chantTitle) {
        this.chantTitle = chantTitle;
    }

    public String getChantText() {
        return chantText;
    }

    public void setChantText(String chantText) {
        this.chantText = chantText;
    }


}
