
package com.music.ca7s.model.search_song;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TapScanData {

    @SerializedName("artist_name")
    @Expose
    private String artistName;
    @SerializedName("duration_ms")
    @Expose
    private Integer durationMs;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("is_favorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("is_like")
    @Expose
    private Boolean isLike;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user")
    @Expose
    private TapScanUser user;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TapScanUser getUser() {
        return user;
    }

    public void setUser(TapScanUser user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
