package com.music.ca7s.adapter.generic_adapter;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public abstract class MultipleSelectionAdapter<T, VH extends RecyclerView.ViewHolder> extends GenericAdapter<T, VH> {

    public List<T> getSelectedItemList() {
        return selectedItemList;
    }

    private List<T> selectedItemList;

    public MultipleSelectionAdapter(@LayoutRes int layout, Class<VH> mViewHolderClass, List<T> modelList) {
        super(layout, mViewHolderClass, modelList);
        selectedItemList = new ArrayList<>();
    }

    public void selectItem(T data) {
        selectedItemList.add(data);
    }

    public void unSelectItem(T data) {
        selectedItemList.remove(data);
    }
}
