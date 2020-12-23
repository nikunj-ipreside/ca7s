package com.music.ca7s.genericbottomsheet;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.music.ca7s.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BottomSheetViewHolder extends RecyclerView.ViewHolder {
    public TextView tvItemText;
    public LinearLayout lrMain;
    public BottomSheetViewHolder(View itemView) {
        super(itemView);
        tvItemText = itemView.findViewById(R.id.tvItemText);
        lrMain = itemView.findViewById(R.id.lrMain);

    }
}
