
package com.music.ca7s.model.top_track;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class ListTopPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<ListTopData> data = null;

    public List<ListTopData> getData() {
        return data;
    }

    public void setData(List<ListTopData> data) {
        this.data = data;
    }

}
