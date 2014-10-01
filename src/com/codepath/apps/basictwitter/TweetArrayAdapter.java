package com.codepath.apps.basictwitter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.utilities.RelativeTimeStamp;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the data item for this position
		Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.tweet_single_item, parent, false);
		}

		// Lookup view for data population
		TextView tvUserName = (TextView) convertView
				.findViewById(R.id.tvUserName);
		TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		TextView tvUserScreenName = (TextView) convertView
				.findViewById(R.id.tvUserScreenName);
		TextView tvRelativeTimeStamp = (TextView) convertView
				.findViewById(R.id.tvRelativeTimeStamp);
		ImageView ivProfileImg = (ImageView) convertView
				.findViewById(R.id.ivProfileImg);
		ivProfileImg.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();

		// Populate the data into the template view using the data object
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(),
				ivProfileImg);
		tvUserName.setText(tweet.getUser().getName());
		tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());

		String relativeTime = RelativeTimeStamp.getRelativeTimeAgo(tweet
				.getCreatedAt());
		Log.d("Twitter", "time = "+relativeTime);
		if(relativeTime.equals("Yesterday")){
			relativeTime = "1 day ago";
		}
		String relativeTimeInt = relativeTime.substring(0,
				relativeTime.indexOf(" "));
		String relativeTimeStr = relativeTime.substring(
				relativeTime.indexOf(" ") + 1, relativeTime.indexOf(" ") + 2);
		tvRelativeTimeStamp.setText(relativeTimeInt + relativeTimeStr);

		// Return the completed view to render on screen
		return convertView;

	}

}
