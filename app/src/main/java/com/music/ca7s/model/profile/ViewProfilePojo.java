
package com.music.ca7s.model.profile;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class ViewProfilePojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<ViewProfileDatum> data = null;

    public List<ViewProfileDatum> getData() {
        return data;
    }

    public void setData(List<ViewProfileDatum> data) {
        this.data = data;
    }

}
