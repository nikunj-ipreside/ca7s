package com.music.ca7s.adapter.viewholder;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyMusicHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_song_image)
    public ImageView imgVPSongs;
    @BindView(R.id.tv_song_name)
    public TextView txtSongName;
    @BindView(R.id.txtSongContent)
    public TextView txtSongContent;
    @BindView(R.id.imgDot)
    public ImageView imgDot;
    @BindView(R.id.llRowRoot)
    public LinearLayout llRowRoot;


    public MyMusicHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
