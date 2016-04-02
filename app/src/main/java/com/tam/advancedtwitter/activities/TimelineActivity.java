package com.tam.advancedtwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.tam.advancedtwitter.adapters.TweetsAdapter;
import com.tam.advancedtwitter.helpers.NetworkHelper;
import com.tam.advancedtwitter.listeners.EndlessRecyclerViewScrollListener;
import com.tam.advancedtwitter.models.Tweet;
import com.tam.advancedtwitter.models.User;
import com.tam.advancedtwitter.network.TwitterApplication;
import com.tam.advancedtwitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

//import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();
    private static final int CREATE_TWEET_CODE = 305;
    static ArrayList<Tweet> tweetArrayList = new ArrayList<>();
    TwitterClient client;
    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Bind(R.id.bnOpenCompose)
    FloatingActionButton bnOpenCompose;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    TweetsAdapter tweetsAdapter;
    long maxId = 0;
    ArrayList<Tweet> newTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initComponents();
        getDefaultTimeline();
    }

    private void initComponents() {
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvTweets.setItemAnimator(new SlideInUpAnimator());
        this.client = TwitterApplication.getRestClient();
        tweetsAdapter = new TweetsAdapter(tweetArrayList);
        rvTweets.setAdapter(tweetsAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayout.scrollToPosition(0);
        rvTweets.setLayoutManager(linearLayout);
//        rvTweets.setHasFixedSize(true);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (tweetArrayList.size() > 0) {
                    maxId = tweetArrayList.get(tweetArrayList.size()-1).getUid();
                }
                Log.d(TAG, "onLoadMore: " + String.valueOf(page));
                getTimelineData(maxId, totalItemsCount);
            }
        });
        bnOpenCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i, CREATE_TWEET_CODE);
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDefaultTimeline();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_TWEET_CODE && resultCode == RESULT_OK) {
            User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));
            String tweetContent = data.getStringExtra("tweetConent");
            final Tweet newTweet = new Tweet();
            newTweet.setUser(user);
            newTweet.setBody(tweetContent);
            tweetArrayList.add(0, newTweet);
            tweetsAdapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
            if (!NetworkHelper.isOnline()) {
                Toast.makeText(TimelineActivity.this, "Cannot connect to Internet", Toast.LENGTH_SHORT).show();
                return;
            }
            client.postNewTweet(newTweet.getBody(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        super.onSuccess(statusCode, headers, response);
                        int previousTweetIndex = tweetArrayList.indexOf(newTweet);
                        tweetArrayList.set(previousTweetIndex, Tweet.fromJson(response));
                        tweetsAdapter.notifyItemChanged(previousTweetIndex);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    try {
                        Log.d(TAG, "onFailure: " + errorResponse.toString());
                        tweetArrayList.remove(0);
                        tweetsAdapter.notifyItemRemoved(0);
                        rvTweets.scrollToPosition(0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private void getDefaultTimeline() {
        this.maxId = 0;
        getTimelineData(0, 25);
    }

    private void getTimelineData(final long maxId, int totalItemsCount) {
        try {
            if (!NetworkHelper.isOnline()) {
                Toast.makeText(TimelineActivity.this, "Cannot connect to Internet", Toast.LENGTH_SHORT).show();
                return;
            }
            if (maxId == 0) {
                tweetsAdapter.clear();
            }
            this.client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(TAG, "onSuccess: " + response.toString());
                    newTweets = Tweet.fromJSONArray(response);

//                    tweetArrayList.addAll(newTweets);
//
//                    int currentSize = tweetsAdapter.getItemCount();
//                    tweetsAdapter.notifyItemRangeInserted(currentSize, newTweets.size() - 1);
                    Log.d(TAG, "count list: " + String.valueOf(tweetArrayList.size()));
                    tweetsAdapter.addAll(newTweets);
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, "onFailure: " + errorResponse.toString());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
