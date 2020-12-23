package com.music.ca7s.listener;

import android.widget.ImageView;

import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.OfflineSearchModel;
import com.music.ca7s.model.SearchData;
import com.music.ca7s.model.SearchModel;

public interface RecyclerItemSearchedSongListenerListener {

    void onClickedonSong(int pos, SearchData song);

    void onClickedOnHistoryItem(int pos, OfflineSearchModel song);

    void onRemove(int pos, OfflineSearchModel song);

    void onDownloadClick(int pos, SearchData searchData);

}
