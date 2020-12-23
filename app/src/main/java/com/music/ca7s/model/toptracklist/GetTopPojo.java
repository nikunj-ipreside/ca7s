
package com.music.ca7s.model.toptracklist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class GetTopPojo extends BaseModel {

    @SerializedName("list")
    @Expose
    private GetTopList list;

    public GetTopList getList() {
        return list;
    }

    public void setList(GetTopList list) {
        this.list = list;
    }

}
