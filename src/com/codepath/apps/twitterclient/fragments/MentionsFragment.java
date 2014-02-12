package com.codepath.apps.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import com.codepath.apps.twitterclient.TwitterClientApp;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterClientApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				onSuccessfulResponse(jsonTweets);
			}
			
		});
	}

	@Override
	void customLoadDataFromApi(long lastId) {
		TwitterClientApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				onSuccessfulResponse(jsonTweets);
			}
		}, ("?max_id=" + lastId));
		
	}
}
