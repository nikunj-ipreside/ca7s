package com.music.ca7s.model;

public class SongPlayingModel {

    int songID;
    String songName;
    String songArtistName;
    String songLyrix;
    String songPic;
    String songUrl;
    boolean songLike;
    boolean songFavourite;
    boolean songDownload;


    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtistName() {
        return songArtistName;
    }

    public void setSongArtistName(String songArtistName) {
        this.songArtistName = songArtistName;
    }

    public String getSongLyrix() {
        return songLyrix;
    }

    public void setSongLyrix(String songLyrix) {
        this.songLyrix = songLyrix;
    }

    public String getSongPic() {
        return songPic;
    }

    public void setSongPic(String songPic) {
        this.songPic = songPic;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public boolean isSongLike() {
        return songLike;
    }

    public void setSongLike(boolean songLike) {
        this.songLike = songLike;
    }

    public boolean isSongFavourite() {
        return songFavourite;
    }

    public void setSongFavourite(boolean songFavourite) {
        this.songFavourite = songFavourite;
    }

    public boolean isSongDownload() {
        return songDownload;
    }

    public void setSongDownload(boolean songDownload) {
        this.songDownload = songDownload;
    }
}
