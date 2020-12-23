
package com.music.ca7s.model.favourite.playlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavPlaylist {

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
    private FavPlaylistImages images;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("user")
    @Expose
    private FavPlaylistUser user;
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


    @SerializedName("like_count")
    @Expose
    private Integer likeCount;


    @SerializedName("lyrics")
    @Expose
    private String lyrics;

    @SerializedName("album_id")
    @Expose
    private Integer albumId;

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }



    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }




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

    public FavPlaylistImages getImages() {
        return images;
    }

    public void setImages(FavPlaylistImages images) {
        this.images = images;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public FavPlaylistUser getUser() {
        return user;
    }

    public void setUser(FavPlaylistUser user) {
        this.user = user;
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

}
