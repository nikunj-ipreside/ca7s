package com.music.ca7s.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.music.ca7s.fragment.BaseFragment;

import java.util.List;

/**
 * Created by Nikunj Dhokia.
 */

public class HomeVPagerAdapter extends FragmentStatePagerAdapter {
    List<BaseFragment> baseFragments;

    public HomeVPagerAdapter(FragmentManager fm, List<BaseFragment> baseFragments) {
        super(fm);
        this.baseFragments = baseFragments;
    }

    @Override
    public Fragment getItem(int i) {
        return baseFragments.get(i);
    }

    @Override
    public int getCount() {
        return baseFragments.size();
    }
}
