package com.tam.advancedtwitter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.tam.advancedtwitter.network.TwitterApplication;
import com.tam.advancedtwitter.network.TwitterClient;
import com.tam.advancedtwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = TimelineActivity.class.getSimpleName();

    @Bind(R.id.ivClose)
    ImageView ivClose;

    @Bind(R.id.etTweetInput)
    EditText etTweetInput;

    @Bind(R.id.tvCharactersLeft)
    TextView tvCharactersLeft;

    @Bind(R.id.bnTweet)
    Button bnTweet;

    @Bind(R.id.ivComposeProfileImage)
    RoundedImageView ivComposeProfileImage;

    @Bind(R.id.tvComposeUserName)
    TextView tvComposeUserName;

    @Bind(R.id.tvComposeName)
    TextView tvComposeName;

    String currentTweetContent = "";
    int maxLengthTweet;
    TwitterClient client;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        getUser();
        initEvents();
    }

    private void initComponents() {
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        tvComposeName.setText("");
        tvComposeUserName.setText("");
        maxLengthTweet = getResources().getInteger(R.integer.max_length_tweet);
        this.client = TwitterApplication.getRestClient();
    }

    private void initEvents() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        etTweetInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentTweetContent = s.toString();
                countCharactersLeft();
            }
        });

        bnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(currentTweetContent)) {
//                    Toast.makeText(ComposeActivity.this, "Please input tweet content.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                    intent.putExtra("tweetConent", currentTweetContent);
                    intent.putExtra("user", Parcels.wrap(currentUser));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void getUser() {
        Intent data = getIntent();
//        currentUser = (User) Parcels.unwrap(data.getParcelableExtra("user"));
        if (currentUser == null) {
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    currentUser = User.fromJson(response);
                    displayUserInfo();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, "onFailure: " + errorResponse.toString());
                }
            });
        }
    }

    private void displayUserInfo() {
        if (currentUser == null || TextUtils.isEmpty(currentUser.getProfileImageUrl())) {
            return;
        }
        tvComposeUserName.setText(currentUser.getFormatingScreenName());
        tvComposeName.setText(currentUser.getFullName());
        Context context = ivComposeProfileImage.getContext();
        Glide.with(context).load(currentUser.getProfileImageUrl())
                .fitCenter()
                .centerCrop()
                .into(ivComposeProfileImage);
    }

    private void countCharactersLeft() {
        if (TextUtils.isEmpty(currentTweetContent)) {
            return;
        }

        tvCharactersLeft.setText(String.valueOf(maxLengthTweet - currentTweetContent.length()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
