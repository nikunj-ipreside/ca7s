
package com.music.ca7s.model.uploaded_music;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class UploadedPojo extends BaseModel {

    @SerializedName("is_update")
    @Expose
    private Boolean isUpdate;

    @SerializedName("list")
    @Expose
    private UploadedList list;

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public UploadedList getList() {
        return list;
    }

    public void setList(UploadedList list) {
        this.list = list;
    }

}
