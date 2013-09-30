package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class Info {
	//Bangkok as default
	public static double lat = 13.724714;
	public static double lng = 100.633111;
	public static String reverseGpsName="";
	public ArrayList<Camera> camList;
	public static final int RESULT_OK = 500;
	public static final int RESULT_CANCELED = 501;
	public static final int RESULT_SELECTED_IMAGE = 502;
	public static final int RESULT_SELECTED_POSITION = 503;

	public static boolean crimTick = false;
	public static boolean accidentTick = false;
	public static boolean otherTick = false;
	public static String latLnConfig;
	public static String radius;
	public static String rewind;
	//singleton pattern
	private static Info instance = null;
    private Info() {}
 
    public static Info getInstance() {
        if (instance == null) {
            instance = new Info();
        }
        return instance;
    }
	public double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit.equalsIgnoreCase("K")) {
			dist = dist * 1.609344;
		} else if (unit.equalsIgnoreCase("N")) {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public Camera getCamById(String id) {
		for (int i = 0; i < Info.getInstance().camList.size(); i++) {
			if (Info.getInstance().camList.get(i).id.equalsIgnoreCase(id)) {
				return Info.getInstance().camList.get(i);
			}
		}
		return null;
	}

	public static Bitmap decodeFile(File f,int requireSize)
	{
		try
		{
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			
			// The new size we want to scale to
			int REQUIRED_SIZE = requireSize;
			
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
	
	public void writeProfile(Context ctx,String fileName,String data)
	{
		BufferedWriter bufferedWriter;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					ctx.getFilesDir() + File.separator + fileName)));
			bufferedWriter.write(data);
			bufferedWriter.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String readProfiles(Context ctx,String fileName)
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					ctx.getFilesDir() + File.separator
							+ fileName)));
			String temp = "undefined";
			while ((temp = bufferedReader.readLine()) != null)
			{
				read = temp;
				Log.i("Info", "read from read: " + read);
			}
			bufferedReader.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return "undefined";
			
		}
		return read;
		
	}
	
	public String getRealPathFromURI(Context ctx, Uri contentUri)
	{
		String[] proj =
		{ MediaStore.Images.Media.DATA };
		Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void sortCamByBookmark(){
		//bubble sort
		for(int i=0;i<Info.getInstance().camList.size();i++){
			
			
		}
		
	}
	
}
