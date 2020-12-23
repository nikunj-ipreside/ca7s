
package com.music.ca7s.model.profile;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class ProfilePicPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<ProfilePicDatum> data = null;


    public List<ProfilePicDatum> getData() {
        return data;
    }

    public void setData(List<ProfilePicDatum> data) {
        this.data = data;
    }

}
