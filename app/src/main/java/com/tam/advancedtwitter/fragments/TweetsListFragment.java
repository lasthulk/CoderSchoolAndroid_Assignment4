package com.tam.advancedtwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitter.R;
import com.tam.advancedtwitter.adapters.TweetsAdapter;
import com.tam.advancedtwitter.listeners.EndlessRecyclerViewScrollListener;
import com.tam.advancedtwitter.models.Tweet;
import com.tam.advancedtwitter.network.TwitterApplication;
import com.tam.advancedtwitter.network.TwitterClient;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public abstract class TweetsListFragment extends Fragment {
    protected static TwitterClient client;
    private static LinearLayoutManager linearLayout;

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    protected String TAG = TweetsListFragment.class.getSimpleName();
    protected ArrayList<Tweet> tweetArrayList = new ArrayList<>();
    protected long maxId = 0;
    protected ArrayList<Tweet> newTweets;
    TweetsAdapter tweetsAdapter;

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
        getDefaultTimeline();
        return view;
    }

    public abstract void getMoreData(long maxId, int totalItemsCount);

    public abstract void getDefaultTimeline();
}
