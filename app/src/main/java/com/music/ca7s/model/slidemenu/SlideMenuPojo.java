
package com.music.ca7s.model.slidemenu;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class SlideMenuPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<SlideMenuDatum> data = null;

    public List<SlideMenuDatum> getData() {
        return data;
    }

    public void setData(List<SlideMenuDatum> data) {
        this.data = data;
    }
    @SerializedName("language")
    @Expose
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @SerializedName("base_count")
    @Expose
    private String base_count;

    public String getBaseCount() {
        return base_count;
    }

    public void setBaseCount(String base_count) {
        this.base_count = base_count;
    }
}
