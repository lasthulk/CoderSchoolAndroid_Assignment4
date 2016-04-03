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

/**
 * Created by toan on 4/3/2016.
 */
public class UserTimeLineFragment extends TweetsListFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, view);
//        return view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment userFragment = new UserTimeLineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", screenName);
        userFragment.setArguments(bundle);
        return userFragment;
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
            String screenName = getArguments().getString("screen_name");
            this.client.getUserTimeline(maxId, screenName, new JsonHttpResponseHandler() {
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

}
