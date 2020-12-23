package com.music.ca7s.model.share;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class SharePojo extends BaseModel {

    @SerializedName("generated_url")
    @Expose
    private String generated_url;


    public String getGeneratedUrl() {
        return generated_url;
    }

    public void setGeneratedUrl(String generated_url) {
        this.generated_url = generated_url;
    }
}
