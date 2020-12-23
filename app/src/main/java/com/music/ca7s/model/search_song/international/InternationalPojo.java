
package com.music.ca7s.model.search_song.international;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class InternationalPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private InternationalList list;

    public InternationalList getList() {
        return list;
    }

    public void setList(InternationalList list) {
        this.list = list;
    }

}
