package com.example.frup69513.cv.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by asus on 20/04/2017.
 */

@IgnoreExtraProperties
public class Contact {

    private String phone;
    private String email;
    private String address;
    private String linkedin;
    private String viadeo;

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getPhone() {
        return phone;
    }

    public String getViadeo() {
        return viadeo;
    }
}
