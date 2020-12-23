
package com.music.ca7s.model.search_song;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TapScanUser {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
