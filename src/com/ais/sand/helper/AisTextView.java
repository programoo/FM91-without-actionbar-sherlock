package com.ais.sand.helper;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class AisTextView extends TextView {
	public AisTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AisTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AisTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface tf = getTypeface("fonts/AISTheStartUp.ttf",getContext());
			/*Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/AISTheStartUp.ttf");*/
			setTypeface(tf, 1);
		}
	}

	private final HashMap<String, Typeface> map = new HashMap<String, Typeface>();

	public Typeface getTypeface(String file, Context context) {
		Typeface result = map.get(file);
		if (result == null) {
			result = Typeface.createFromAsset(context.getAssets(), file);
			map.put(file, result);
		}
		return result;
	}

}
