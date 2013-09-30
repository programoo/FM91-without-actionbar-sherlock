package com.mimotech.testgmapapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

import com.androidquery.util.AQUtility;
public class FM91MainActivity extends FragmentActivity
{
	String tag = getClass().getSimpleName();
	private ViewPager mViewPager;
	private TabHost mTabHost;
	private int badgeCount = 0;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		Log.d(tag, "onCreate");
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		// mViewPager.setId(1);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mTabHost.clearAllTabs();
							
		Bundle tabArgs = null;
		
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_news");
		tabArgs.putInt("id", 1);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_news"), getResources()
				.getDrawable(R.drawable.news_tabbar_img), NewsFragment.class,
				tabArgs, getString(R.string.news_tabbar));
		
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_camera");
		tabArgs.putInt("id", 2);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_camera"), getResources()
				.getDrawable(R.drawable.camera_tabbar_img),
				CameraFragment.class, tabArgs,
				getString(R.string.camera_tabbar));
		
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_radio");
		tabArgs.putInt("id", 3);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_radio"), getResources()
				.getDrawable(R.drawable.radio_tabbar_img), RadioFragment.class,
				tabArgs, getString(R.string.radio_tabbar_text));
		
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_inform");
		tabArgs.putInt("id", 4);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_inform"), getResources()
				.getDrawable(R.drawable.inform_tabbar_img),
				InformFragment.class, tabArgs,
				getString(R.string.inform_tabbar_text));
		
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_other");
		tabArgs.putInt("id", 5);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_other"), getResources()
				.getDrawable(R.drawable.more_tabbar_img), OtherFragment.class,
				tabArgs, getString(R.string.other_tabbar_text));
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// clean the file cache when root activity exit
		// the resulting total cache size will be less than 3M
		if (isTaskRoot())
		{
			AQUtility.cleanCacheAsync(this);
		}
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		TextView v = (TextView) mTabHost.getTabWidget().getChildAt(0)
				.findViewById(R.id.badge_count);
		v.setText("" + badgeCount++);
		return super.onKeyDown(keyCode, event);
		
	}
	
	public void emergencyBtnOnClick(View view)
	{
		Log.d(tag, "emergencyBtnOnClick");
		Intent shareBtn = new Intent(FM91MainActivity.this,
				EmergencyCallActivity.class);
		startActivity(shareBtn);
		
	}
	
}
