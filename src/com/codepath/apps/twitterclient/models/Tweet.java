package com.codepath.apps.twitterclient.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet implements Serializable{
	private static final long serialVersionUID = 4106784198585261708L;
	private boolean mFavorited;
	private boolean mRetweeted;
	private long mId;
	private String mBody;
	private String mTimeCreated;
	private User mUser;
	
	
	public User getUser() {
		return mUser;
	}
	
	public String getBody() {
		return mBody;
	}
	
	public long getId() {
		return mId;
	}
	
	public boolean isFavorited() {
		return mFavorited;
	}
	
	public boolean isRetweeted() {
		return mRetweeted;
	}
	
	public String getTimeCreated() {
		return mTimeCreated;
	}
	
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.mBody = jsonObject.getString("text");
			tweet.mId = jsonObject.getLong("id");
			tweet.mFavorited = jsonObject.getBoolean("favorited");
			tweet.mRetweeted = jsonObject.getBoolean("retweeted");
			tweet.mTimeCreated = jsonObject.getString("created_at");
			tweet.mUser = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		
		return tweets;
	}
}

