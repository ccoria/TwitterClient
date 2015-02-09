package com.codepath.apps.twitterclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TimelineActivity extends ActionBarActivity {
    public String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        TwitterClient client = TwitterApplication.getRestClient();
        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                try {
                    Log.d(TAG, "timeline: " + jsonArray.getJSONObject(0).getString("text"));
                } catch (JSONException e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                    e.printStackTrace();
                }
                // Load json array into model classes
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "Error: " + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
