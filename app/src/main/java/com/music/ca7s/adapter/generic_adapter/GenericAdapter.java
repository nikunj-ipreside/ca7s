package com.music.ca7s.adapter.generic_adapter;


import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {

    protected final Class<VH> mViewHolderClass;
    public List<T> modelList;
    protected int layout;

    public GenericAdapter(@LayoutRes int layout, Class<VH> mViewHolderClass, List<T> modelList) {
        this.modelList = modelList;
        this.layout = layout;
        this.mViewHolderClass = mViewHolderClass;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        try {
            Constructor<VH> constructor = mViewHolderClass.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T model = modelList.get(position);
        setViewHolderData(holder, model, position);
    }

    public abstract void loadMore();

    public abstract void setViewHolderData(VH viewHolderData, T data, int position);

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setRefreshedData(ArrayList<T> updatedlistData) {
        modelList = updatedlistData;
        notifyDataSetChanged();
    }
}