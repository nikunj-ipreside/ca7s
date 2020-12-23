
package com.music.ca7s.model.favourite.playlist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavPlaylistData {

    @SerializedName("playlist")
    @Expose
    private List<FavPlaylist> playlist = null;

    public List<FavPlaylist> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<FavPlaylist> playlist) {
        this.playlist = playlist;
    }

}
