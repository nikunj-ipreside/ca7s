package com.music.ca7s.adapter.generic_adapter;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class SingleSelectionAdapter<T, VH extends RecyclerView.ViewHolder> extends GenericAdapter<T, VH> {


    public T getSelectedItem() throws NullPointerException {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;

    }

    private T selectedItem;

    public SingleSelectionAdapter(@LayoutRes int layout, Class<VH> mViewHolderClass, List<T> modelList) {
        super(layout, mViewHolderClass, modelList);
        if (modelList != null)
            selectedItem = modelList.get(0);
    }


}
