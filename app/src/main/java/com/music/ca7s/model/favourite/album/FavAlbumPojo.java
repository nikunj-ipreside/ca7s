
package com.music.ca7s.model.favourite.album;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class FavAlbumPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private AlbumList list;

    public AlbumList getList() {
        return list;
    }

    public void setList(AlbumList list) {
        this.list = list;
    }

}
