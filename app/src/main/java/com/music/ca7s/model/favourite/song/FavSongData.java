
package com.music.ca7s.model.favourite.song;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavSongData {

    @SerializedName("favorite_list")
    @Expose
    private List<FavoriteList> favoriteList = null;

    public List<FavoriteList> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<FavoriteList> favoriteList) {
        this.favoriteList = favoriteList;
    }

}
