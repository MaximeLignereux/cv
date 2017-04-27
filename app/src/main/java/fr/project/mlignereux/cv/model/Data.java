package fr.project.mlignereux.cv.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Data {

    private String imageUrl;
    private String title;
    private String subtitle;
    private String date;
    private String description;
    private String where;


    public Data(){}

    public String getSubtitle() {
        return subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getWhere() {
        return where;
    }
}
