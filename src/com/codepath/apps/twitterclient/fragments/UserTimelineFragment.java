package com.codepath.apps.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.TwitterClientApp;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	private String mUserName;
	private User mUser;
	public static UserTimelineFragment newInstance(String userName) {
		UserTimelineFragment utlFragment = new UserTimelineFragment();
		Bundle args = new Bundle();
		args.putString("userName", userName);
		utlFragment.setArguments(args);
		return utlFragment;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserName = getArguments().getString("userName");
	}

	@Override
	void customLoadDataFromApi(long lastId) {
		TwitterClientApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				onSuccessfulResponse(jsonTweets);
			}
		}, ("?screen_name=" + mUserName + "&max_id=" + lastId));
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		TwitterClientApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				onSuccessfulResponse(jsonTweets);
			}
			
		}, ("?screen_name=" + mUserName));
		return super.onCreateView(inf, parent, savedInstanceState);
	}
}
