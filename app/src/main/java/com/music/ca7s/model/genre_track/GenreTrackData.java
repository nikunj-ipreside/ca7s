
package com.music.ca7s.model.genre_track;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenreTrackData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("tracks_count")
    @Expose
    private Integer tracksCount;
    @SerializedName("user")
    @Expose
    private List<Object> user = null;
    @SerializedName("is_like")
    @Expose
    private boolean isLike;

    @SerializedName("is_favorite")
    @Expose
    private boolean isFavorite;

    @SerializedName("is_playlist")
    @Expose
    private boolean isPlaylist;


    public boolean getisPlaylist() {
        return isPlaylist;
    }

    public void setisPlaylist(boolean likeCount) {
        this.isPlaylist = isPlaylist;
    }


    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    @SerializedName("like_count")
    @Expose
    private Integer likeCount;

    @SerializedName("artist_name")
    @Expose
    private String artist_name;

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @SerializedName("lyrics")
    @Expose
    private String lyrics;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public Integer getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(Integer tracksCount) {
        this.tracksCount = tracksCount;
    }

    public List<Object> getUser() {
        return user;
    }

    public void setUser(List<Object> user) {
        this.user = user;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

}
