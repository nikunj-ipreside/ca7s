
package com.music.ca7s.model.request_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class RequestPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private RequestList list;

    public RequestList getList() {
        return list;
    }

    public void setList(RequestList list) {
        this.list = list;
    }

}
