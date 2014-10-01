package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.fragments.ComposeTweetFragment;
import com.codepath.apps.fragments.ComposeTweetFragment.ComposeFragmentListener;
import com.codepath.apps.utilities.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements ComposeFragmentListener{

	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private int pageNumber;
	private boolean refresh = false;
	private boolean loadMore = false;
	User currentUser; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();

		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);

		getUserCredentials();
		populateTimeline();
		SetOnClickListeners();
	}

	private void SetOnClickListeners() {
		// Support for infinite scrolling
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				pageNumber = page;
				refresh = false;
				loadMore = true;
				// Log.d("Twitter", "page = "+page);
				// Log.d("Twitter", "totalItemsCount = "+totalItemsCount);
				populateTimeline();
			}
		});
	}

	public void getUserCredentials() {
		client.getUserCredentials(new JsonHttpResponseHandler(){
			
			public void onSuccess(JSONObject json) {
				currentUser = User.fromJSON(json);
			}
			
			public void onFailure(Throwable e, String s) {
				Log.d("Twitter", e.toString());
				Log.d("Twitter", s.toString());
			}

		});
	}

	public void populateTimeline() {
		client.getHomeTimeline(loadMore, refresh,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray json) {
						Log.d("Twitter", "On Success");
						if (pageNumber == 0 && refresh == false) {
							tweets.clear();
							aTweets.clear();
						}
						if (refresh == true) {
							tweets.addAll(0, Tweet.fromJSONArray(json));
						} else {
							tweets.addAll(Tweet.fromJSONArray(json));
						}
						aTweets.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("Twitter", e.toString());
						Log.d("Twitter", s.toString());
					}

				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_compose_tweet, menu);

		aTweets.clear();
		pageNumber = 0;
		refresh = false;
		loadMore = false;
		populateTimeline();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miActionRefresh:
			RefreshTimeline();
			return true;
		case R.id.miActionCompose:
			ComposeTweet();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void ComposeTweet() {
		// Compose Dialog
		FragmentManager fm = getSupportFragmentManager();
		ComposeTweetFragment composeTweet = ComposeTweetFragment
				.newInstance("Compose");
		Bundle bundleCompose = new Bundle();
		bundleCompose.putSerializable("CurrentUser", currentUser);
		composeTweet.setArguments(bundleCompose);
		composeTweet.show(fm, "compose");
	}

	private void RefreshTimeline() {
		refresh = true;
		loadMore = false;
		populateTimeline();
	}

	@Override
	public void onSentTweet(Tweet sentTweet) {
		tweets.add(0, sentTweet);
		aTweets.notifyDataSetChanged();
	}
}
