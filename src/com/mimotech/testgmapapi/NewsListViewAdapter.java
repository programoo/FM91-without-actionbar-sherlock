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

public class NewsListViewAdapter extends BaseAdapter
{
	private String tag = getClass().getSimpleName();
	private ArrayList<News> newsList;
	private LayoutInflater inflater;
	
	public NewsListViewAdapter(Context context, ArrayList<News> newsList)
	{
		super();
		// TODO Auto-generated constructor stub
		this.newsList = newsList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Log.d(tag, "NewsListViewAdapter");
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		if (convertView == null)
			convertView = inflater.inflate(R.layout.news_fragment_listview,
					parent, false);
		
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.newsLogo);
		TextView description = (TextView) convertView
				.findViewById(R.id.newsText);
		TextView reporter = (TextView) convertView.findViewById(R.id.reporter);
		
		TextView endTime = (TextView) convertView.findViewById(R.id.newsTime);
		
		imageView.setImageResource(R.drawable.ic_launcher);
		
		description.setText(newsList.get(position).description);
		
		reporter.setText("โดย " + newsList.get(position).secondarySource + " ("
				+ newsList.get(position).primarySource + ")");
		endTime.setText(newsList.get(position).alreadyPassTime);
		
		// hidden isRead marker if already read and
		if (newsList.get(position).isRead)
		{
			ImageView isReadImg = (ImageView) convertView
					.findViewById(R.id.isRead);
			isReadImg.setVisibility(View.INVISIBLE);
		} else
		{
			// show unread tag
			ImageView isReadImg = (ImageView) convertView
					.findViewById(R.id.isRead);
			isReadImg.setVisibility(View.VISIBLE);
		}
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return newsList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return position;
	}
	
	@Override
	public long getItemId(int position)
	{
		String newsIdStr = newsList.get(position).id;
		int newsId = Integer.parseInt(newsIdStr);
		return newsId;
	}
	
}