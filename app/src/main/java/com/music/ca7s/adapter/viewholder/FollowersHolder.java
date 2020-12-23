package com.music.ca7s.adapter.viewholder;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FollowersHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgDot)
    public ImageView imgDot;

    @BindView(R.id.imgFollowersPic)
    public ImageView imgFollowersPic;
    @BindView(R.id.txtName)
    public TextView txtName;
    @BindView(R.id.txtCity)
    public TextView txtCity;
    @BindView(R.id.linearCity)
    public LinearLayout linearCity;
    @BindView(R.id.txtPending)
    public TextView txtPending;

    @BindView(R.id.llRowRoot)
    public LinearLayout llRowRoot;

    public FollowersHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
