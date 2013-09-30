package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankActivity extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View viewMainFragment = inflater.inflate(R.layout.blank_activity,
				container, false);
		
		return viewMainFragment;
	}
	
}
