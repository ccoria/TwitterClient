package com.codepath.apps.twitterclient.models;

import java.util.ArrayList;

/**
 * Created by ccoria on 2/8/15.
 */
public class TweetList extends ArrayList<Tweet> {
    @Override
    public Tweet get(int index) {
        return (Tweet)super.get(index);
    }
}
