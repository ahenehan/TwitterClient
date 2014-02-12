package com.codepath.apps.twitterclient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.codepath.apps.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item,  null);
		}
		
		Tweet tweet = getItem(position);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		imageView.setTag(tweet.getUser().getScreenName());
		/*imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
			}
			
		});*/
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		
		String formattedName = "<b>" +tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@" +
				tweet.getUser().getScreenName() + "</font></small>";
		
		
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView timeView = (TextView) view.findViewById(R.id.tvTime);
		timeView.setText(twitterHumanFriendlyDate(tweet.getTimeCreated()));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		return view;
	}
	
	public static String twitterHumanFriendlyDate(String dateStr) {
		// parse Twitter date
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
		dateFormat.setLenient(false);
		Date created = null;
		try {
			created = dateFormat.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
 
		// today
		Date today = new Date();
 
		// how much time since (ms)
		Long duration = today.getTime() - created.getTime();
 
		int second = 1000;
		int minute = second * 60;
		int hour = minute * 60;
		int day = hour * 24;
 
		if (duration < second * 7) {
			return "right now";
		}
 
		if (duration < minute) {
			int n = (int) Math.floor(duration / second);
			return n + " seconds ago";
		}
 
		if (duration < minute * 2) {
			return "about 1 minute ago";
		}
 
		if (duration < hour) {
			int n = (int) Math.floor(duration / minute);
			return n + " minutes ago";
		}
 
		if (duration < hour * 2) {
			return "about 1 hour ago";
		}
 
		if (duration < day) {
			int n = (int) Math.floor(duration / hour);
			return n + " hours ago";
		}
		if (duration > day && duration < day * 2) {
			return "yesterday";
		}
 
		if (duration < day * 365) {
			int n = (int) Math.floor(duration / day);
			return n + " days ago";
		} else {
			return "over a year ago";
		}
	}
	
}
