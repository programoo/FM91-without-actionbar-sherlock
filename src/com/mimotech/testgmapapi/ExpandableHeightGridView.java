package com.mimotech.testgmapapi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView
{
	boolean expanded = false;
	private Context context;
	public ExpandableHeightGridView(Context context)
	{
		super(context);
		this.context = context;
	}
	
	public ExpandableHeightGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public ExpandableHeightGridView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public boolean isExpanded()
	{
		return expanded;
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// HACK! TAKE THAT ANDROID!
		if (isExpanded())
		{
			/*
			WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int width = display.getWidth();  // deprecated
			int height = display.getHeight();  // deprecated
			*/
			// Calculate entire height by providing a very large height hint.
			// View.MEASURED_SIZE_MASK represents the largest height possible.
			int expandSpec = MeasureSpec.makeMeasureSpec(10000,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
			
			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
		} else
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	public void setExpanded(boolean expanded)
	{
		this.expanded = expanded;
	}
}
