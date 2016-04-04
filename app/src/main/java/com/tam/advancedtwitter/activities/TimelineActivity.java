package com.tam.advancedtwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tam.advancedtwitter.adapters.TweetsFragmentPagerAdapter;
import com.tam.advancedtwitter.fragments.HomeTimeLineFragment;
import com.tam.advancedtwitter.models.Tweet;
import com.tam.advancedtwitter.models.User;
import com.tam.advancedtwitter.network.TwitterApplication;
import com.tam.advancedtwitter.network.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

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
//    @Bind(R.id.bnOpenCompose)
//    FloatingActionButton bnOpenCompose;

    TwitterClient client;
    User user;
    TweetsFragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();


        this.client = TwitterApplication.getRestClient();
        fetchUser();
        this.pagerAdapter = new TweetsFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(this.pagerAdapter);
        tabsStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
//        bnOpenCompose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//                if (user == null) {
//                    fetchUser();
//                }
//                i.putExtra("user", Parcels.wrap(user));
//                startActivityForResult(i, CREATE_TWEET_CODE);
//            }
//        });
    }

    private void fetchUser() {
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("getCurrentUser", "onFailure: " + responseString);
            }
        });
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
        intent.putExtra("my_screen_name", user.getScreenName());
        intent.putExtra("whoview", "me");
        startActivity(intent);
    }

    public void onOpenComposeView(MenuItem mi) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        //i.putExtra("user", Parcels.wrap(user));
        startActivityForResult(i, CREATE_TWEET_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_TWEET_CODE && resultCode == Activity.RESULT_OK) {
            User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));
            String tweetContent = data.getStringExtra("tweetConent");
            final Tweet newTweet = new Tweet();
            newTweet.setUser(user);
            newTweet.setBody(tweetContent);
            //client.postNewTweet(newTweet);
            //HomeTimeLineFragment tweetsListFragment = (HomeTimeLineFragment)this.pagerAdapter.getItem(0);
            HomeTimeLineFragment homeFragment = (HomeTimeLineFragment)this.pagerAdapter.getRegisteredFragment(0);
            if (homeFragment != null) {
                homeFragment.postNewTweet(newTweet);
            }
        }
    }

}
