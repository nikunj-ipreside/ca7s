
package com.music.ca7s.model.followers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class FollowersPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private FollowersList list;

    public FollowersList getList() {
        return list;
    }

    public void setList(FollowersList list) {
        this.list = list;
    }

}
