
package com.music.ca7s.model.getcity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCityList {

    @SerializedName("data")
    @Expose
    private java.util.List<GetCityData> data = null;

    public java.util.List<GetCityData> getData() {
        return data;
    }

    public void setData(java.util.List<GetCityData> data) {
        this.data = data;
    }

}
