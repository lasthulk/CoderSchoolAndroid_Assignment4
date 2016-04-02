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

    public User() {
    }
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJson(JSONObject jsonObject) {
        User u = new User();
        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = AT_CHAR + jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return u;
    }
}
