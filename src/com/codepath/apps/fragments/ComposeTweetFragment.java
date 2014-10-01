package com.codepath.apps.fragments;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetFragment extends DialogFragment {

	TextView tvMyName;
	TextView tvMyScreenName;
	ImageView ivMyProfileImg;
	EditText etCompose;
	Button btnTweet;

	private TwitterClient client;

	public interface ComposeFragmentListener {
		void onSentTweet(Tweet sentTweet);
	}

	public ComposeTweetFragment() {
		// Empty constructor required for DialogFragment
	}

	public static ComposeTweetFragment newInstance(String title) {
		ComposeTweetFragment frag = new ComposeTweetFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_compose_tweet, container);
		String title = getArguments().getString("title", "Compose");
		getDialog().setTitle(title);
		client = TwitterApplication.getRestClient();
		getViews(view);
		setOnClickListeners();
		GetAndSetDialogFields();

		return view;
	}

	private void setOnClickListeners() {
		btnTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!etCompose.getText().toString().isEmpty()) {
					client.postTweet(etCompose.getText().toString(),
							new JsonHttpResponseHandler() {

								public void onSuccess(JSONObject json) {
									Tweet sentTweet = Tweet.fromJSON(json);
									ComposeFragmentListener listner = (ComposeFragmentListener) getActivity();
									listner.onSentTweet(sentTweet);
									dismiss();
								}

								public void onFailure(Throwable e, String s) {
									Log.d("Twitter", e.toString());
									Log.d("Twitter", s.toString());
								}

							});
				}
			}
		});
	}

	private void GetAndSetDialogFields() {
		User currentUser = (User) getArguments().getSerializable("CurrentUser");

		etCompose.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		tvMyName.setText(currentUser.getName());
		tvMyScreenName.setText("@" + currentUser.getScreenName());

		ivMyProfileImg.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();

		// Populate the data into the template view using the data object
		imageLoader.displayImage(currentUser.getProfileImageUrl(),
				ivMyProfileImg);

	}

	private void getViews(View view) {
		tvMyName = (TextView) view.findViewById(R.id.tvMyName);
		tvMyScreenName = (TextView) view.findViewById(R.id.tvMyScreenName);
		ivMyProfileImg = (ImageView) view.findViewById(R.id.ivMyProfileImg);
		etCompose = (EditText) view.findViewById(R.id.etCompose);
		btnTweet = (Button) view.findViewById(R.id.btnTweet);
	}

}
