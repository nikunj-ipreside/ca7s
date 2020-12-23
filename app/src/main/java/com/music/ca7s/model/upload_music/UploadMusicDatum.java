
package com.music.ca7s.model.upload_music;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadMusicDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("mime")
    @Expose
    private String mime;
    @SerializedName("original_filename")
    @Expose
    private String originalFilename;
    @SerializedName("song_title")
    @Expose
    private String songTitle;
    @SerializedName("album_name")
    @Expose
    private String albumName;
    @SerializedName("artist_name")
    @Expose
    private String artistName;
    @SerializedName("genre_id")
    @Expose
    private Integer genreId;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("lyrics")
    @Expose
    private String lyrics;
    @SerializedName("privacy")
    @Expose
    private Integer privacy;
    @SerializedName("soft_delete")
    @Expose
    private Integer softDelete;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public Integer getSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(Integer softDelete) {
        this.softDelete = softDelete;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
