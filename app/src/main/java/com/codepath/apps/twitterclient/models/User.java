package com.codepath.apps.twitterclient.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ccoria on 2/9/15.
 */
public class User implements Serializable {
    public static String TAG = "**********>> USER";

    public static User newFromJSON(JSONObject user) {
        try {
            return new User(
                    user.getInt("id"),
                    user.getString("name"),
                    user.getString("screen_name"),
                    user.getString("profile_image_url"),
                    user.getString("profile_image_url"),
                    user.getInt("followers_count"),
                    user.getInt("friends_count"),
                    user.getInt("statuses_count"));
        } catch (JSONException e) {
            Log.e(User.TAG, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    int id;
    String name;
    String screenName;
    String profileImageURL;
    String profileBackgrountURL;
    int followersCount;
    int followingCount;
    int tweetsCount;

    //TODO: integrate with tweet and create on the database
    public User(int id,
                String name,
                String screenName,
                String profileImageURL,
                String profileBackgrountURL,
                int followersCount,
                int followingCount,
                int tweetsCount) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.profileImageURL = profileImageURL;
        this.profileBackgrountURL = profileBackgrountURL;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.tweetsCount = tweetsCount;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public String getProfileBackgrountURL() {
        return profileBackgrountURL;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPrettyScreenName() {
        return "@" + screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }
}
