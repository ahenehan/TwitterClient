package com.codepath.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsFragment;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {

	private final int REQUEST_CODE = 20;
	
	private User currentUser;
	private HomeTimelineFragment h;
	private MentionsFragment m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		TwitterClientApp.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				currentUser = User.fromJson(userData);
			}
		});
		
		setupNavigationTabs();
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home")
				.setTag("HomeTimelineFragment")
				.setIcon(R.drawable.ic_home)
				.setTabListener(this);
		
		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag("MentionsTimelineFragment")
				.setIcon(R.drawable.ic_mentions)
				.setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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
		i.putExtra("user", currentUser);
		startActivityForResult(i, REQUEST_CODE);
	}
	
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("name", currentUser.getScreenName());
		startActivity(i);
	}
	
	public void onImageClicked(View v) {
		Intent i = new Intent (this, ProfileActivity.class);
		i.putExtra("name", v.getTag().toString());
		startActivity(i);
	}
	
	private void onRefresh() {
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    		Toast.makeText(getApplicationContext(), "Tweet posted", Toast.LENGTH_SHORT).show();
    		
    		Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
			//h.getTweets().add(0, tweet);
    		if (h != null) {
    			h.getAdapter().insert(tweet, 0);
    		}
    			//tweetsAdapter.notifyDataSetChanged();
    	}
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		h = new HomeTimelineFragment();
		m = new MentionsFragment();
		if (tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, h);
		} else {
			fts.replace(R.id.frame_container, m);
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
}
