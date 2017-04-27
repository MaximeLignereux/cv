package fr.project.mlignereux.cv.model;

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
    private String bornDate;
    private String state;
    private String driversLicense;
    private String linkedinIcon;
    private String viadeoIcon;

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

    public String getState() {
        return state;
    }

    public String getDriversLicense() {
        return driversLicense;
    }

    public String getBornDate() {
        return bornDate;
    }

    public String getLinkedinIcon() {
        return linkedinIcon;
    }

    public String getViadeoIcon() {
        return viadeoIcon;
    }
}
