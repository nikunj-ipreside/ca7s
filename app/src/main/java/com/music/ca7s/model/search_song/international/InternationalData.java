
package com.music.ca7s.model.search_song.international;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternationalData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("album_name")
    @Expose
    private String album_name;
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
    private InternationalImages images;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("user")
    @Expose
    private InternationalUser user;
    @SerializedName("is_like")
    @Expose
    private Boolean isLike;
    @SerializedName("is_favorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("track_type")
    @Expose
    private Integer trackType;

    @SerializedName("artist_name")
    @Expose
    private String artist_name;

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }


/*
    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    @SerializedName("stream_url")
    @Expose
    private String stream_url;
*/

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @SerializedName("lyrics")
    @Expose
    private String lyrics;


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

//    public Boolean getPrivate() {
//        return _private;
//    }
//
//    public void setPrivate(Boolean _private) {
//        this._private = _private;
//    }

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

    public InternationalImages getImages() {
        return images;
    }

    public void setImages(InternationalImages images) {
        this.images = images;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public InternationalUser getUser() {
        return user;
    }

    public void setUser(InternationalUser user) {
        this.user = user;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Integer getTrackType() {
        return trackType;
    }

    public void setTrackType(Integer trackType) {
        this.trackType = trackType;
    }

}
