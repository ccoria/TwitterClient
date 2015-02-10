package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;


public class ComposeActivity extends ActionBarActivity {
    TextView etCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_logo_white);

        TextView tvUserName = (TextView) findViewById(R.id.tvCompUserName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvCompScreenName);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivCompProfileImage);

        etCompose = (TextView) findViewById(R.id.etCompose);

        tvUserName.setText(TimelineActivity.user.getName());
        tvScreenName.setText(TimelineActivity.user.getPrettyScreenName());
        Picasso.with(this).load(TimelineActivity.user.getProfileImageURL())
                .noFade().fit().into(ivProfileImage);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Hand all the form data
        if (requestCode == 50) {
            if (resultCode == RESULT_OK) {
            }
        }

        //Toast YES or NO vased on if age is greater 21
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tweet) {
            TwitterClient client = TwitterApplication.getRestClient();
            client.postTweet(etCompose.getText().toString(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Intent composeIntent = new Intent(ComposeActivity.this, TimelineActivity.class);
                    startActivity(composeIntent);
                    ComposeActivity.this.finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(ComposeActivity.this, "Error posting: " + error.getMessage(), Toast.LENGTH_SHORT);
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
