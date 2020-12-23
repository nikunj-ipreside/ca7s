package com.music.ca7s.adapter.viewholder;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeGenreHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgGenrePic)
    public ImageView imgGenrePic;
    @BindView(R.id.txtGenreName)
    public TextView txtGenreName;
    @BindView(R.id.llRowGenreRoot)
    public LinearLayout llRowGenreRoot;

    public HomeGenreHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
