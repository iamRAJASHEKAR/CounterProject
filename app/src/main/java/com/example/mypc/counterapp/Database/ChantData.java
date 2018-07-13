package com.example.mypc.counterapp.Database;

import com.j256.ormlite.field.DatabaseField;

public class ChantData
{

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "chant_name")
    private String chant_name;
    @DatabaseField(columnName = "chant_description")
    private String chant_description;
    @DatabaseField(columnName = "time_stamp")
    private String firstname;

    public ChantData() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


}
