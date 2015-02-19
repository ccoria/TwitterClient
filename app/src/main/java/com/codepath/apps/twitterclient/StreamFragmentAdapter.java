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
import com.codepath.apps.twitterclient.uihelpers.EndlessScrollListener;
import com.codepath.apps.twitterclient.uihelpers.TwitterArrayAdapter;
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
        StreamFragment fragment = StreamFragment.newInstance(tab_index, null);
        return fragment;
    }

    public static void getHomeTimeline(final int page, final StreamFragment fragment) {
        TwitterApplication.getRestClient().getHomeTimeline(page, getHandler(fragment));
    }

    public static void getMentionsTimeline(final int page, final StreamFragment fragment) {
        TwitterApplication.getRestClient().getMentionsTimeline(page, getHandler(fragment));
    }

    public static void getUserTimeline(final String screenName, final int page, final StreamFragment fragment) {
        TwitterApplication.getRestClient().getUserTimeline(screenName, page, getHandler(fragment));
    }

    public static JsonHttpResponseHandler getHandler(final StreamFragment fragment) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {
                    fragment.addTweets(jsonArray);
                } else {
                    Log.e("StreamFragmentAdapter", "Error getting tweets. Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("StreamFragmentAdapter", "getTweets Error: " + errorResponse);
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