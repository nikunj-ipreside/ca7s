
package com.music.ca7s.model.search_song;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class TapScanPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<TapScanData> data = null;

    public List<TapScanData> getData() {
        return data;
    }

    public void setData(List<TapScanData> data) {
        this.data = data;
    }

}
