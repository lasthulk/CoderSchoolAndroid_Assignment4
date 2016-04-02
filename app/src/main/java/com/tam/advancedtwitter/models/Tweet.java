package com.tam.advancedtwitter.models;

import com.tam.advancedtwitter.helpers.TimeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toan on 3/27/2016.
 */
public class Tweet {
    private String body;
    private long uid;
    private User user;
    private String createdAt;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet t = new Tweet();
        try {
            t.body = jsonObject.getString("text");
            t.uid = jsonObject.getLong("id");
            t.createdAt = TimeHelper.getTwitterRelativeTimeAgo(jsonObject.getString("created_at"));
            t.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return t;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(jsonObject);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
