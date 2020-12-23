
package com.music.ca7s.model.search_song;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class SearchHistoryPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private HistoryList list;

    public HistoryList getList() {
        return list;
    }

    public void setList(HistoryList list) {
        this.list = list;
    }

}
