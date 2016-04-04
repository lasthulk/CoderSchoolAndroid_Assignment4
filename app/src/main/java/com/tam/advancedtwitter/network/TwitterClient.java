package com.tam.advancedtwitter.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "rPXtm2jicIJxC1NLFI9lb1wNz";       // Change this
    public static final String REST_CONSUMER_SECRET = "P5iE7NvVkbf8GVvSh6Ga2GIC1iHqt6HJBjyqPhRKNIhMeanSSd"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpsimpletwitter"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, handler);
    }

    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(long max_id, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        if (max_id > 1) {
            params.put("max_id", max_id);
        }
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        if (max_id > 1) {
            params.put("max_id", max_id);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        if (max_id > 1) {
            params.put("max_id", max_id);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void postNewTweet(String newTweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        getClient().post(apiUrl, new RequestParams("status", newTweet), handler);
    }
}