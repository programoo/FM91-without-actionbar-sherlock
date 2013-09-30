package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidquery.AQuery;

public class InsertProfileActivity extends Activity implements OnClickListener
{
	public final String TAG = this.getClass().getSimpleName();
	private static final int REQUEST_CODE = 6384;
	private String pathImgSelected;
	private String userName;
	private String userPhoneNumber;
	private Bitmap bitmapSelected;
	private ImageButton imgBtn;
	private EditText usernameEdt;
	private EditText userPhoneNumberEdt;
	private AQuery aq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_insert_activity);
		this.userName = "";
		this.userPhoneNumber = "";
		usernameEdt = (EditText) findViewById(R.id.userNameEdt);
		userPhoneNumberEdt = (EditText) findViewById(R.id.userPhoneNumberEdt);
		
		imgBtn = (ImageButton) findViewById(R.id.insertImgProfileBtn);
		imgBtn.setOnClickListener(this);
		
		Button saveProfileBtn = (Button) findViewById(R.id.saveProfileBtn);
		saveProfileBtn.setOnClickListener(this);
		
		// read old image
		// read profile if can for showing only
		String temProfile = readProfiles();
		if (!temProfile.equalsIgnoreCase("undefined"))
		{
			Log.i(TAG, "on start");
			Log.i(TAG, temProfile);
			aq = new AQuery(this);
			
			aq.id(imgBtn).image(temProfile.split(",")[0], true, true, 200, 0);
			
			usernameEdt.setText(temProfile.split(",")[1]);
			userPhoneNumberEdt.setText(temProfile.split(",")[2]);
		}
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
	}
	
	public void writeProfile(String imgUrl, String name, String phoneNumber)
	{
		BufferedWriter bufferedWriter;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					getFilesDir() + File.separator + "profile.csv")));
			bufferedWriter.write(imgUrl + "," + name + "," + phoneNumber);
			bufferedWriter.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void showChooser()
	{
		
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case REQUEST_CODE:
				// If the file selection was successful
				if (resultCode == RESULT_OK)
				{
					if (data != null)
					{
						// Get the URI of the selected file
						Uri uri = data.getData();
						
						try
						{
							// Create a file instance from the URI
							File file = new File(Info.getInstance()
									.getRealPathFromURI(this, uri));
							Log.i(TAG, "path: " + file.getAbsolutePath());
							pathImgSelected = file.getAbsolutePath();
							bitmapSelected = decodeFile(file);
							imgBtn.setImageBitmap(bitmapSelected);
							// select file complete
							Log.i(TAG, "image selected");
						} catch (Exception e)
						{
							Log.e("FileSelectorTestActivity",
									"File select error", e);
						}
					}
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.insertImgProfileBtn:
				this.showChooser();
				break;
			case R.id.saveProfileBtn:
				this.userName = usernameEdt.getText().toString();
				this.userPhoneNumber = userPhoneNumberEdt.getText().toString();
				if (this.userName.equals("") || this.userPhoneNumber.equals(""))
				{
					Toast.makeText(
							this,
							"cannot update profie with blank username or phone number",
							Toast.LENGTH_LONG).show();
				} else
				{
					Toast.makeText(this, "update profile complete",
							Toast.LENGTH_LONG).show();
					this.writeProfile(pathImgSelected, this.userName,
							this.userPhoneNumber);
				}
				break;
			case View.NO_ID:
			default:
				Log.e(TAG, "Error here");
				break;
		}
		
	}
	
	public String readProfiles()
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getFilesDir() + File.separator + "profile.csv")));
			String temp = "undefined";
			
			while ((temp = bufferedReader.readLine()) != null)
			{
				read = temp;
				Log.i(TAG, "read from read: " + read);
				
			}
			bufferedReader.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return "undefined";
			
		}
		
		return read;
		
	}
}
