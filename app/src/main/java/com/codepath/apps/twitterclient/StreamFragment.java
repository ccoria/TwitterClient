package com.codepath.apps.twitterclient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.TweetList;
import com.codepath.apps.twitterclient.rest.TwitterApplication;
import com.codepath.apps.twitterclient.rest.TwitterClient;
import com.codepath.apps.twitterclient.uihelpers.EndlessScrollListener;
import com.codepath.apps.twitterclient.uihelpers.TwitterArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class StreamFragment extends Fragment {
    public String TAG = "**********>> " + this.getClass().getName();

    public static final String ARG_PAGE = "ARG_TYPE";
    public static final String ARG_USER = "ARG_USER";
    TwitterArrayAdapter adapter;
    ListView listView;

    private String screenName;

    private int mType;

    public static StreamFragment newInstance(int type, String screenName) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, type);
        args.putString(ARG_USER, screenName);
        StreamFragment fragment = new StreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(ARG_PAGE);
        screenName = getArguments().getString(ARG_USER);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        listView = (ListView) view.findViewById(R.id.lvGenericStream);

        adapter = new TwitterArrayAdapter(view.getContext());
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getTimeline(1);

        final StreamFragment fragment = this;
        listView.setOnScrollListener(new EndlessScrollListener(3, 0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("setOnScrollListener TYPE: " + mType, "calling page " + page);
                fragment.getTimeline(page);
            }
        });
    }

    public void setUpEndlessScroll () {

    }

    public void addTweets (JSONArray jsonArrayTweets) {
        try {
            adapter.addAll(new TweetList(jsonArrayTweets));
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getTimeline(int page) {
        if (mType == 0) {
            getHomeTimeline(page);
        } else if (mType == 1) {
            getMentionsTimeline(page);
        } else if (mType == 2) {
            getUserTimeline(page, screenName);
        }
    }

    public void getHomeTimeline(int page) {
        Log.d(TAG, "getting getHomeTimeline");
        TwitterApplication.getRestClient().getHomeTimeline(page, getHandler());
    }

    public void getMentionsTimeline(int page) {
        Log.d(TAG, "getting getMentionsTimeline");
        TwitterApplication.getRestClient().getMentionsTimeline(page, getHandler());
    }

    public void getUserTimeline(int page, String screenName) {
        Log.d(TAG, "getting getUserTimeline");
        TwitterApplication.getRestClient().getUserTimeline(screenName, page, getHandler());
    }

    private JsonHttpResponseHandler getHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {
                    addTweets(jsonArray);
                } else {
                    Log.e("StreamFragmentAdapter", "Error getting tweets. Status code: " + statusCode);
                }
                // Load json array into model classes
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("StreamFragmentAdapter", "getTweets Error: " + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }
}
