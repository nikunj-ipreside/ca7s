package com.music.ca7s.model;

import java.io.Serializable;

public class OfflineSearchModel implements Serializable {
    private int id=0;
    private String name="";
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

}
