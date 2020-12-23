package com.music.ca7s.adapter.viewholder;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeTopHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgTopPic)
    public ImageView imgTopPic;
    @BindView(R.id.txtTopName)
    public TextView txtTopName;
    @BindView(R.id.llRowTopRoot)
    public LinearLayout llRowTopRoot;

    public HomeTopHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
