
package com.music.ca7s.model.topcas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class TopCasPojo extends BaseModel{

    @SerializedName("list")
    @Expose
    private TopCasList list;

    public TopCasList getList() {
        return list;
    }

    public void setList(TopCasList list) {
        this.list = list;
    }

}
