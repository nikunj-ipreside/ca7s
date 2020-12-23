
package com.music.ca7s.model.favourite.song;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class FavSongPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private FavSongList list;

    public FavSongList getList() {
        return list;
    }

    public void setList(FavSongList list) {
        this.list = list;
    }

}
