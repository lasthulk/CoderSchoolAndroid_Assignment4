package com.tam.advancedtwitter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tam.advancedtwitter.fragments.HomeTimeLineFragment;
import com.tam.advancedtwitter.fragments.MentionsTimeLineFragment;

/**
 * Created by toan on 4/3/2016.
 */
public class TweetsFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = { "Home", "Mentions" };

    public TweetsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimeLineFragment();
        } else if (position == 1) {
            return new MentionsTimeLineFragment();
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
