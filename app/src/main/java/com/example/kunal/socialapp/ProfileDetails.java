package com.example.kunal.socialapp;

import android.net.Uri;

public class ProfileDetails {

    private String image;
    private String name;
    private String penName;
    private String about;
    private String occupation;

    public ProfileDetails() {

    }

    public ProfileDetails(String image, String name, String penName, String about, String occupation) {
        this.image = image;
        this.name = name;
        this.penName = penName;
        this.about = about;
        this.occupation = occupation;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPenName() {
        return penName;
    }

    public String getAbout() {
        return about;
    }

    public String getOccupation() {
        return occupation;
    }
}
