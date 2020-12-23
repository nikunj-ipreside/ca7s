
package com.music.ca7s.model.login;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class LoginPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<LoginDatum> data = null;
    @SerializedName("is_update")
    @Expose
    private Boolean isUpdate;

    public List<LoginDatum> getData() {
        return data;
    }

    public void setData(List<LoginDatum> data) {
        this.data = data;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

}
