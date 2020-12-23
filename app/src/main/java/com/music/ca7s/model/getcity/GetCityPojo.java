
package com.music.ca7s.model.getcity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class GetCityPojo extends BaseModel {

    @SerializedName("list")
    @Expose
    private GetCityList list;

    public GetCityList getList() {
        return list;
    }

    public void setList(GetCityList list) {
        this.list = list;
    }

}
