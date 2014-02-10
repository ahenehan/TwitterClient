package com.codepath.apps.twitterclient;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {

	private EditText etTweet;
	private TextView tvCurrentCharacterCount;
	private User currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		currentUser = (User) getIntent().getSerializableExtra("user");
		ImageView image = (ImageView) findViewById(R.id.ivUserProfile);
		TextView userName = (TextView) findViewById(R.id.tvUser);
		ImageLoader.getInstance().displayImage(currentUser.getProfileImageUrl(), image);
		userName.setText("@" + currentUser.getScreenName());
		
		tvCurrentCharacterCount = (TextView) findViewById(R.id.tvCurrentCharacterCount);
		
		etTweet = (EditText) findViewById(R.id.etTweet);
		etTweet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (etTweet.getText().length() <= 140) {
					tvCurrentCharacterCount.setText(String.valueOf(etTweet.getText().length()));
				} else {
					Toast.makeText(ComposeTweetActivity.this, "Tweets cannot exceed 140 characters", Toast.LENGTH_SHORT).show();
					etTweet.setText(etTweet.getText().subSequence(0, 140));
					etTweet.setSelection(140);
				}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	public void onCancelPressed(View view) {
		super.onBackPressed();
	}
	
	public void onTweetPressed(View view) {
		String body = etTweet.getText().toString();
		TwitterClientApp.getRestClient().postTweet(body, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject JSONTweet) {
				Tweet tweet = Tweet.fromJson(JSONTweet);
				Intent data = new Intent();
				data.putExtra("tweet", tweet);
				setResult(RESULT_OK, data);
				Log.d("DEBUG", JSONTweet.toString());
				finish();
			}
		});
	}
	
}
