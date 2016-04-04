package com.tam.advancedtwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tam.advancedtwitter.fragments.UserTimeLineFragment;
import com.tam.advancedtwitter.models.User;
import com.tam.advancedtwitter.network.TwitterApplication;
import com.tam.advancedtwitter.network.TwitterClient;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    String screenName;

    @Bind(R.id.ivProfileUserImage)
    RoundedImageView ivProfileUserImage;

    @Bind(R.id.tvProfileFullName)
    TextView tvProfileFullName;

    @Bind(R.id.tvProfileDescription)
    TextView tvProfileDescription;

    @Bind(R.id.tvProfileFollower)
    TextView tvProfileFollower;

    @Bind(R.id.tvProfileFollowing)
    TextView tvProfileFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ButterKnife.bind(this);
        screenName = getIntent().getStringExtra("my_screen_name");
        this.client = TwitterApplication.getRestClient();
        //client.getCurrentUser(new JsonHttpResponseHandler() {
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                getSupportActionBar().setTitle(user.getFormatingScreenName());
                displayHeaderInformation(user);
//                screenName = user.getScreenName();
//                UserTimeLineFragment userFragment = UserTimeLineFragment.newInstance(screenName);
//                // display a user fragment within a activity dynamically
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.flContainer, userFragment);
//                ft.commit();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("getCurrentUser", "onFailure: " + responseString);
            }
        });

        Log.d("Check screenName", "screenName: " + screenName);
        String viewType = getIntent().getStringExtra("whoview");
        if (savedInstanceState == null) {
            Log.d("savedInstanceState", "savedInstanceState");
            UserTimeLineFragment userFragment = UserTimeLineFragment.newInstance(screenName);
            // display a user fragment within a activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userFragment);
            ft.commit();
        }
    }

    private void displayHeaderInformation(User user) {
        tvProfileFollower.setText(user.getFollowersCount() + " Follower(s)");
        tvProfileFollowing.setText(user.getFollowingsCount() + " Following");
        tvProfileFullName.setText(user.getFullName());
        tvProfileDescription.setText(user.getProfileDescription());
        Glide.with(this).load(user.getProfileImageUrl())
                .fitCenter()
                .centerCrop()
                .into(ivProfileUserImage);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
}
