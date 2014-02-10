package com.codepath.apps.twitterclient.models;

import java.io.Serializable;

import org.json.JSONObject;

public class User implements Serializable {
	private static final long serialVersionUID = -7969323318548203860L;

	private int mNumTweets;
	private int mNumFollowers;
	private int mNumFriends;
	private long mId;
	private String mName;
	private String mScreenName;
	private String mProfileImageUrl;
	private String mProfileBackgroundImageUrl;
	
	public String getName() {
		return mName;
	}
	
	public long getId() {
		return mId;
	}
	
	public String getScreenName() {
		return mScreenName;
	}
	
	public String getProfileImageUrl() {
		return mProfileImageUrl;
	}
	
	public String getProfileBackgroundImageUrl() {
		return mProfileBackgroundImageUrl;
	}
	
	public int getNumTweets() {
		return mNumTweets;
	}
	
	public int getNumFollowers() {
		return mNumFollowers;
	}
	
	public int getNumFriends() {
		return mNumFriends;
	}
	
	public static User fromJson(JSONObject json) {
		User u = new User();
		
		try {
			u.mName = json.getString("name");
			u.mId = json.getLong("id");
			u.mScreenName = json.getString("screen_name");
			u.mProfileImageUrl = json.getString("profile_image_url");
			u.mProfileBackgroundImageUrl = json.getString("profile_background_image_url");
			u.mNumTweets = json.getInt("statuses_count");
			u.mNumFollowers = json.getInt("followers_count");
			u.mNumFriends = json.getInt("friends_count");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return u;
	}
}
