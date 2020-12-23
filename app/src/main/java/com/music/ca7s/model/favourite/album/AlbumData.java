
package com.music.ca7s.model.favourite.album;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumData {

    @SerializedName("genre_id")
    @Expose
    private Integer genreId;
    @SerializedName("track_count")
    @Expose
    private Integer trackCount;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image_icon")
    @Expose
    private String imageIcon;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
