package com.example.chrisyeh.pupper_ui_swipescreen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chris Yeh on 11/13/2017 for the purposes of UCSC CMPS115 class project: puppers
 * Profile.java: java class file used to hold, set and retrieve dog profile information
 */

public class Profile {

    // Profile information stored as private variables in profile class.
    // SerialzedName allows for variable to be read by the json file and bound to a model variable
    // Expose makes variable readable to gson
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imgURL;

    @SerializedName("age")
    @Expose
    private int age;

    @SerializedName("location")
    @Expose
    private String location;

    // getters
    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
