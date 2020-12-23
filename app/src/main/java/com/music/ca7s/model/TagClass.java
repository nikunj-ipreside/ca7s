package com.music.ca7s.model;


/**
 * Created by Cuneyt on 21.8.2015.
 *
 */
public class TagClass {

    private int icon;
    private String name;
    private int color;
    private long songID;

    public TagClass() {

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }

    public long getSongID() {
        return songID;
    }

    public TagClass(int sinif, String name, int color, long songID) {
        this.icon = sinif;
        this.name = name;
        this.color = color;
        this.songID = songID;

    }


}
