
package com.music.ca7s.model.search_song.international;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InternationalList {

    @SerializedName("data")
    @Expose
    private List<InternationalData> data = null;

    public List<InternationalData> getData() {
        return data;
    }

    public void setData(List<InternationalData> data) {
        this.data = data;
    }

}
