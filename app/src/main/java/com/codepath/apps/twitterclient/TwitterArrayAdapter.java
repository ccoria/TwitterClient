package com.codepath.apps.twitterclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.TweetList;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ccoria on 2/8/15.
 */
class TwitterArrayAdapter extends ArrayAdapter<Tweet> {

    TwitterArrayAdapter(Context context, TweetList objects) {
        super(context, R.layout.tweet, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = (Tweet) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.tweet, parent, false);
        }

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvUserScreenName = (TextView) convertView.findViewById(R.id.tvUserScreenName);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);

        Picasso.with(this.getContext()).load(tweet.getUserPicture())
                .noFade().fit().into(ivProfileImage);
        tvUserName.setText(tweet.getUserName());
        tvUserScreenName.setText("@"+tweet.getUserScreenName());
        tvText.setText(tweet.getText());
        tvRelativeTime.setText(tweet.getRelativeTimeAgo());

        return convertView;
    }
}
