package com.music.ca7s.adapter.generic_adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.music.ca7s.R;
import com.music.ca7s.model.toplist.TopListDatum;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class SlidingImageAdapter extends PagerAdapter {


//    private ArrayList<Integer> IMAGES;
    private List<TopListDatum> listData;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, final List<TopListDatum> listData) {
        this.context = context;
//        this.IMAGES=IMAGES;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.row_home_album, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imgTopPic);

        String playlistImage = listData.get(position).getImageUrl();
//                String playlistImage = profileData.get(0).getProfilePicture();
        if(playlistImage != null && !playlistImage.isEmpty()) {
            if (playlistImage.contains("/index.php")) {
                playlistImage = playlistImage.replace("/index.php", "");
            }
            Glide.with(context).
                    load(playlistImage)
                    .placeholder(R.drawable.ic_top_placeholder)
                    .error(R.drawable.ic_top_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                    .into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listData.get(position).getUrl()));
                context.startActivity(browserIntent);
            }
        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}