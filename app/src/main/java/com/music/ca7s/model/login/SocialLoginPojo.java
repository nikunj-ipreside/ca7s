
package com.music.ca7s.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class SocialLoginPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private SocialLoginData data;

    @SerializedName("is_update")
    @Expose
    private Boolean isUpdate;

    public SocialLoginData getData() {
        return data;
    }

    public void setData(SocialLoginData data) {
        this.data = data;
    }
    
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
}
