
package com.music.ca7s.model.topcas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TopCasList {

    @SerializedName("data")
    @Expose
    private ArrayList<TopCasData> data = null;

    public ArrayList<TopCasData> getData() {
        return data;
    }

    public void setData(ArrayList<TopCasData> data) {
        this.data = data;
    }

}
