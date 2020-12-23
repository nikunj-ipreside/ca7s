
package com.music.ca7s.model.genre_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenreDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("image_icon")
    @Expose
    private String imageIcon;

    @SerializedName("en")
    @Expose
    private String en;

    @SerializedName("pt")
    @Expose
    private String pt;

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

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getPt() {
        return pt;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEn() {
        return en;
    }
}
