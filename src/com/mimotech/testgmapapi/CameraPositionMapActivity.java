package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
	private ArrayList<Marker> markerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_position_activity);

		camList = Info.getInstance().camList;
		camListFilter = Info.getInstance().camList;// new ArrayList<Camera>();
		markerList = new ArrayList<Marker>();
		Log.d(TAG, "onCreate");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		markAll();
	}

	private void markAll() {

		Log.i(TAG, "set up map marker");
		// remove old marker
		for (int i = 0; i < this.markerList.size(); i++) {
			this.markerList.get(i).remove();
		}

		// re-draw marker again
		for (int i = 0; i < camListFilter.size(); i++) {
			myMarker(camListFilter.get(i).lat, camListFilter.get(i).lng,
					camListFilter.get(i).thaiName, camListFilter.get(i).id);
		}
		Log.i(TAG, "camera num: " + camListFilter.size());

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

	private void myMarker(String sLat, String sLng, String title, String id) {

		LatLng accidentLatLng;
		// set accident lat long
		if (sLat.equalsIgnoreCase("undefined")
				|| sLng.equalsIgnoreCase("undefined")) {
			accidentLatLng = new LatLng(0, 0);

		} else {

			accidentLatLng = new LatLng(Double.parseDouble(sLat),
					Double.parseDouble(sLng));

		}

		if (mMap == null) {

			if (mMap == null) {
				mMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.cameraPositionMap)).getMap();
				mMap.setOnMarkerClickListener(this);
				mMap.setOnInfoWindowClickListener(this);

			}

		}

		if (mMap != null) {

			// calculate distance between user and event
			double howFar = (int) (Info.getInstance().distance(accidentLatLng.latitude,
					accidentLatLng.longitude, Info.lat, Info.lng, "K") * 100) / 100.0;
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";

			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng)
					.title(id + ":" + title)
					.snippet(titileDetail));
			// kept it for remove later
			markerList.add(marker);

			mMap.getUiSettings().setZoomControlsEnabled(true);
			// when load complete mark our position

			Marker myMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Info.lat, Info.lng))
					.title(getString(R.string.you_here_msg))
					.snippet(Info.reverseGpsName)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

			myMarker.showInfoWindow();

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					Info.lat, Info.lng), 10));

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
		Camera cam = Info.getInstance().getCamById(marker.getTitle().split("[:]")[0]);
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
