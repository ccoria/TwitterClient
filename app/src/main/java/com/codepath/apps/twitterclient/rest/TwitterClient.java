package com.codepath.apps.twitterclient.rest;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    private String TAG = this.getClass().getName();
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "1WmJxz7ajODwYEn1enJfAvfRT";       // Change this
	public static final String REST_CONSUMER_SECRET = "4BT90oYXpZab2F3Y8AdNrpfHz9azDZAcjDfNejXuhyiykQB6GR"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets"; // Change this (here and in manifest)

    public enum TIMELINE_TYPE {
        HOME, MENTIONS, USER
    }

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        getTimeline(
                "statuses/home_timeline.json",
                new RequestParams("page", String.valueOf(page)),
                handler);
    }

    public void getMentionsTimeline(int page, AsyncHttpResponseHandler handler) {
        getTimeline(
                "statuses/mentions_timeline.json",
                new RequestParams("page", String.valueOf(page)),
                handler);
    }

    public void getUserTimeline(int userId, int page, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("page", String.valueOf(page));
        params.put("user_id", userId);
        getTimeline("statuses/mentions_timeline.json", params, handler);
    }

    public void getTimeline(String endpoint, RequestParams params, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(endpoint);
        getClient().get(apiUrl, params, handler);
    }

    public void getAccountSettings(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/settings.json");
        getClient().get(apiUrl, handler);
    }

    public void getCredentials(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}