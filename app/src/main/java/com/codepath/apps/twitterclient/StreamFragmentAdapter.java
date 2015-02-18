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
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}