package com.music.ca7s.listener;

import com.music.ca7s.mediaplayer.Song;

import java.util.ArrayList;

public interface RecyclerItemListener {
    void onListUpdated(ArrayList<Song> songs);
//    void setListLike(boolean isLike);
}
