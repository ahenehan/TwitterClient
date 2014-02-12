package com.codepath.apps.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TweetsAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

public abstract class TweetsListFragment extends Fragment {
	
	protected ArrayList<Tweet> tweets;
	protected ListView lvTweets;
	protected TweetsAdapter tweetsAdapter;
	protected User currentUser;
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragments_tweets_list, parent, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
		tweetsAdapter = new TweetsAdapter(getActivity(), tweets);		
		lvTweets.setAdapter(tweetsAdapter);
		tweetsAdapter.addAll(tweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadDataFromApi(tweetsAdapter.getItem(tweetsAdapter.getCount()-1).getId() - 1);//tweets.get(tweets.size() - 1).getId() - 1);				
			}
		});
		//lvTweets.setOnItemClickListener(listener)
	}
	
	protected void onSuccessfulResponse(JSONArray jsonTweets) {
		ArrayList<Tweet> moreTweets = Tweet.fromJson(jsonTweets);
		//tweets.addAll(moreTweets);
		tweetsAdapter.addAll(moreTweets);
		Log.d("DEBUG", jsonTweets.toString());
	}
	
	public TweetsAdapter getAdapter() {
		return tweetsAdapter;
	}
	
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}
	
	abstract void customLoadDataFromApi(long lastId);
	
}
