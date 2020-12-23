
package com.music.ca7s.model.topcas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopCasData {

    @SerializedName("album_name")
    @Expose
    private String albumName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_icon")
    @Expose
//    private String imageIcon;
//    @SerializedName("type")
//    @Expose
    private Integer type;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public String getImageIcon() {
//        return imageIcon;
//    }
//
//    public void setImageIcon(String imageIcon) {
//        this.imageIcon = imageIcon;
//    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
