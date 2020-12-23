package com.music.ca7s.model.playlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayListData implements Serializable {

    @SerializedName("playlist")
    @Expose
    private ArrayList<Playlist> playlist = null;
    private final static long serialVersionUID = 2548968832250792456L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PlayListData() {
    }

    /**
     *
     * @param playlist
     */
    public PlayListData(ArrayList<Playlist> playlist) {
        super();
        this.playlist = playlist;
    }

    public ArrayList<Playlist> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Playlist> playlist) {
        this.playlist = playlist;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("playlist", playlist).toString();
    }
}
