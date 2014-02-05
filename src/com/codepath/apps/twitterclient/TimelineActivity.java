package com.codepath.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private final int REQUEST_CODE = 20;
	
	private ArrayList<Tweet> tweets;
	private ListView lvTweets;
	private TweetsAdapter tweetsAdapter;
	private User currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		tweets = new ArrayList<Tweet>();
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);		
		lvTweets.setAdapter(tweetsAdapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadDataFromApi(tweets.get(tweets.size() - 1).getId() + 1);				
			}
			
		});
		
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				//ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				tweets = Tweet.fromJson(jsonTweets);
				tweetsAdapter.addAll(tweets);
				//tweetsAdapter.notifyDataSetChanged();
				Log.d("DEBUG", jsonTweets.toString());
				int bob = 0;
			}
			
		});
		
		TwitterClientApp.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				currentUser = User.fromJson(userData);
			}
		});
	}

	public void customLoadDataFromApi(long lastId) {
		TwitterClientApp.getRestClient().getHomeTimelineNextSet(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> moreTweets = Tweet.fromJson(jsonTweets);
				tweets.addAll(moreTweets);
				tweetsAdapter.addAll(moreTweets);
				Log.d("DEBUG", jsonTweets.toString());
			}
		}, lastId);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void onComposeTweet(MenuItem mi) {
		Toast.makeText(this, getString(R.string.compose_tweet), Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, ComposeTweetActivity.class);
		i.putExtra("user", currentUser.getJSONString());
		startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    		Toast.makeText(getApplicationContext(), "Tweet posted", Toast.LENGTH_SHORT).show();
    		String text = data.getExtras().getString("tweet");
    		JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(text);
				Tweet newTweet = Tweet.fromJson(jsonObject);
				tweets.add(0, newTweet);
				tweetsAdapter.insert(newTweet, 0);
				tweetsAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    	}
    }
	
}
