
package com.music.ca7s.model.uploaded_music;

import java.math.BigInteger;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadedDatum {

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
    private List<UploadedUser> user = null;

    @SerializedName("download_count")
    @Expose
    private BigInteger download_count;

    @SerializedName("stream_count")
    @Expose
    private BigInteger stream_count;

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @SerializedName("lyrics")
    @Expose
    private String lyrics;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @SerializedName("artist_name")
    @Expose
    private String artistName;

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

    public List<UploadedUser> getUser() {
        return user;
    }

    public void setUser(List<UploadedUser> user) {
        this.user = user;
    }

    public void set_private(Boolean _private) {
        this._private = _private;
    }

    public Boolean get_private() {
        return _private;
    }

    public void setDownload_count(BigInteger download_count) {
        this.download_count = download_count;
    }

    public BigInteger getDownload_count() {
        return download_count;
    }

    public void setStream_count(BigInteger stream_count) {
        this.stream_count = stream_count;
    }

    public BigInteger getStream_count() {
        return stream_count;
    }


    @SerializedName("album_id")
    private String album_id;
    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_id() {
        return album_id;
    }
}
