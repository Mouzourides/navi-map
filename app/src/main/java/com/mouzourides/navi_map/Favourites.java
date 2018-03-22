package com.mouzourides.navi_map;

/**
 * Created by Nik Mouzourides on 24/11/2016.
 */
// Favourites class, gets all components of a favourite and allows getting and setting of values easily.
public class Favourites {
    private String name;
    private String imageID;
    private String desc;
    private String rating;
    private String openNow;
    private String notes;


    // contructs favourite object initialising fields
    public Favourites(String name, String img, String desc, String rating, String openNow, String notes) {
        this.name = name;
        this.imageID = img;
        this.desc = desc;
        this.rating = rating;
        this.openNow = openNow;
        this.notes = notes;
    }

    // get and set classes for fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow= openNow;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }




}
