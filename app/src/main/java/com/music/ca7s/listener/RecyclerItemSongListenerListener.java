package com.music.ca7s.listener;

import android.widget.ImageView;

import com.music.ca7s.mediaplayer.Song;

import java.util.ArrayList;

public interface RecyclerItemSongListenerListener {

    void onItemClicked(int pos,ArrayList<Song> song);

    void onImageDotClicked(int pos,Song song);

    void onFavouriteButtonClicked(int pos, Song song, ImageView iv_like);
}
