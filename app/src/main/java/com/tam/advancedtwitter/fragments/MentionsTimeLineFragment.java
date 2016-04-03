package com.tam.advancedtwitter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tam.advancedtwitter.helpers.NetworkHelper;
import com.tam.advancedtwitter.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.ButterKnife;

public class MentionsTimeLineFragment extends TweetsListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void getMoreData(long maxId, int totalItemsCount) {
        try {
            if (!NetworkHelper.isOnline()) {
                Toast.makeText(getActivity(), "Cannot connect to Internet", Toast.LENGTH_SHORT).show();
                return;
            }
            if (maxId == 0) {
                tweetsAdapter.clear();
            }
            this.client.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    newTweets = Tweet.fromJSONArray(response);
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

    @Override
    public void getDefaultTimeline() {
        this.maxId = 0;
        getMoreData(0, 25);
    }
}
