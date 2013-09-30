package com.mimotech.testgmapapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class CameraDetailsSharedActivity extends FragmentActivity 
{
	private String tag = getClass().getSimpleName();
	private GoogleMap mMap;
	private SocialAuthAdapter adapter;
	private String description;
	private RelativeLayout newsDetailNormalLayout;
	private RelativeLayout newsDetailShareLayout;
	private EditText newsDetailsShareEditText;
	
	// share layout
	private ImageView uploadIv;
	private Bitmap bitmapSelected;
	private String pathImgSelected;
	
	private static final int REQUEST_CODE = 6384;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_fragment_detail_activity);
		
		Button share = (Button) findViewById(R.id.shareCctvImgBtn);
		adapter = new SocialAuthAdapter(new ResponseListener());
		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
		// Enable Provider
		adapter.enable((Button) share);
		// share layout
		
	}
	
	private final class ResponseListener implements DialogListener
	{
		@Override
		public void onComplete(Bundle values)
		{
			
			Log.d("ShareButton", "Authentication Successful");
			
			// Get name of provider after authentication
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);
			Toast.makeText(CameraDetailsSharedActivity.this,
					providerName + " connected", Toast.LENGTH_LONG).show();
			
			newsDetailNormalLayout.setVisibility(View.GONE);
			newsDetailShareLayout.setVisibility(View.VISIBLE);
			
			// after connected complete redirect to share page
			Log.d(tag, "emergencyBtnOnClick");
			// adapter.updateStatus(description, new MessageListener(), false);
			
		}
		
		@Override
		public void onError(SocialAuthError error)
		{
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}
		
		@Override
		public void onCancel()
		{
			Log.d("ShareButton", "Authentication Cancelled");
		}
		
		@Override
		public void onBack()
		{
			Log.d("Share-Button", "Dialog Closed by pressing Back Key");
		}
		
	}
	
	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer>
	{
		@Override
		public void onExecute(String provider, Integer t)
		{
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(CameraDetailsSharedActivity.this,
						"Message posted on " + provider, Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(CameraDetailsSharedActivity.this,
						"Message not posted on " + provider, Toast.LENGTH_LONG)
						.show();
		}
		
		@Override
		public void onError(SocialAuthError e)
		{
			
		}
	}
	
	private Bitmap decodeFile(File f)
	{
		try
		{
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			
			// The new size we want to scale to
			final int REQUIRED_SIZE = 256;
			
			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;
			
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void emergencyBtnOnClick(View view)
	{
		Log.d(tag, "emergencyBtnOnClick");
		Intent shareBtn = new Intent(CameraDetailsSharedActivity.this,
				EmergencyCallActivity.class);
		startActivity(shareBtn);
	}
}