
package com.music.ca7s.model.favourite.song;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoriteList {

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

    @SerializedName("stream_url")
    @Expose
    private String streamUrl;

    @SerializedName("artist_name")
    @Expose
    private String artistName;
    @SerializedName("is_like")
    @Expose
    private Boolean isLike;
    @SerializedName("is_favorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("is_playlist")
    @Expose
    private Boolean isPlaylist;
    @SerializedName("is_download")
    @Expose
    private Boolean isDownload;
    @SerializedName("thirdparty_song")
    @Expose
    private Boolean thirdparty_song;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;

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



    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }



    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
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

    public Boolean getIsPlaylist() {
        return isPlaylist;
    }

    public void setIsPlaylist(Boolean isPlaylist) {
        this.isPlaylist = isPlaylist;
    }

    public Boolean getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(Boolean isDownload) {
        this.isDownload = isDownload;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setThirdparty_song(Boolean thirdparty_song) {
        this.thirdparty_song = thirdparty_song;
    }

    public Boolean getThirdparty_song() {
        return thirdparty_song;
    }
}
