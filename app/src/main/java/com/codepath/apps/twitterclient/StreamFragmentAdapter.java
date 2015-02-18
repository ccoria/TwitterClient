package com.codepath.apps.twitterclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.twitterclient.rest.TwitterApplication;
import com.codepath.apps.twitterclient.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by ccoria on 2/16/15.
 */
public class StreamFragmentAdapter extends FragmentPagerAdapter {
    public String TAG = "**********>> " + this.getClass().getName();
    private String tabTitles[] = new String[] { "Home", "Mentions" };
    public final int COUNT = 2;

    public StreamFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Fragment getItem(int tab_index) {
        Log.d(TAG, "Getting Fragment " + tab_index);
        StreamFragment fragment = StreamFragment.newInstance(tab_index);

        if (tab_index == 0) {
            getHomeTimeline(1, fragment);
        } else if (tab_index == 1) {
            getMentionsTimeline(1, fragment);
        }

        return fragment;
    }

    public void getHomeTimeline(final int page, final StreamFragment fragment) {
        TwitterApplication.getRestClient().getHomeTimeline(page, getHandler(fragment));
    }

    public void getMentionsTimeline(final int page, final StreamFragment fragment) {
        TwitterApplication.getRestClient().getMentionsTimeline(page, getHandler(fragment));
    }

    private JsonHttpResponseHandler getHandler(final StreamFragment fragment) {
        return  new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {
                    fragment.addTweets(jsonArray);
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
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}