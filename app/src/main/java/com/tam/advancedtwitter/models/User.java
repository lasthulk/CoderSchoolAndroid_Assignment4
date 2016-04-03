package com.tam.advancedtwitter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by toan on 3/27/2016.
 */
@Parcel
public class User {

    private static final String AT_CHAR = "@";
    String name;
    long uid;
    String screenName;
    String profileImageUrl;
    String description;
    int followersCount;
    int followingsCount;

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public String getProfileDescription() {
        return description;
    }

    public User() {
    }
    public String getFullName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getFormatingScreenName() {
        return AT_CHAR + screenName;
    }

    public String getScreenName() {
        return  this.screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJson(JSONObject jsonObject) {
        User u = new User();
        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.description = jsonObject.getString("description");
            u.followersCount = jsonObject.getInt("followers_count");
            u.followingsCount = jsonObject.getInt("friends_count");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return u;
    }
}
