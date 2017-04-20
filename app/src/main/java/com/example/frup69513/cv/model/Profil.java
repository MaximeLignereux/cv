package com.example.frup69513.cv.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Profil {

    private String name;
    private String photoUrl;
    private String job;
    private String where;
    private String from;
    private String backgroundUrl;
    private String description;
    private String bornDate;
    private String address;
    private String state;
    private String driversLicense;
    private String mail;

    public Profil(){}

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getJob() {
        return job;
    }

    public String getWhere() {
        return where;
    }

    public String getFrom() {
        return from;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getBornDate() {
        return bornDate;
    }

    public String getDriversLicense() {
        return driversLicense;
    }

    public String getState() {
        return state;
    }

    public String getMail() {
        return mail;
    }
}
