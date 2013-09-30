package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EmergencyCallListViewAdapter extends BaseAdapter
{
	private String tag = getClass().getSimpleName();
	private ArrayList<Contact> contactList;
	private LayoutInflater inflater;
	
	public EmergencyCallListViewAdapter(Context context, ArrayList<Contact> newsList)
	{
		super();
		// TODO Auto-generated constructor stub
		this.contactList = newsList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Log.d(tag, "EmergencyCallListViewAdapter");
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = inflater.inflate(R.layout.emergencycall_fragment_listview,
					parent, false);
		TextView phoneNumTv = (TextView) convertView.findViewById(R.id.phoneEmergencyTv);
		TextView nameNumTv = (TextView) convertView.findViewById(R.id.nameEmergencyTv);

		
		
		phoneNumTv.setText(this.contactList.get(position).phoneNum);
		nameNumTv.setText(this.contactList.get(position).name);
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return contactList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return position;
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
}