package com.tam.advancedtwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tam.advancedtwitter.activities.ProfileActivity;
import com.tam.advancedtwitter.models.Media;
import com.tam.advancedtwitter.models.Tweet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> {

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

//        @Bind(R.id.ivProfileImage)
//        ImageView ivProfileImage;

        @Bind(R.id.ivProfileImage)
        RoundedImageView ivProfileImage;

        @Bind(R.id.tvBody)
        TextView tvBody;

        @Bind(R.id.tvUserName)
        TextView tvUserName;

        @Bind(R.id.tvTimeStamp)
        TextView tvTimeStamp;

        @Bind(R.id.ivThumbnailImage)
        ImageView ivThumbnailImage;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Tweet> tweets;

    public TweetsAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View viewItemTweet = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        TweetViewHolder holder = new TweetViewHolder(viewItemTweet);

        return holder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        final Tweet tweet = this.tweets.get(position);
        holder.tvBody.setText(tweet.getBody());
        holder.tvUserName.setText(tweet.getUser().getFormatingScreenName());
        holder.tvTimeStamp.setText(tweet.getCreatedAt());
        final Context context = holder.ivProfileImage.getContext();
        Glide.with(context).load(tweet.getUser().getProfileImageUrl())
                .fitCenter()
                .centerCrop()
                .into(holder.ivProfileImage);
        Media media = tweet.getMedia();
        if (media != null) {

            String thumbmail = media.getThumbnailImage();
            if (!thumbmail.isEmpty()) {
                holder.ivThumbnailImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(thumbmail)
                        .fitCenter()
                        .into(holder.ivThumbnailImage);
            } else {
                holder.ivThumbnailImage.setVisibility(View.GONE);
            }
        } else {
            holder.ivThumbnailImage.setVisibility(View.GONE);
        }
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("my_screen_name", String.valueOf(tweet.getUser().getScreenName()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

    public void clear() {
        this.tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        this.tweets.addAll(list);
        notifyDataSetChanged();
    }
}
