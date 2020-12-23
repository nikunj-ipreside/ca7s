package com.music.ca7s.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class TrendingData implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("song_title")
    @Expose
    private String songTitle;
    private final static long serialVersionUID = -7383903034988333367L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TrendingData() {
    }

    /**
     *
     * @param id
     * @param songTitle
     */
    public TrendingData(long id, String songTitle) {
        super();
        this.id = id;
        this.songTitle = songTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("songTitle", songTitle).toString();
    }
}
