package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class CameraGridViewAdapter extends BaseAdapter {
	String tag = this.getClass().getSimpleName();
	Context mainContext;
	private ArrayList<Camera> camList;
	private AQuery aq;
	private LayoutInflater lf;

	public CameraGridViewAdapter(Context context, ArrayList<Camera> camList) {
		Log.d(tag, "GridViewAdapter");
		mainContext = context;
		this.camList = camList;
		aq = new AQuery(this.mainContext);
		lf = (LayoutInflater) mainContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return camList.size();
	}

	@Override
	public Camera getItem(int position) {
		// TODO Auto-generated method stub
		return this.camList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = lf.inflate(R.layout.camera_fragment_gridview, null);
		
		TextView tv = (TextView) convertView.findViewById(R.id.cameraTextView1);
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.cameraImageView1);

		tv.setText(camList.get(position).thaiName);
		aq.id(iv).image(camList.get(position).imgUrl, true, true, 200, 0);

		return convertView;
	}

}
