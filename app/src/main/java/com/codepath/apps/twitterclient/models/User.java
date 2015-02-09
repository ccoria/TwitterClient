package com.codepath.apps.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ccoria on 2/9/15.
 */
public class User implements Serializable {

    public static User newFromJSON(JSONObject user) {
        try {
            return new User(
                    user.getInt("id"),
                    user.getString("name"),
                    user.getString("screen_name"),
                    user.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    int id;
    String name;
    String screenName;
    String profileImageURL;

    //TODO: integrate with tweet and create on the database
    public User(int id, String name, String screenName, String profileImageURL) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.profileImageURL = profileImageURL;
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
}
