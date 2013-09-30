package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoreFragment extends Fragment {
	private String tag = this.getClass().getSimpleName();
	private View viewMainFragment;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.viewMainFragment = inflater.inflate(R.layout.more_fragment,
				container, false);

		Log.d(tag, "onCreateView");
		return viewMainFragment;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	public void onStart() {
		super.onStart();
		Log.d(tag, "onStart");

	}

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreated");

	}

	public void reloadViewAfterRequestTaskComplete() {
		Log.d(tag, "reloadViewAfterRequestTaskComplete");

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

}
