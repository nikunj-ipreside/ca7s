package com.music.ca7s.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.contant.RecylerViewSongItemClickListener;
import com.music.ca7s.mediaplayer.Song;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Song> list;
    private RecylerViewSongItemClickListener listener;
    private ImageView imageView;
//    private OnSelectedPosition OnSelectedPosition;
    public MyPagerAdapter(Context context, ArrayList<Song> list,RecylerViewSongItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }
    /*
    This callback is responsible for creating a page. We inflate the layout and set the drawable
    to the ImageView based on the position. In the end we add the inflated layout to the parent
    container .This method returns an object key to identify the page view, but in this example page view
    itself acts as the object key
    */
    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_viewpagify, container,false);
         imageView = view.findViewById(R.id.imageView);
//        CardView cv_main = view.findViewById(R.id.cv_main);
        if (list.get(position).getSongImageUrl() != null && !list.get(position).getSongImageUrl().isEmpty()) {
            String playlistImage = list.get(position).getSongImageUrl();

            if (playlistImage.contains("/index.php")){
                playlistImage = playlistImage.replace("/index.php","");
            }
            if (HomeActivity.isDataSaverEnabled()) {
                Glide.with(context)
                        .load(R.drawable.logo_main_player)
                        .placeholder(R.drawable.logo_main_player)
                        .override(250,250)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                        .into(imageView);
            } else {
                if (playlistImage != null && !playlistImage.isEmpty()){
                    Glide.with(context)
                            .load(playlistImage)
                            .override(250,250)
                            .placeholder(R.drawable.logo_main_player)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(imageView);
                }
                else {
                    Glide.with(context)
                            .load(R.drawable.logo_main_player)
                            .override(250,250)
                            .placeholder(R.drawable.logo_main_player)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(imageView);
                }
            }
        }

        imageView.setId(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                listener.onImageSelected(id,list.get(id));
            }
        });
        ((ViewPager)container).addView(view);
//        container.addView(view,0);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    public void updateList(ArrayList<Song> updatedlist){
        list = updatedlist;
        notifyDataSetChanged();
    }
    /*
    This callback is responsible for destroying a page. Since we are using view only as the
    object key we just directly remove the view from parent container
    */
    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object view) {
        ((ViewPager)container).removeView((View) view);
    }

    /*
    Returns the count of the total pages
    */
    @Override
    public int getCount() {
        return list.size();
    }
    /*
    Used to determine whether the page view is associated with object key returned by instantiateItem.
    Since here view only is the key we return view==object
    */
    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view.equals(object);
    }

    public void updatePage(String playlistImage){
        if (playlistImage.contains("/index.php")){
            playlistImage = playlistImage.replace("/index.php","");
        }
        if (HomeActivity.isDataSaverEnabled()) {
            Glide.with(context)
                    .load(R.drawable.logo_main_player)
                    .placeholder(R.drawable.logo_main_player)
                    .override(250,250)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                    .into((ImageView) imageView);
        } else {
            if (playlistImage != null && !playlistImage.isEmpty()){
                Glide.with(context)
                        .load(playlistImage)
                        .override(250,250)
                        .placeholder(R.drawable.logo_main_player)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                        .into((ImageView) imageView);
            }
            else {
                Glide.with(context)
                        .load(R.drawable.logo_main_player)
                        .override(250,250)
                        .placeholder(R.drawable.logo_main_player)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                        .into((ImageView) imageView);
            }
        }
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
