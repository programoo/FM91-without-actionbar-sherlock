package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CameraPositionMapActivity extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener {
	private String TAG = this.getClass().getSimpleName();
	private GoogleMap mMap;
	private ArrayList<Camera> camList;
	private ArrayList<Camera> camListFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_position_activity);

		camList = Info.getInstance().camList;
		camListFilter = Info.getInstance().camList;

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.cameraPositionMap)).getMap();
			mMap.setOnMarkerClickListener(this);
			mMap.setOnInfoWindowClickListener(this);
		}

		EditText searchCamEdt = (EditText) findViewById(R.id.positionCameraSearhEdt);
		searchCamEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.i(TAG, "textChange: " + s.toString());
				camListFilter = new ArrayList<Camera>();
				for (int i = 0; i < Info.getInstance().camList.size(); i++) {
					if (Info.getInstance().camList.get(i).toString()
							.indexOf(s.toString()) != -1) {
						camListFilter.add(Info.getInstance().camList.get(i));
					}
				}
				markAll();
			}
		});

	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		markAll();
		return super.onCreateView(parent, name, context, attrs);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		markAll();
	}

	private void markAll() {
		// re-draw marker again
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.insertPositionMap)).getMap();
			mMap.setOnMarkerClickListener(this);
			mMap.setOnInfoWindowClickListener(this);
			//mMap.setOnMapClickListener(this);
		}

		if (mMap != null) {
			mMap.clear();
			// when load complete mark our position
			mMap.getUiSettings().setZoomControlsEnabled(true);
			Marker myMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Info.lat, Info.lng))
					.title(getString(R.string.you_here_msg))
					.snippet(Info.reverseGpsName)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

			myMarker.showInfoWindow();

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					Info.lat, Info.lng), 15));

		}

		for (int i = 0; i < this.camListFilter.size(); i++) {
			myMarker(this.camListFilter.get(i).lat, this.camListFilter.get(i).lng,
					this.camListFilter.get(i).thaiName,this.camListFilter.get(i).id);
		}

		Log.i(TAG, "nearby num: " + camListFilter.size());

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

	private void myMarker(String sLat, String sLng, String title,String id) {
		Log.i(TAG, "re-mark num: " + camListFilter.size());

		LatLng accidentLatLng;
		// set accident lat long
		if (sLat.equalsIgnoreCase("undefined")
				|| sLng.equalsIgnoreCase("undefined")) {
			accidentLatLng = new LatLng(0, 0);

		} else {

			accidentLatLng = new LatLng(Double.parseDouble(sLat),
					Double.parseDouble(sLng));
		}

		if (mMap != null) {
			// calculate distance between user and event
			double howFar = (int) (Info.getInstance().distance(
					accidentLatLng.latitude, accidentLatLng.longitude,
					Info.lat, Info.lng, "K") * 100) / 100.0;
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";
			title = id+":"+title;
			mMap.addMarker(new MarkerOptions().position(accidentLatLng)
					.title(title).snippet(titileDetail));
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		Log.i(TAG, marker.getTitle() + marker.getSnippet());
		Camera cam = Info.getInstance().getCamById(
				marker.getTitle().split("[:]")[0]);
		Intent cameraDetail = new Intent(CameraPositionMapActivity.this,
				CameraDetailsActivity.class);
		// in case of user point
		try {
			cameraDetail.putExtra("description", cam.thaiName + ","
					+ cam.englishName);
			cameraDetail.putExtra("id", cam.id);
			cameraDetail.putExtra("imgList", cam.imgList);
			startActivity(cameraDetail);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

}
