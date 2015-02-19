package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.TweetList;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.rest.TwitterApplication;
import com.codepath.apps.twitterclient.uihelpers.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends ActionBarActivity {
    public String TAG = "**********>> " + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        getUserInfo(screenName);
    }

    public void setUpFragment(String userScreen) {
        Log.d(TAG, "setting up fragment for " + userScreen);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.frame_stream, StreamFragment.newInstance(2, userScreen));
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Execute the changes specified
        ft.commit();
    }

    public void getUserInfo(String screenName) {
        Log.d(TAG, "getting user " + screenName);
        TwitterApplication.getRestClient().getUsersLookup(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    JSONObject jsonUser = response.getJSONObject(0);
                    populateUserPage(User.newFromJSON(jsonUser));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    public void populateUserPage(final User user){
        setUpFragment(user.getScreenName());

        Log.d(TAG, "populating user page " + user.getName());
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName_profile);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName_profile);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage_profile);

        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        TextView tvTweets = (TextView) findViewById(R.id.tvTweets);

        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        tvTweets.setText(user.getTweetsCount() + " Tweets");


        tvUserName.setText(user.getName());
        tvScreenName.setText(user.getPrettyScreenName());
        Picasso.with(this).load(user.getProfileImageURL())
                .noFade().fit().into(ivProfileImage);

        getSupportActionBar().setTitle(user.getPrettyScreenName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            //Intent homeIntent = new Intent(this, MainActivity.class);
            //startActivity(homeIntent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
