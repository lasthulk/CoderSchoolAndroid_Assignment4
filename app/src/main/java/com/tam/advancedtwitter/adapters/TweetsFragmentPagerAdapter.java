package com.tam.advancedtwitter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tam.advancedtwitter.fragments.HomeTimeLineFragment;
import com.tam.advancedtwitter.fragments.MentionsTimeLineFragment;


public class TweetsFragmentPagerAdapter extends SmartFragmentStatePagerAdapter { //FragmentPagerAdapter {

    private String tabTitles[] = { "Home", "Mentions" };

    public TweetsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            //return new HomeTimeLineFragment();
            return HomeTimeLineFragment.newInstance("");
        } else if (position == 1) {
            return MentionsTimeLineFragment.newInstance("");
        }
        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
