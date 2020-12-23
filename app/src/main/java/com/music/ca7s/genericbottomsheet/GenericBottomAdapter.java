package com.music.ca7s.genericbottomsheet;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.music.ca7s.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class GenericBottomAdapter extends RecyclerView.Adapter<GenericBottomAdapter.GenericBottomSheetViewHolder> {
    List<GenericBottomModel> data;
    abstract public void onItemClick(GenericBottomModel genericBottomModel);
    public GenericBottomAdapter(List<GenericBottomModel> data) {
        this.data = data;
    }

    @Override
    public GenericBottomSheetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_generic_bottomsheet_item, parent, false);
        return new GenericBottomSheetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GenericBottomSheetViewHolder holder, final int position) {
        holder.tvItemText.setText(data.get(position).getItemText());
        holder.lrMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class GenericBottomSheetViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItemText)
        TextView tvItemText;
        @BindView(R.id.lrMain)
        LinearLayout lrMain;

        public GenericBottomSheetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
