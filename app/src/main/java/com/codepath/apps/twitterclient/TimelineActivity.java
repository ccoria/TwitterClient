package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.TweetList;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TimelineActivity extends ActionBarActivity {
    //unique user of the application
    public static User user;

    public String TAG = "**********>> " + this.getClass().getName();
    public ActionBar actionBar;
    public ImageView ivComposeBtn;
    TwitterClient client;
    TwitterArrayAdapter twitterAdapter;
    public int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        final ListView lvStream = (ListView) findViewById(R.id.lvStream);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_logo_white);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.actionbar_timeline);

        client = TwitterApplication.getRestClient();
        lvStream.setOnScrollListener(new EndlessScrollListener(3, 0) { //TODO: figure this -1 out
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getTweets(page, lvStream);
            }
        });

        // Getting first page
        getTweets(1, lvStream);
        getUser(); //TODO display compose button only when user is loaded
    }

    public void getTweets(final int page, final ListView lvStream) {
        Log.d(TAG, "getting page " + page);
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {
                    try {
                        TweetList tweets = new TweetList(jsonArray);
                        fillAdapter(tweets, lvStream);
                    } catch (Exception e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Error getting tweets. Status code: " + statusCode);
                }
                // Load json array into model classes
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "getTweets Error: " + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    //TODO: implement an assync task for this
    public void fillAdapter(TweetList tweets, ListView lvStream) {
        if(twitterAdapter == null) {
            Log.d(TAG, "Creating From: " + tweets.get(0).getText());
            Log.d(TAG, "Creating To: " + tweets.get(tweets.size()-1).getText());
            twitterAdapter = new TwitterArrayAdapter(TimelineActivity.this, tweets);
            lvStream.setAdapter(twitterAdapter);
        } else {
            Log.d(TAG, "From: " + tweets.get(0).getText());
            Log.d(TAG, "To: " + tweets.get(tweets.size()-1).getText());
            twitterAdapter.addAll(tweets);
            twitterAdapter.notifyDataSetChanged();
        }
    }

    public void getUser(){
        client.getCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.newFromJSON(response);
                //actionBar.setTitle(actionBar.getTitle() + " - " + user.getName());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.compose) {
            Intent composeIntent = new Intent(this, ComposeActivity.class);
            composeIntent.putExtra("user", user);
            startActivity(composeIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onComposeClick(MenuItem item) {
        Toast.makeText(this, "Compose Click!", Toast.LENGTH_SHORT).show();
    }
}
