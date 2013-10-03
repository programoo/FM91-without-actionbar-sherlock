package com.mimotech.testgmapapi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView
{
	boolean expanded = false;
	private Context context;
	private int heightSpec;
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
		
		/*
		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE , MeasureSpec.AT_MOST);
        }
        else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
        */
		
		// HACK! TAKE THAT ANDROID!
		if (true)
		{
			
			// Calculate entire height by providing a very large height hint.
			// View.MEASURED_SIZE_MASK represents the largest height possible.
			int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
			
			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
		} else
		{
			//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		
		
		
	}
	
	public void setExpanded(boolean expanded)
	{
		this.expanded = expanded;
	}
}
