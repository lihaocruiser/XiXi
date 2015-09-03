package com.xixi.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xixi.R;
import com.xixi.adapter.cardview.CircleCardViewHolder;
import com.xixi.adapter.cardview.MagpieCardViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private int FRAGMENT_COUNT = 3;
    private Fragment fragmentMagpie;
    private Fragment fragmentCircle;
    private Fragment fragmentMe;

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (fragmentMagpie == null) {
                    initMagpie();
                }
                return fragmentMagpie;
            case 1:
                if (fragmentCircle == null) {
                    initCircle();
                }
                return fragmentCircle;
            default:
                if (fragmentMe == null) {
                    initMe();
                }
                return fragmentMe;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "鹊桥";
            case 1:
                return "小圈子";
            default:
                return "我";
        }
    }

    private void initMagpie() {
        Bundle magpieBundle = new Bundle();
        magpieBundle.putInt("resId", R.layout.cardview_magpie_list);
        magpieBundle.putSerializable("clazz", MagpieCardViewHolder.class);
        fragmentMagpie = new FragmentMagpie();
        fragmentMagpie.setArguments(magpieBundle);
    }

    private void initCircle() {
        Bundle circleBundle = new Bundle();
        circleBundle.putInt("resId", R.layout.cardview_circle_list);
        circleBundle.putSerializable("clazz", CircleCardViewHolder.class);
        fragmentCircle = new FragmentCircle();
        fragmentCircle.setArguments(circleBundle);
    }

    private void initMe() {
        fragmentMe = new FragmentMe();
    }

}
