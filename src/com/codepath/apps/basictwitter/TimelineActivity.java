package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterClientApp.getRestClient();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		populateTimeline(10, 1, 10);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				customLoadMoreDataFromApi(page);
				// or customLoadMoreDataFromApi(totalItemsCount);
			}
		});
	}

	// Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    }
    
	public void populateTimeline(int count, int since_id, int max_id) {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJSONArray(json));
				Log.d("debug", "GET TIMELINE - On Success:");

				Toast.makeText(TimelineActivity.this,
						"GET TIMELINE - onSuccess", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		}, count, since_id, max_id);
	}

	public void postTweet(String tweet) {
		client.postTweet((new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				Log.d("debug", "POST TWEET - On Success:");
				Toast.makeText(TimelineActivity.this, "POST TWEET - onSuccess",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", "POST TWEET - On Failure");

				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		}), tweet);
		Log.d("debug", "postTweet done");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onComposeAction(MenuItem mi) {
		Intent composeIntent = new Intent(this, ComposeActivity.class);
		startActivityForResult(composeIntent, REQUEST_CODE);
	}

	private final int REQUEST_CODE = 20;
	private final int RESULT_OK = 200;

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent composeIntent) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			String tweet = composeIntent.getExtras().getString("newTweet");

			// Post the new tweet and update the timeline
			postTweet(tweet);
			Log.d("debug", "calling populateTimeline");

			populateTimeline(10, 1, 10);
		}
	}
}
