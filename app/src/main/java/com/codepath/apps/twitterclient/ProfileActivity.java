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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends ActionBarActivity {
    public String TAG = "**********>> " + this.getClass().getName();
    StreamFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        fragment = StreamFragment.newInstance(0);
        // Replace the container with the new fragment
        ft.replace(R.id.frame_stream, fragment);

        String screenName = MainActivity.user.getScreenName();
        getUserInfo(screenName);

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

    public void populateUserPage(User user){
        StreamFragmentAdapter.getUserTimeline(user.getId(), 1, fragment);

        Log.d(TAG, "populating user page " + user.getName());
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName_profile);
        TextView tvScreenName = (TextView) findViewById(R.id.tvCompScreenName_profile);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage_profile);

        tvUserName.setText(user.getName());
        tvScreenName.setText(user.getPrettyScreenName());
        Picasso.with(this).load(user.getProfileImageURL())
                .noFade().fit().into(ivProfileImage);
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
