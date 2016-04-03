package com.tam.advancedtwitter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tam.advancedtwitter.activities.ComposeActivity;
import com.tam.advancedtwitter.helpers.NetworkHelper;
import com.tam.advancedtwitter.models.Tweet;
import com.tam.advancedtwitter.models.User;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by toan on 4/3/2016.
 */
public class HomeTimeLineFragment extends TweetsListFragment {

    private static final int CREATE_TWEET_CODE = 305;

    @Bind(R.id.bnOpenCompose)
    FloatingActionButton bnOpenCompose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);
        bnOpenCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                Intent i = new Intent(getActivity(), ComposeActivity.class);
                startActivityForResult(i, CREATE_TWEET_CODE);
            }
        });
        return view;
    }

    @Override
    public void getTimelineData(long maxId, int totalItemsCount) {
        try {
            if (!NetworkHelper.isOnline()) {
                Toast.makeText(getActivity(), "Cannot connect to Internet", Toast.LENGTH_SHORT).show();
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
//                    tweetArrayList.addAll(newTweets);//
//                    int currentSize = tweetsAdapter.getItemCount();
//                    tweetsAdapter.notifyItemRangeInserted(currentSize, newTweets.size() - 1);
                    Log.d(TAG, "count list: " + String.valueOf(tweetArrayList.size()));
                    tweetsAdapter.addAll(newTweets);
                    //tweetsListFragment.addAll(newTweets);
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

    @Override
    public void getDefaultTimeline() {
        this.maxId = 0;
        getTimelineData(0, 25);
    }

    private void postNewTweet(final Tweet newTweet) {
        if (!NetworkHelper.isOnline()) {
            Toast.makeText(getActivity(), "Cannot connect to Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        tweetArrayList.add(0, newTweet);
        tweetsAdapter.notifyItemInserted(0);
        rvTweets.smoothScrollToPosition(0);
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
}
