package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.TweetList;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TimelineActivity extends ActionBarActivity {
    public String TAG = "**********>> " + this.getClass().getName();
    public ListView lvStream;
    public ActionBar actionBar;
    TwitterClient client;
    TwitterArrayAdapter twitterAdapter;
    public int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        lvStream = (ListView) findViewById(R.id.lvStream);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_logo);

        client = TwitterApplication.getRestClient();

        lvStream.setOnScrollListener(new EndlessScrollListener(10, -1) { //TODO: figure this -1 out
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getTweets(page);
            }
        });

        // Getting first page
        getTweets(0);
    }

    public void getTweets(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {
                    try {
                        TweetList tweets = Tweet.fromJson(jsonArray);
                        fillAdapter(tweets);
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

    public void fillAdapter(TweetList tweets) {
        if(twitterAdapter == null) {
            twitterAdapter = new TwitterArrayAdapter(this, tweets);
            lvStream.setAdapter(twitterAdapter);
        } else {
            twitterAdapter.addAll(tweets);
            twitterAdapter.notifyDataSetChanged();
        }
    }

    public void setUsernameTitle(){
        client.getAccountSettings(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    actionBar.setTitle("@" + response.getString("screen_name"));
                } catch (JSONException e) {
                    actionBar.setTitle("Home");
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
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
            startActivity(composeIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onComposeClick(MenuItem item) {
        Toast.makeText(this, "Compose Click!", Toast.LENGTH_SHORT).show();
    }
}
