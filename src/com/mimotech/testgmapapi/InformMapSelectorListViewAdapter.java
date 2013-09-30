package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InformMapSelectorListViewAdapter extends BaseAdapter {
	private String tag = getClass().getSimpleName();
	private Context context;
	private ArrayList<Nearby> nearbyList;
	private LayoutInflater inflater;

	public InformMapSelectorListViewAdapter(Context context, ArrayList<Nearby> newsList) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		this.nearbyList = newsList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d(tag, "NewsListViewAdapter");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null)
			convertView = inflater.inflate(R.layout.inform_listview_selector, parent,
					false);
		
		TextView tvTitle = (TextView) convertView.findViewById(R.id.placeDetail);
		tvTitle.setText(nearbyList.get(position).title+"\n");

		TextView tvHowFar = (TextView) convertView.findViewById(R.id.placeHowFar);
		tvHowFar.setText(this.context.getString(R.string.farfromyou_msg)+" "+nearbyList.get(position).howFar());

		
		
		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nearbyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		
		return this.nearbyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//String newsIdStr = newsList.get(position).id;
	//	int newsId = Integer.parseInt(newsIdStr);
		return 0;
	}

}