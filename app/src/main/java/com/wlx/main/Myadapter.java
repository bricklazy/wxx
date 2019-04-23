package com.wlx.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

//适配器
public class Myadapter extends FragmentPagerAdapter {

    private List<String> titles;
    private List<Fragment> frags;

    public Myadapter(FragmentManager fm) {
        super(fm);
        titles = new ArrayList<>();
        frags = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void setDatas(List<String> titles, List<Fragment> frags){
        this.titles.clear();
        this.frags.clear();
        this.titles.addAll(titles);
        this.frags.addAll(frags);
    }
}