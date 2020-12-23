package com.music.ca7s.fragment;

import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.ca7s.R;
import com.music.ca7s.adapter.HomeVPagerAdapter;
import com.music.ca7s.utils.CustomViewPager;
import com.music.ca7s.utils.DebugLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyMusicFragment extends BaseFragment  {
    private static final int STORAGE_PERMISSION_CODE = 1;
    TabLayout tabLayout;
    CustomViewPager vpager;
    ImageView imgTopbarLeft;
    TextView txtTopbarTitle;
    ImageView imgTopbarRight;
    RelativeLayout relativeTopBar;
    private long mLastClickTime = 0;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private List<BaseFragment> fragmentList;
    private HomeVPagerAdapter homeVPagerAdapter;
    private ArrayList<String> urls;
    ProgressDialog dialog;
    private int int_items = 2 ;
    private View view;
    public static MyMusicFragment newInstance() {

        Bundle args = new Bundle();

        MyMusicFragment fragment = new MyMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mymusic, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTopbarTitle = view.findViewById(R.id.txtTopbarTitle);
        imgTopbarRight = view.findViewById(R.id.imgTopbarRight);
        relativeTopBar = view.findViewById(R.id.relativeTopBar);
        imgTopbarLeft = view.findViewById(R.id.iv_close_player);
        imgTopbarLeft.setOnClickListener(this);
        relativeTopBar.setOnClickListener(this);
        tabLayout = view.findViewById(R.id.tab_layout);
        vpager = view.findViewById(R.id.vpager);
        txtTopbarTitle.setText(homeActivity.getString(R.string.downloads));
        imgTopbarRight.setVisibility(View.GONE);
        homeActivity.setSlidingState(true);
        setupTabandFragment();
    }

    public void setupTabandFragment() {
        vpager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(vpager);
            }
        });
        vpager.setPagingEnabled(false);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    vpager.setCurrentItem(0);
                }else if (tab.getPosition() == 1){
                    vpager.setCurrentItem(1);
                    ViewPagerDownloadPlaylistFragment.getInstance().setData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : return new ViewPagerDownloadSongsFragment();
                case 1 : return new ViewPagerDownloadPlaylistFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return homeActivity.getString(R.string.songs);
                case 1 :
                    return homeActivity.getString(R.string.playlist);
            }
            return null;
        }
    }

    private List<BaseFragment> getFragmentList() {
        List<BaseFragment> fragments = new ArrayList<>();
        ViewPagerDownloadSongsFragment albumFragment = new ViewPagerDownloadSongsFragment();
        fragments.add(albumFragment);
        ViewPagerDownloadPlaylistFragment songsFragment = new ViewPagerDownloadPlaylistFragment();
        fragments.add(songsFragment);
        return fragments;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private boolean checkMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
//        DebugLog.e("onResume");
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.iv_close_player) {
            homeActivity.openDrawer();
        }
    }
}
