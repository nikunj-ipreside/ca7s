
package com.music.ca7s.model.profile;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class ProfilePojo  extends BaseModel{


    @SerializedName("data")
    @Expose
    private List<ProfileDatum> data = null;

    @SerializedName("request_count")
    @Expose
    private String request_count;

    public String getRequestCount() {
        return request_count;
    }

    public void setRequestCount(String request_count) {
        this.request_count = request_count;
    }

    public List<ProfileDatum> getData() {
        return data;
    }

    public void setData(List<ProfileDatum> data) {
        this.data = data;
    }

}
