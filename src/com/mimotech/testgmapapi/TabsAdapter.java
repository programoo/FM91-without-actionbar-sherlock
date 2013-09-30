package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TabsAdapter extends FragmentPagerAdapter implements
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, OnTouchListener{
	String tag = getClass().getSimpleName();
	private final Context mContext;
	private final TabHost mTabHost;
	private final ViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	static final class TabInfo {
		@SuppressWarnings("unused")
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(String _tag, Class<?> _class, Bundle _args) {
			tag = _tag;
			clss = _class;
			args = _args;
		}
	}

	static class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	public TabsAdapter(FragmentActivity activity, TabHost tabHost,
			ViewPager pager) {
		super(activity.getSupportFragmentManager());
		Log.i(tag, "TabsAdapter");
		mContext = activity;
		mTabHost = tabHost;
		mViewPager = pager;
		mTabHost.setOnTabChangedListener(this);
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(this);
			
	}

	public void addTab(TabHost.TabSpec tabSpec, Drawable drawableId,
			Class<?> clss, Bundle args, String label) {
		tabSpec.setContent(new DummyTabFactory(mContext));
		String tag = tabSpec.getTag();

		TabInfo info = new TabInfo(tag, clss, args);
		Log.d(tag, "label name is: " + label);
		View tabIndicator = null;
		if (label.equalsIgnoreCase(mContext.getString(R.string.news_tabbar))) {
			tabIndicator = LayoutInflater.from(mContext).inflate(
					R.layout.badge_tabbar_view, mTabHost.getTabWidget(), false);
			ImageView icon = (ImageView) tabIndicator
					.findViewById(R.id.badge_icon);
			icon.setImageDrawable(drawableId);

			TextView tv = (TextView) tabIndicator
					.findViewById(R.id.badge_title);
			tv.setText(label);
			// set font
			Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
					"fonts/Tahoma.ttf");
			tv.setTypeface(tf);

		} else {
			tabIndicator = LayoutInflater.from(mContext)
					.inflate(R.layout.normal_tabbar_view,
							mTabHost.getTabWidget(), false);
			ImageView icon = (ImageView) tabIndicator
					.findViewById(R.id.normal_icon);
			icon.setImageDrawable(drawableId);

			TextView tv = (TextView) tabIndicator
					.findViewById(R.id.normal_title);
			tv.setText(label);
			// set font
			Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
					"fonts/Tahoma.ttf");
			tv.setTypeface(tf);

		}

		tabSpec.setIndicator(tabIndicator);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	public void onTabChanged(String tabId) {
		int position = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(position);
		notifyDataSetChanged();
		Log.d(tag, "ontabChanged: " + tabId);

	}

	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	public void onPageSelected(int position) {
		mTabHost.setCurrentTab(position);
		Log.d(tag, "page change: " + position);
	}

	public void onPageScrollStateChanged(int state) {
	}
	/*
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	*/
	/*
	@Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
	*/

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
