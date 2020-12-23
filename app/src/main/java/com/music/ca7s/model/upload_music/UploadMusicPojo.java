
package com.music.ca7s.model.upload_music;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class UploadMusicPojo extends BaseModel {


    @SerializedName("data")
    @Expose
    private UploaddMucisList data;

    public UploaddMucisList getData() {
        return data;
    }

    public void setData(UploaddMucisList data) {
        this.data = data;
    }

}
