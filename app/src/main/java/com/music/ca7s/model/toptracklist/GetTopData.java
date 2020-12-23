
package com.music.ca7s.model.toptracklist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTopData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("album_id")
    private String album_id;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("lyrics")
    @Expose
    private String lyrics;
    @SerializedName("artist_name")
    @Expose
    private String artistName;
    @SerializedName("album_name")
    @Expose
    private String albumName;
    @SerializedName("album_url")
    @Expose
    private String albumUrl;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("is_like")
    @Expose
    private Boolean isLike = false;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @SerializedName("is_favorite")
    @Expose
    private Boolean isFavorite = false;
    @SerializedName("is_playlist")
    @Expose
    private Boolean isPlaylist = false;
    @SerializedName("is_download")
    @Expose
    private Boolean isDownload = false;
    @SerializedName("thirdparty_song")
    @Expose
    private Boolean thirdparty_song;
    @SerializedName("user")
    @Expose
    private List<GetTopUser> user = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
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

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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

    public List<GetTopUser> getUser() {
        return user;
    }

    public void setUser(List<GetTopUser> user) {
        this.user = user;
    }

    public void setThirdparty_song(Boolean thirdparty_song) {
        this.thirdparty_song = thirdparty_song;
    }

    public Boolean getThirdparty_song() {
        return thirdparty_song;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_id() {
        return album_id;
    }
}
