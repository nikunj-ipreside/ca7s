package com.music.ca7s.helper;

import androidx.recyclerview.widget.RecyclerView;

import com.music.ca7s.mediaplayer.Song;

import java.util.ArrayList;

/**
 * Created by Sudesh Bishnoi on 21-Jul-18.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

    void onNoteListChanged(ArrayList<Song> customers);
}