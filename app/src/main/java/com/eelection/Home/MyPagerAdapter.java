package com.eelection.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.eelection.Home.Fragments.ElectionFragment;
import com.eelection.Home.Fragments.ResultFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    MyPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ElectionFragment();
            case 1: return new ResultFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override    public CharSequence getPageTitle(int position) {        switch (position){
        case 0: return "Election";
        case 1: return "Results";
        default: return null;
    }
    }
}
