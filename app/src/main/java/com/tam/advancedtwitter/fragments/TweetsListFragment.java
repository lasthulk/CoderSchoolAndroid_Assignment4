package com.tam.advancedtwitter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tam.advancedtwitter.activities.ComposeActivity;
import com.tam.advancedtwitter.adapters.TweetsAdapter;
import com.tam.advancedtwitter.helpers.NetworkHelper;
import com.tam.advancedtwitter.listeners.EndlessRecyclerViewScrollListener;
import com.tam.advancedtwitter.models.Tweet;
import com.tam.advancedtwitter.models.User;
import com.tam.advancedtwitter.network.TwitterApplication;
import com.tam.advancedtwitter.network.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public abstract class TweetsListFragment extends Fragment {
    protected static TwitterClient client;
    private static LinearLayoutManager linearLayout;
    protected static final int CREATE_TWEET_CODE = 305;
    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.bnOpenCompose)
    FloatingActionButton bnOpenCompose;

    protected String TAG = TweetsListFragment.class.getSimpleName();
    protected ArrayList<Tweet> tweetArrayList = new ArrayList<>();
    protected long maxId = 0;
    protected ArrayList<Tweet> newTweets;
    protected TweetsAdapter tweetsAdapter;


    public void postNewTweet(final Tweet newTweet) {
        if (!NetworkHelper.isOnline()) {
            Toast.makeText(getActivity(), "Cannot connect to Internet", Toast.LENGTH_SHORT).show();
            return;
        }
//        tweetArrayList.add(0, newTweet);
//        tweetsAdapter.notifyItemInserted(0);
//        rvTweets.smoothScrollToPosition(0);
        client.postNewTweet(newTweet.getBody(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    super.onSuccess(statusCode, headers, response);
//                    int previousTweetIndex = tweetArrayList.indexOf(newTweet);
//                    tweetArrayList.set(previousTweetIndex, Tweet.fromJson(response));
//                    tweetsAdapter.notifyItemChanged(previousTweetIndex);
                    getDefaultTimeline();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.d(TAG, "onFailure: " + errorResponse.toString());
//                    tweetArrayList.remove(0);
//                    tweetsAdapter.notifyItemRemoved(0);
                    rvTweets.scrollToPosition(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, view);
        rvTweets.setItemAnimator(new SlideInUpAnimator());
        tweetsAdapter = new TweetsAdapter(tweetArrayList);
        rvTweets.setAdapter(tweetsAdapter);
        this.client = TwitterApplication.getRestClient();

        linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayout.scrollToPosition(0);
        rvTweets.setLayoutManager(linearLayout);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (tweetArrayList.size() > 0) {
                    maxId = tweetArrayList.get(tweetArrayList.size() - 1).getUid();
                }
                Log.d(TAG, "onLoadMore: " + String.valueOf(page));
                getMoreData(maxId, totalItemsCount);
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDefaultTimeline();
            }
        });
        bnOpenCompose.setOnClickListener(new View.OnClickListener() {
                        @Override
            public void onClick(View view) {
                //Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                Intent i = new Intent(getActivity(), ComposeActivity.class);
                startActivityForResult(i, CREATE_TWEET_CODE);
            }
        });
        getDefaultTimeline();
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_TWEET_CODE && resultCode == Activity.RESULT_OK) {
            User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));
            String tweetContent = data.getStringExtra("tweetConent");
            final Tweet newTweet = new Tweet();
            newTweet.setUser(user);
            newTweet.setBody(tweetContent);
            postNewTweet(newTweet);
        }
    }


    public abstract void getMoreData(long maxId, int totalItemsCount);

    public void getDefaultTimeline() {
        this.maxId = 0;
        getMoreData(0, 25);
    }
}
