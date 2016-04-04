package com.tam.advancedtwitter.models;

import com.tam.advancedtwitter.helpers.TimeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet {
    private String body;
    private long uid;
    private User user;
    private String createdAt;
    private Media media;
    public Media getMedia() { return media; }

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
            JSONObject entitiesObject = jsonObject.optJSONObject("extended_entities");
//            if (entitiesObject != null) {
//                JSONArray mediaArray = entitiesObject.optJSONArray("media");
//                if (mediaArray != null && mediaArray.length() > 0) {
//                    String thumbnailImage = mediaArray.optJSONObject(0).getString("media_url");
//                    if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
//                        Log.d("media_url", "media_url: " + thumbnailImage);
//                    } else {
//                        Log.d("media_url", "No media_url ");
//                    }
//                }
//            }
            t.media = Media.getMedia(entitiesObject);
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
