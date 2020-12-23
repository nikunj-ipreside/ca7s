package com.music.ca7s.mediaplayer;

import android.graphics.Bitmap;

import java.math.BigInteger;

/**
 * Created by Dhara on 15-05-2018.
 */


public class Song {

    private String songID;
    private String songTitle;
    private String songAlbum;
    private String songURL;
    private String songImageUrl;
    private String image_url;
    private String songNumber;
    private boolean isLike;
    private boolean isFavourite;
    private boolean isDownload;
    private String trackType;
    private String albumID;
    private String from;
    private String songPath;
    private String songImagePath;
    private boolean isPlaylist;
    private Boolean thirdparty_song = false;
    private Integer likeCount=0;
    private String createdAt;
    private String type = "";
    private String playList_id = "";
    private String user_id ="";
    private BigInteger downloades_song;
    private BigInteger stremmed_song;
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setSongImagePath(String songImagePath) {
        this.songImagePath = songImagePath;
    }

    public String getSongImagePath() {
        return songImagePath;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public void setPlaylist(boolean playlist) {
        isPlaylist = playlist;
    }


    public boolean getIsPlaylist() {
        return isPlaylist;
    }

    public void setIsPlaylist(boolean isPlaylist) {
        this.isPlaylist = isPlaylist;
    }


    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    private String lyrics;

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public boolean getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    private String songArtist="";

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }


    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }
    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public void setSongImageUrl(String songImageUrl) {
        this.songImageUrl = songImageUrl;
    }

    public String getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(String songNumber) {
        this.songNumber = songNumber;
    }

    public void setThirdparty_song(Boolean thirdparty_song) {
        this.thirdparty_song = thirdparty_song;
    }

    public void setDownloades_song(BigInteger downloades_song) {
        this.downloades_song = downloades_song;
    }

    public BigInteger getDownloades_song() {
        return downloades_song;
    }

    public void setStremmed_song(BigInteger stremmed_song) {
        this.stremmed_song = stremmed_song;
    }

    public BigInteger getStremmed_song() {
        return stremmed_song;
    }

    public Boolean getThirdparty_song() {
        if (thirdparty_song == null){
            return false;
        }else {
            return thirdparty_song;
        }
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        if (type != null && !type.isEmpty()) {
            return type;
        }else {
            return  "";
        }
    }

    public void setPlayList_id(String playList_id) {
        this.playList_id = playList_id;
    }

    public String getPlayList_id() {
        if (playList_id != null && !playList_id.isEmpty()) {
            return playList_id;
        }else {
            return "";
        }
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        if (user_id != null && !user_id.isEmpty()) {
            return user_id;
        }
        else {
            return "";
        }
    }
}
