package com.music.ca7s.model;

import java.io.Serializable;

public class PlayListModel implements Serializable {
    int screenID;
    int genreID;
    String image;
    String type;
    String name;
    String genre_name;
    String playListId="";
    String en="";
    String pt = "";

    public void setPlayListId(String playListId) {
        this.playListId = playListId;
    }

    public String getPlayListId() {
        return playListId;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScreenID() {
        return screenID;
    }

    public void setScreenID(int screenID) {
        this.screenID = screenID;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setEn(String en) {
        this.en = en;
    }

    public String getEn() {
        return en;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getPt() {
        return pt;
    }
}
