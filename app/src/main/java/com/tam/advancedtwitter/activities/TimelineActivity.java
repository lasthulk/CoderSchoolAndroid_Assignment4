package com.tam.advancedtwitter.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.tam.advancedtwitter.adapters.TweetsFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

//import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();

    @Bind(R.id.tabsStrip)
    PagerSlidingTabStrip tabsStrip;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        viewPager.setAdapter(new TweetsFragmentPagerAdapter(getSupportFragmentManager()));
        tabsStrip.setViewPager(viewPager);
    }

}
