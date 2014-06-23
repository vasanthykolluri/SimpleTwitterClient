package com.codepath.apps.basictwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeActivity extends Activity {
	
	TextView tvUserName;
	TextView tvTgtUserName;
	EditText etComposeTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvTgtUserName = (TextView) findViewById(R.id.tvTgtUserName);
		
		String userName = getIntent().getStringExtra("userName");
		tvUserName.setText(userName);
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

	public void onTweetSave(MenuItem mi) {
		String newTweet = etComposeTweet.getText().toString();
		
		Intent timeLineIntent = new Intent(this, TimelineActivity.class);
		timeLineIntent.putExtra("newTweet", newTweet);
		setResult(200, timeLineIntent);
		finish();
	}
}
