package com.music.ca7s.adapter.viewholder;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NavigationDrawerHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.txtItemName)
    public TextView txtItemName;
//    @BindView(R.id.imgItemIcon)
    public ImageView imgItemIcon;
//    @BindView(R.id.linearLine)
    public LinearLayout linearLine;
//    @BindView(R.id.llRowNavItem)
    public LinearLayout llRowNavItem;

    public NavigationDrawerHolder(View view) {
        super(view);
        txtItemName = view.findViewById(R.id.txtItemName);
        imgItemIcon = view.findViewById(R.id.imgItemIcon);
        linearLine = view.findViewById(R.id.linearLine);
        llRowNavItem = view.findViewById(R.id.llRowNavItem);

//        ButterKnife.bind(this, view);
    }
}
