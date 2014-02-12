package com.codepath.apps.twitterclient;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		String name = getIntent().getStringExtra("name");
		loadFragment(name);
		loadProfileInfo(name);
	}

	private void loadFragment(String name) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment utlFragment = UserTimelineFragment.newInstance(name);
		ft.replace(R.id.flFrame, utlFragment);
		ft.commit();
	}
	
	private void loadProfileInfo(String name) {
		TwitterClientApp.getRestClient().getUser(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				User u = User.fromJson(jsonObject);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
				Log.d("DEBUG it", jsonObject.toString());
			}
		}, ("?screen_name=" + name));
	}

	private void loadProfileInfo() {
		TwitterClientApp.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				User u = User.fromJson(jsonObject);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
			}
		});
	}
	
	private void populateProfileHeader(User user) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getNumFollowers() + " Followers");
		tvFollowing.setText(user.getNumFriends() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
