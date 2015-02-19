package com.codepath.apps.twitterclient.uihelpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.ProfileActivity;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.TweetList;
import com.squareup.picasso.Picasso;

/**
 * Created by ccoria on 2/8/15.
 */
public class TwitterArrayAdapter extends ArrayAdapter<Tweet> {

    public TwitterArrayAdapter(Context context) {
        super(context, R.layout.tweet);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = (Tweet) getItem(position);
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

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                profileIntent.putExtra("screen_name", tweet.getUserScreenName());
                getContext().startActivity(profileIntent);
            }
        });

        return convertView;
    }

    public void addTweets(TweetList tweets){
        //Log.d(TAG, "Adding From: " + tweets.get(0).getText());
        //Log.d(TAG, "Adding To: " + tweets.get(tweets.size()-1).getText());
        adapterInstance.addAll(tweets);
        adapterInstance.notifyDataSetChanged();
    }

    private static TwitterArrayAdapter adapterInstance;
    public static TwitterArrayAdapter getAdapter(Context context, ListView listView) {
        if(adapterInstance == null) {
            adapterInstance = new TwitterArrayAdapter(context);
            listView.setAdapter(adapterInstance);
        }

        return adapterInstance;
    }
}
