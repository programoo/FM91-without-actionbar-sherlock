package co.th.mimo.fm91;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewsDetailsActivity extends FragmentActivity implements
		OnClickListener
{
	private String TAG = getClass().getSimpleName();
	private GoogleMap mMap;
	private Bitmap snapShot;
	private SocialAuthAdapter adapter;
	private String description;
	private RelativeLayout newsDetailNormalLayout;
	private RelativeLayout newsDetailShareLayout;
	private EditText newsDetailsShareEditText;
	
	// share layout
	private ImageView uploadIv;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_fragment_detail_activity);
		
		newsDetailNormalLayout = (RelativeLayout) findViewById(R.id.news_detail_normal_layout);
		newsDetailShareLayout = (RelativeLayout) findViewById(R.id.news_detail_share_layout);
		
		newsDetailNormalLayout.setVisibility(View.VISIBLE);
		newsDetailShareLayout.setVisibility(View.GONE);
		
		Intent intent = getIntent();
		String sLat = intent.getStringExtra("startPointLat");
		String sLng = intent.getStringExtra("startPointLong");
		String title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		String source = intent.getStringExtra("source");
		String time = intent.getStringExtra("time");
		
		// normal layout
		uploadIv = (ImageView) findViewById(R.id.uploadImgIv);
		
		TextView titleTv = (TextView) findViewById(R.id.titleNewsTv);
		titleTv.setText(title);
		
		TextView tv = (TextView) findViewById(R.id.newsTextDetail);
		tv.setText(description);
		
		TextView sourceNewTv = (TextView) findViewById(R.id.sourceNewsTv);
		sourceNewTv.setText(getString(R.string.by_text) + " " + source);
		
		TextView timeNewsTv = (TextView) findViewById(R.id.timeNewsTv);
		timeNewsTv.setText(time);
		
		this.myMarker(sLat, sLng, title);
		Button share = (Button) findViewById(R.id.shareBtn);
		
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
		TextView shareDetailTv = (TextView) findViewById(R.id.news_details_msg);
		shareDetailTv.setText(description);

		TextView shareTitleTv = (TextView) findViewById(R.id.news_title_msg);
		shareTitleTv.setText(title);

		
		TextView shareSourceTv = (TextView) findViewById(R.id.news_details_share_srcTv);
		shareSourceTv.setText(getString(R.string.by_text)+": "+source);
		
		Button shareButton = (Button) findViewById(R.id.newsDetailsShareBtn);
		shareButton.setOnClickListener(this);
		
		newsDetailsShareEditText = (EditText) findViewById(R.id.newsDetailsShareEditText);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	public void myMarker(String sLat, String sLng, String title)
	{
		
		LatLng accidentLatLng = new LatLng(0, 0);
		if (!sLat.equalsIgnoreCase("undefined")
				&& !sLng.equalsIgnoreCase("undefined"))
		{
			
			try
			{
				accidentLatLng = new LatLng(Double.parseDouble(sLat),
						Double.parseDouble(sLng));
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
			
		}
		
		if (mMap == null)
		{
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.newsMap)).getMap();
		}
		
		if (mMap != null)
		{
			// calculate distance between user and event
			double howFar = (int) (Info.getInstance().distance(
					accidentLatLng.latitude, accidentLatLng.longitude,
					Info.lat, Info.lng, "K") * 100) / 100.0;
			
			if (accidentLatLng.latitude == 0 || accidentLatLng.longitude == 0)
			{
				howFar = 0;
			}
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";
			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(title)
					.snippet(titileDetail));
			// user marker
			new LatLng(Info.lat, Info.lng);
			titileDetail = getString(R.string.farfromyou_msg) + ": " + howFar
					+ " km";
			mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Info.lat, Info.lng))
					.title(getString(R.string.you_here_msg))
					.snippet(Info.reverseGpsName)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			
			mMap.getUiSettings().setZoomControlsEnabled(true);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng,
					11));
			// myMarker.showInfoWindow();
			marker.showInfoWindow();
			
		}
	}
	
	private final class ResponseListener implements DialogListener
	{
		@Override
		public void onComplete(Bundle values)
		{
			
			// Get name of provider after authentication
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Toast.makeText(NewsDetailsActivity.this,
					providerName + " connected", Toast.LENGTH_LONG).show();
			
			CaptureMapScreen();
			newsDetailNormalLayout.setVisibility(View.GONE);
			newsDetailShareLayout.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onError(SocialAuthError error)
		{
			//Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}
		
		@Override
		public void onCancel()
		{
			//Log.d("ShareButton", "Authentication Cancelled");
		}
		
		@Override
		public void onBack()
		{
			//Log.d("Share-Button", "Dialog Closed by pressing Back Key");
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
				Toast.makeText(NewsDetailsActivity.this,
						"Message posted on " + provider, Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(NewsDetailsActivity.this,
						"Message not posted on " + provider, Toast.LENGTH_LONG)
						.show();
		}
		
		@Override
		public void onError(SocialAuthError e)
		{
			
		}
	}
	
	@Override
	public void onClick(View v)
	{
		
		String quotMsg = newsDetailsShareEditText.getText().toString() + "\n\n"
				+ description;
		//adapter.updateStatus(quotMsg, new MessageListener(), false);
		
		// prevent re-post return to first step
		newsDetailNormalLayout.setVisibility(View.VISIBLE);
		newsDetailShareLayout.setVisibility(View.GONE);
		try
		{
			
			Toast.makeText(NewsDetailsActivity.this, "Posting message...",
					Toast.LENGTH_LONG).show();
			
			new uploadImgBgTask().execute(quotMsg);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private class uploadImgBgTask extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... params)
		{
			
			try
			{
				adapter.uploadImage(params[0], "googleMap.jpg", snapShot,
						100);
			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
		}
		
	}
	
	public void CaptureMapScreen()
	{
		SnapshotReadyCallback callback = new SnapshotReadyCallback()
		{
			
			@Override
			public void onSnapshotReady(Bitmap snapshot)
			{
				// TODO Auto-generated method stub
				snapShot = snapshot;
				uploadIv.setImageBitmap(snapShot);
			}
		};
		
		mMap.snapshot(callback);
	}
	
	public void emergencyBtnOnClick(View view)
	{
		Intent shareBtn = new Intent(NewsDetailsActivity.this,
				EmergencyCallActivity.class);
		startActivity(shareBtn);
	}
}