package co.th.mimo.fm91;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.mimotech.testgmapapi.R;

public class CameraDetailsActivity extends FragmentActivity
{
	private String TAG = getClass().getSimpleName();
	private ArrayList<Bitmap> bitMapList;
	ImageView iv;
	TextView tv;
	private boolean run = true;
	private ImageButton bookMarkImgBtn;
	private Camera cam;
	private Context ctx;
	private SocialAuthAdapter adapter;
	private RelativeLayout mainCameraLayout;
	private RelativeLayout mainSharedLayout;
	private AQuery aq;
	private EditText userCommentEdt;
	private boolean isCheck = false;

	@Override
	public void onAttachFragment(Fragment fragment)
	{
		super.onAttachFragment(fragment);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.camera_fragment_detail);
		ctx = this;
		aq = new AQuery(this);
		bitMapList = new ArrayList<Bitmap>();
		this.mainCameraLayout =(RelativeLayout) findViewById(R.id.cameraMainLayout);
		this.mainSharedLayout =(RelativeLayout) findViewById(R.id.cameraSharedLayout);
		this.mainSharedLayout.setVisibility(View.GONE);
		
		Intent intent = getIntent();
		String[] imgList = intent.getStringExtra("imgList").split(",");
		for (int i = 0; i < imgList.length; i++)
		{
			new ImageLoader().downloadBitmapToList(imgList[i], bitMapList);
			bitMapList.add(null);
		}
		String id = intent.getStringExtra("id");
		cam = Info.getInstance().getCamById(id);
		iv = (ImageView) findViewById(R.id.cameraDetail);
		
		tv = (TextView) findViewById(R.id.cameraDescription);
		TextView supporterTv = (TextView) findViewById(R.id.supporterCameraTv);
		
		tv.setText(cam.thaiName + " " + cam.englishName);
		supporterTv.setText(getString(R.string.support_by_text) + " "
				+ cam.source);
		
		bookMarkImgBtn = (ImageButton) findViewById(R.id.bookmarkBookMarkImgBtn);
		
		if(cam.isBookmark){
			bookMarkImgBtn.setImageResource(R.drawable.star_active);
			isCheck = true;
		}
		else{
			bookMarkImgBtn.setImageResource(R.drawable.star_inactive);
			isCheck = false;
		}
		
		bookMarkImgBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(isCheck){
					bookMarkImgBtn.setImageResource(R.drawable.star_inactive);
					isCheck = false;
					cam.isBookmark = isCheck;

				}
				else{
					bookMarkImgBtn.setImageResource(R.drawable.star_active);
					isCheck = true;
					cam.isBookmark = isCheck;
				}
			}
		});
		
		
		//SHARE LAYOUT
		
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
		
		TextView desCamShare = (TextView) findViewById(R.id.descriptionCameraShareTv);
		desCamShare.setText(cam.thaiName+" "+cam.englishName);
		
		TextView sourceCamShare = (TextView) findViewById(R.id.sourceCameraShareTv);
		sourceCamShare.setText(getString(R.string.support_by_text)+" "+cam.source);
		
		ImageView camShareIv = (ImageView) findViewById(R.id.cameraShareIv);
		//camShareIv.setImageBitmap(cam.im);
		aq.id(camShareIv).image(cam.imgUrl);
		
		Button postCameraToWallBtn = (Button) findViewById(R.id.cameraPostShareBtn);
		userCommentEdt = (EditText) findViewById(R.id.cameraShareEdt);
		
		postCameraToWallBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				String message = userCommentEdt.getText().toString()+"\n\n"+cam.thaiName+" "+cam.englishName;
				new uploadImgBgTask().execute(message);

				mainCameraLayout.setVisibility(View.VISIBLE);;
				mainSharedLayout.setVisibility(View.GONE);;
				
			}
		});
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		run = false;
		
		// save state of camera
		cam.isBookmark = isCheck;
		String bufferCameraCSV = "";
		for (int i = 0; i < Info.getInstance().camList.size(); i++)
		{
			bufferCameraCSV += Info.getInstance().camList.get(i).id + "-"
					+ Info.getInstance().camList.get(i).isBookmark + ",";
		}
		Info.getInstance().writeProfile(this, "camera.csv", bufferCameraCSV);
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	public void onAttachedToWindow()
	{
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		new Thread(new HelloRunnable()).start();
		
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			String text = (String) msg.obj;
			int index = Integer.parseInt(text) % 5;
			
			if (bitMapList.size() > index && bitMapList.get(index) != null)
			{	
				cam.imgBmp = bitMapList.get(0);
				iv.setImageBitmap(bitMapList.get(index));
			}
		}
	};
	
	public void updateUIThread(String msgStr)
	{
		Message msg = new Message();
		String textTochange = msgStr;
		msg.obj = textTochange;
		mHandler.sendMessage(msg);
	}
	
	private class HelloRunnable implements Runnable
	{
		
		public void run()
		{
			int i = 1;
			while (run)
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				updateUIThread(i + "");
				i++;
			}
		}
		
	}
	
	public void emergencyBtnOnClick(View view)
	{
		Intent shareBtn = new Intent(CameraDetailsActivity.this,
				EmergencyCallActivity.class);
		startActivity(shareBtn);
	}
	
	public String readProfiles()
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getFilesDir() + File.separator + "camera.csv")));
			String temp = "undefined";
			
			while ((temp = bufferedReader.readLine()) != null)
			{
				read = temp;

			}
			bufferedReader.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return "undefined";
			
		}
		return read;
		
	}
	
	private final class ResponseListener implements DialogListener
	{
		@Override
		public void onComplete(Bundle values)
		{
			
			// Get name of provider after authentication
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Toast.makeText(CameraDetailsActivity.this,
					providerName + " connected", Toast.LENGTH_LONG).show();
			//hide main layout
			mainCameraLayout.setVisibility(View.GONE);
			mainSharedLayout.setVisibility(View.VISIBLE);

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
				Toast.makeText(CameraDetailsActivity.this,
						"Message posted on " + provider, Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(CameraDetailsActivity.this,
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
	
	private class uploadImgBgTask extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... params)
		{
			
			try
			{
				adapter.uploadImage(params[0], "cctvImage.jpg", cam.imgBmp,
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
			Toast.makeText(ctx, "Upload image complete", Toast.LENGTH_LONG).show();
		}
		
	}

	
	
}