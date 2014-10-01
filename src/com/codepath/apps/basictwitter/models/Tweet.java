package com.codepath.apps.basictwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet {
	public static long maxId;
	public static long sinceId;
	private String body;
	private long uid;
	private String createdAt;
	private User user;

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Extract values from JSON to populate member variables
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			if (Tweet.maxId == 0) {
				Tweet.maxId = tweet.uid;
			} else {
				Tweet.maxId = Math.min(tweet.uid, Tweet.maxId);
			}

			Tweet.sinceId = Math.max(tweet.uid, Tweet.sinceId);
			Log.d("Twitter", Tweet.sinceId + "");

			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return tweet;
	}
}
