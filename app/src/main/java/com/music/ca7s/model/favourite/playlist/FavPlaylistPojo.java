
package com.music.ca7s.model.favourite.playlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class FavPlaylistPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private FavPlaylistList list;

    public FavPlaylistList getList() {
        return list;
    }

    public void setList(FavPlaylistList list) {
        this.list = list;
    }

}
