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
import com.codepath.apps.twitterclient.uihelpers.TwitterArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class StreamFragment extends Fragment {
    public String TAG = "**********>> " + this.getClass().getName();

    public static final String ARG_PAGE = "ARG_PAGE";
    TwitterArrayAdapter adapter;

    private int mPage;

    public static StreamFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StreamFragment fragment = new StreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ListView lvStream = (ListView) view.findViewById(R.id.lvGenericStream);
        tvTitle.setText("Fragment #" + mPage);

        adapter = new TwitterArrayAdapter(view.getContext());
        lvStream.setAdapter(adapter);
        TwitterClient.TIMELINE_TYPE timeline_type = TwitterClient.TIMELINE_TYPE.HOME;

        if (mPage == 0) {
            timeline_type = TwitterClient.TIMELINE_TYPE.HOME;
        } else if (mPage == 1) {
            timeline_type = TwitterClient.TIMELINE_TYPE.MENTIONS;
        }

        getTimeline(timeline_type, 1);

        return view;
    }

    public void getTimeline(TwitterClient.TIMELINE_TYPE type, final int page) {
        TwitterApplication.getRestClient().getTimeline(type, page, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (statusCode == 200) {
                    try {
                        adapter.addAll(new TweetList(jsonArray));
                        adapter.notifyDataSetChanged();
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
}
