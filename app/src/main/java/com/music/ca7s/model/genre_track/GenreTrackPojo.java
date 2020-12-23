
package com.music.ca7s.model.genre_track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class GenreTrackPojo extends BaseModel {

    @SerializedName("is_update")
    @Expose
    private Boolean isUpdate;

    @SerializedName("list")
    @Expose
    private GenreTrackList list;

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public GenreTrackList getList() {
        return list;
    }

    public void setList(GenreTrackList list) {
        this.list = list;
    }

}
