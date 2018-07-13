package com.example.mypc.counterapp.Database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Userdata {
    @DatabaseField(generatedId = true,columnName = "user_id")
    private int id;
    @DatabaseField(columnName = "username")
    private String username;
    @DatabaseField(columnName = "firstname")
    private String firstname;
    @DatabaseField(columnName = "lastname")
    private String lastname;
    @DatabaseField(columnName = "email")
    private String email;

    public Userdata() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }
/*

    public Userdata(int id, String username, String password, String firstname, String lastname, String country, String email, boolean optimize_photos, boolean autoaccept_friend) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.email = email;
        this.optimize_photos = optimize_photos;
        this.autoaccept_friend = autoaccept_friend;
    }
*/

    public void setEmail(String email) {
        this.email = email;
    }


}
