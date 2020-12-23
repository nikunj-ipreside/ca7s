package com.music.ca7s.viewpagify;

import androidx.cardview.widget.CardView;

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
