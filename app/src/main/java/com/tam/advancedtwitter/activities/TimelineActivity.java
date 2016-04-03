package com.tam.advancedtwitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.twitter.R;

//import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ButterKnife.bind(this);
        initComponents();
        //homeTimeLineFragment.getDefaultTimeline();
        //getDefaultTimeline();
    }

    private void initComponents() {
//        if (savedInstanceState != null) {
//            homeTimeLineFragment = (HomeTimeLineFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTimeline);
//        }
//        rvTweets.setHasFixedSize(true);
//        bnOpenCompose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//                startActivityForResult(i, CREATE_TWEET_CODE);
//            }
//        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CREATE_TWEET_CODE && resultCode == RESULT_OK) {
//            User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));
//            String tweetContent = data.getStringExtra("tweetConent");
//            final Tweet newTweet = new Tweet();
//            newTweet.setUser(user);
//            newTweet.setBody(tweetContent);
//            //homeTimeLineFragment.postNewTweet(newTweet);
//        }
//    }
}
