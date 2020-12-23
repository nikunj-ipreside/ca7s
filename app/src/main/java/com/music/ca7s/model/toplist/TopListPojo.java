
package com.music.ca7s.model.toplist;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class TopListPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private ArrayList<TopListDatum> data = null;


    public ArrayList<TopListDatum> getData() {
        return data;
    }

    public void setData(ArrayList<TopListDatum> data) {
        this.data = data;
    }

}
