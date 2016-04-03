package com.tam.advancedtwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.tam.advancedtwitter.adapters.TweetsFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

//import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();
    private static final int CREATE_TWEET_CODE = 305;
    @Bind(R.id.tabsStrip)
    PagerSlidingTabStrip tabsStrip;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

//    @Bind(R.id.bnOpenCompose)
//    FloatingActionButton bnOpenCompose;

    //TweetsListFragment tweetsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //TweetsFragmentPagerAdapter pagerAdapter = new TweetsFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new TweetsFragmentPagerAdapter(getSupportFragmentManager()));
        tabsStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
//        bnOpenCompose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//                startActivityForResult(i, CREATE_TWEET_CODE);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item) {
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CREATE_TWEET_CODE && resultCode == Activity.RESULT_OK) {
//            User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));
//            String tweetContent = data.getStringExtra("tweetConent");
//            final Tweet newTweet = new Tweet();
//            newTweet.setUser(user);
//            newTweet.setBody(tweetContent);
//            postNewTweet(newTweet);
//        }
//    }

}
