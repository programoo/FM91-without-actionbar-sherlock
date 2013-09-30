package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OtherListViewAdapter extends BaseAdapter
{
	private String TAG = getClass().getSimpleName();
	private ArrayList<String> strList;
	private LayoutInflater inflater;
	private int selectedLayout;
	
	public OtherListViewAdapter(Context context, ArrayList<String> strList,
			int selectedLayout)
	{
		Log.d(TAG, "OtherListViewAdapter");
		this.strList = strList;
		this.selectedLayout = selectedLayout;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.d(TAG, "OtherListViewAdapter");
		if (convertView == null)
			convertView = inflater.inflate(selectedLayout,
					parent, false);
		try{
			TextView otherTv = (TextView) convertView.findViewById(R.id.otherTv);
			otherTv.setText(this.strList.get(position).split(",")[0]);

			TextView otherSelectedDataTv = (TextView) convertView.findViewById(R.id.otherSelectedDataTv);
			otherSelectedDataTv.setText(this.strList.get(position).split(",")[1]);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.strList.size();
	}
	
	@Override
	public String getItem(int position)
	{
		return this.strList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
}