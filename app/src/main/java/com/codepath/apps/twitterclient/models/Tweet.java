package com.codepath.apps.twitterclient.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
        import org.json.JSONObject;

        import com.activeandroid.Model;
        import com.activeandroid.annotation.Column;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(this.getCreatedAt()).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(
                    dateMillis,
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS).toString();

            relativeDate = relativeDate
                    .replace(" seconds ago","s")
                    .replace("in 1 second","1s")
                    .replace(" minutes ago","m")
                    .replace("in 1 minute","1m")
                    .replace(" hours ago","h")
                    .replace("in 1 hour","1h")
                    .replace(" days ago","d")
                    .replace("in 1 day","1d");

            // TODO Smarter solution (use lib)
            // MutablePeriod period = new MutablePeriod(dateMillis, System.currentTimeMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
