package com.music.ca7s.model;

import java.io.Serializable;

public class DownloadPlaylistModel implements Serializable {
    private int id=0;
    private String name="";
    private String image="";
    private String value="";
    private String createdAt ="";

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }




}
