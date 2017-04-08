package com.example.frup69513.cv.model;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Skill {

    private String title;
    private float value;

    public Skill(){}

    public String getTitle() {
        return title;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "title='" + title + '\'' +
                ", value=" + value +
                '}';
    }
}
