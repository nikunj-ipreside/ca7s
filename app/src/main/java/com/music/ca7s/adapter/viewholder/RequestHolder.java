package com.music.ca7s.adapter.viewholder;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RequestHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgRequestPic)
    public ImageView imgRequestPic;
    @BindView(R.id.txtName)
    public TextView txtName;
    @BindView(R.id.txtAccept)
    public TextView txtAccept;
    @BindView(R.id.txtDeclain)
    public TextView txtDeclain;
    @BindView(R.id.llRowRoot)
    public LinearLayout llRowRoot;

    public RequestHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
