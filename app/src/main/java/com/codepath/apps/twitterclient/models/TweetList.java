package com.codepath.apps.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ccoria on 2/8/15.
 */
public class TweetList extends ArrayList<Tweet> {

    public TweetList(JSONArray jsonArray) {

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            add(tweet);
        }
    }

    @Override
    public Tweet get(int index) {
        return (Tweet)super.get(index);
    }
}
