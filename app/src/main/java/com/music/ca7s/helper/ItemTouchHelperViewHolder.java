package com.music.ca7s.helper;

/**
 * Created by Sudesh Bishnoi on 21-Jul-18.
 */

public interface ItemTouchHelperViewHolder {
    /**
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * state should be cleared.
     */
    void onItemClear();
}
