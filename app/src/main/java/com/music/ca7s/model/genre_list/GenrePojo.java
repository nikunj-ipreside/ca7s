
package com.music.ca7s.model.genre_list;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class GenrePojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private ArrayList<GenreDatum> data = null;

    public ArrayList<GenreDatum> getData() {
        return data;
    }

    public void setData(ArrayList<GenreDatum> data) {
        this.data = data;
    }

}
