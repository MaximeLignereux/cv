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
}
