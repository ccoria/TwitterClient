package com.codepath.apps.twitterclient.models;

import android.util.Log;

import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
        import org.json.JSONObject;

        import com.activeandroid.Model;
        import com.activeandroid.annotation.Column;

@Table(name = "Tweets")

/**
 * Created by ccoria on 2/8/15.
 */
public class Tweet extends Model {
    String TAG = this.getClass().getName();

    // Define database columns and associated fields
    @Column(name = "userId")
    String userId;
    @Column(name = "userName")
    String userName;
    @Column(name = "userPicture")
    String userPicture;
    @Column(name = "userScreenName")
    String userScreenName;
    @Column(name = "createdAt")
    String createdAt;
    @Column(name = "text")
    String text;

    public Tweet(JSONObject object){
        super();

        try {
            JSONObject user = object.getJSONObject("user");
            this.userId = user.getString("id");
            this.userName = user.getString("name");
            this.userPicture = user.getString("profile_image_url");
            this.userScreenName = user.getString("screen_name");
            this.createdAt = object.getString("created_at");
            this.text = object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public String getText() {
        return text;
    }

    // load a remote image url into a particular ImageView
    //

    public static TweetList fromJson(JSONArray jsonArray) {
        TweetList tweets = new TweetList();

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
            tweets.add(tweet);
        }

        return tweets;
    }
}
