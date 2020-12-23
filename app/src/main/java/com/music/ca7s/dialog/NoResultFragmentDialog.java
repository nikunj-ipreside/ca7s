package com.music.ca7s.dialog;


import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.listener.OnTryAginLIstener;
import com.music.ca7s.model.search_song.TapScanData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoResultFragmentDialog extends BaseFragmentDialog {
    Unbinder unbinder;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_song_name)
    TextView txtSongName;
    @BindView(R.id.txtArtistName)
    TextView txtArtistName;
    @BindView(R.id.txtTapToCopy)
    TextView txtTapToCopy;
    /* @BindView(R.id.btnPlay)
    Button btnPlay;*/


    List<TapScanData> tapScanData;

    String title;

    String artist;

    String imageUrl;
    private OnTryAginLIstener ontryAgailListener;

    HomeActivity homeActivity;

//    public void setTapScanData(List<TapScanData> tapScanData) {
//        this.tapScanData = tapScanData;
//    }

//    public void setTapScanData(List<TapScanData> tapScanData) {
//        this.tapScanData = tapScanData;
//    }

    public void setTapScanData(String title,OnTryAginLIstener ontryAgailListener) {
        this.title=title;
        this.ontryAgailListener = ontryAgailListener;

    }

//    public static TapScanFragmentDialog newInstance(List<TapScanData> tapScanData) {
//        TapScanFragmentDialog fragment = new TapScanFragmentDialog();
//        fragment.setTapScanData(tapScanData);
//        return fragment;
//    }

    public static NoResultFragmentDialog newInstance(String title, OnTryAginLIstener ontryAgailListener) {
        NoResultFragmentDialog fragment = new NoResultFragmentDialog();
        fragment.setTapScanData(title,ontryAgailListener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_no_result, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String playlistImage = imageUrl;

        if (playlistImage != null && !playlistImage.isEmpty()) {
            if (playlistImage.contains("/index.php")){
                playlistImage = playlistImage.replace("/index.php","");
            }
            Glide.with(getActivity()).load(playlistImage)
                    .placeholder(R.drawable.ic_top_placeholder)
                    .error(R.drawable.ic_top_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                    .into(imgLogo);
        }

        txtSongName.setText(title);
        txtArtistName.setText(artist);

        txtTapToCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           ontryAgailListener.onSearchAgain();
                dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


//    @OnClick(R.id.txtTapToCopy)
//    public void onViewClicked() {
//
//    }

}