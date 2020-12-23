
package com.music.ca7s.model.top_track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListTopData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("downloadable")
    @Expose
    private Boolean downloadable;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("images")
    @Expose
    private ListTopImages images;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("user")
    @Expose
    private ListTopUser user;
    @SerializedName("is_like")
    @Expose
    private boolean isLike;
    @SerializedName("is_favorite")
    @Expose
    private boolean isFavorite;

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @SerializedName("lyrics")
    @Expose
    private String lyrics;


    @SerializedName("is_playlist")
    @Expose
    private boolean isPlaylist;


    public boolean getisPlaylist() {
        return isPlaylist;
    }

    public void setisPlaylist(boolean likeCount) {
        this.isPlaylist = isPlaylist;
    }



    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    @SerializedName("artist_name")
    @Expose
    private String artist_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ListTopImages getImages() {
        return images;
    }

    public void setImages(ListTopImages images) {
        this.images = images;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public ListTopUser getUser() {
        return user;
    }

    public void setUser(ListTopUser user) {
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
