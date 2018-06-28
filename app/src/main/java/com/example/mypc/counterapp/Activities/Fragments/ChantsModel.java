package com.example.mypc.counterapp.Activities.Fragments;

public class ChantsModel
{



    public String getName() {
        return name;
    }

    public ChantsModel(String name, String user) {
        this.name = name;
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    String name;
    String user;
    public boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
