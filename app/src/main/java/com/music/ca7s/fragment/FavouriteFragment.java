package com.music.ca7s.fragment;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.ca7s.R;
import com.music.ca7s.adapter.HomeVPagerAdapter;
import com.music.ca7s.utils.CustomViewPager;
import com.music.ca7s.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.music.ca7s.fragment.ViewPagerPlaylistFragment.viewPagerPlaylistFragment;

public class FavouriteFragment extends BaseFragment {
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    Unbinder unbinder;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vpager)
    CustomViewPager vpager;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    private List<BaseFragment> fragmentList;
    private HomeVPagerAdapter homeVPagerAdapter;
    private int int_items = 3 ;

    public static FavouriteFragment newInstance() {
        Bundle args = new Bundle();
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(true);
        txtTopbarTitle.setText(R.string.favourites);
        imgTopbarRight.setVisibility(View.GONE);
        ll_main.setBackgroundColor(Color.WHITE);
        try {
            setupTabandFragment();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.e("onResume");
//        vpager.setCurrentItem(0);

    }

    private void setupTabandFragment() {
        vpager.setAdapter(new MyAdapter(getChildFragmentManager()));
        if (tabLayout != null && vpager != null) {
            tabLayout.setupWithViewPager(vpager);
        }
        assert vpager != null;
        vpager.setPagingEnabled(false);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    vpager.setCurrentItem(0);
                }else if (tab.getPosition() == 1){
                    vpager.setCurrentItem(1);
                }else if (tab.getPosition() == 2){
                    vpager.setCurrentItem(2);
                    try {
                        viewPagerPlaylistFragment.onResume();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ViewPagerAlbumFragment();
                case 1:
                    return new ViewPagerSongsFragment();
                case 2:
                    return new ViewPagerPlaylistFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getString(R.string.collections);
                case 1:
                    return getString(R.string.songs);
                case 2:
                    return getString(R.string.playlist_favouritr);
            }
            return null;
        }
    }

    private List<BaseFragment> getFragmentList() {
        List<BaseFragment> fragments = new ArrayList<>();
        ViewPagerAlbumFragment albumFragment = new ViewPagerAlbumFragment();
        fragments.add(albumFragment);
        ViewPagerSongsFragment songsFragment = new ViewPagerSongsFragment();
        fragments.add(songsFragment);
        ViewPagerPlaylistFragment playlistFragment = new ViewPagerPlaylistFragment();
        fragments.add(playlistFragment);
        return fragments;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.openDrawer();
                break;
            case R.id.imgTopbarRight:
                break;
        }
    }

    }
