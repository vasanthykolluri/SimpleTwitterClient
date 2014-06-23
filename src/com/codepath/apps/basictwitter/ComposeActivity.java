package com.codepath.apps.basictwitter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {

	private TwitterClient client;

	private ImageView ivProfileImage;
	private TextView tvUserName;
	private TextView tvUserHandle;
	private EditText etComposeTweet;
	private TextView tvNumCharsLeft;

	private final int MAX_TWEET_CHARS = 140;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		client = TwitterClientApp.getRestClient();
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserHandle = (TextView) findViewById(R.id.tvUserHandle);
		tvNumCharsLeft = (TextView) findViewById(R.id.tvNumCharsLeft);

		// Get the user details
		getUserCredentials();

		tvNumCharsLeft.setText(Integer.toString(MAX_TWEET_CHARS));

		etComposeTweet.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				updateCharsLeft();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void updateCharsLeft() {
		int numCharsLeft = MAX_TWEET_CHARS
				- etComposeTweet.getText().toString().length();
		tvNumCharsLeft.setText(Integer.toString(numCharsLeft));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_menu, menu);
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

	public void getUserCredentials() {
		client.verifyCredentials((new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {

				try {
					String profileImageUrl = json
							.getString("profile_image_url").toString();

					String screen_name = json.getString("screen_name")
							.toString();

					ivProfileImage
							.setImageResource(android.R.color.transparent);
					ImageLoader imageLoader = ImageLoader.getInstance();
					imageLoader.displayImage(profileImageUrl, ivProfileImage);

					tvUserName.setText(screen_name);
					tvUserHandle.setText("@" + screen_name);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		}));
	}

	public void onTweetSave(MenuItem mi) {
		String newTweet = etComposeTweet.getText().toString();

		// Do nothing if the tweet is longer than 140 chars
		if (newTweet.length() > MAX_TWEET_CHARS)
			return;

		Intent timeLineIntent = new Intent(this, TimelineActivity.class);
		timeLineIntent.putExtra("newTweet", newTweet);
		setResult(200, timeLineIntent);
		finish();
	}
}
