package co.th.mimo.fm91;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.androidquery.util.XmlDom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mimotech.testgmapapi.R;

public class InformMapSelectorActivity extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMapClickListener,
		TextWatcher {
	private String TAG = this.getClass().getSimpleName();
	private ArrayList<Nearby> nearbyList;
	private ArrayList<Nearby> nearbyTemPlateList;

	private GoogleMap mMap;
	private ListView lv;
	private RelativeLayout mapViewLayout;
	private RelativeLayout listViewLayout;
	boolean showMapView = true;
	private EditText filterEdt;
	private ImageButton imageDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nearbyList = new ArrayList<Nearby>();
		nearbyTemPlateList = new ArrayList<Nearby>();
		setContentView(R.layout.activity_infom_mapview_selector);
		mapViewLayout = (RelativeLayout) findViewById(R.id.mapSelectorMapView);
		listViewLayout = (RelativeLayout) findViewById(R.id.mapSelectorListView);
		filterEdt = (EditText) findViewById(R.id.mapFilterEdt);
		filterEdt.addTextChangedListener(this);

		lv = (ListView) findViewById(R.id.insertPositionListView);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				Nearby nearby = (Nearby) lv.getItemAtPosition(arg2);
				String result = nearby.lat + "," + nearby.lng;
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", result);
				setResult(Info.RESULT_OK, returnIntent);
				finish();

			}
		});
		imageDialog = (ImageButton) findViewById(R.id.imageDialog);
		imageDialog.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (showMapView) {
					mapViewLayout.setVisibility(View.GONE);
					listViewLayout.setVisibility(View.VISIBLE);
					imageDialog.setImageResource(R.drawable.map_mapview_img);
					showMapView = false;
				} else {
					mapViewLayout.setVisibility(View.VISIBLE);
					listViewLayout.setVisibility(View.GONE);
					imageDialog.setImageResource(R.drawable.map_listview_img);
					showMapView = true;
				}
				reloadView();
			}
		});

	}

	public void reloadView() {
		InformMapSelectorListViewAdapter ardap = new InformMapSelectorListViewAdapter(
				InformMapSelectorActivity.this, this.nearbyList);
		lv.setAdapter(ardap);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.mapViewLayout.setVisibility(View.VISIBLE);
		this.listViewLayout.setVisibility(View.GONE);
		new RetreiveFeedTask().execute("defined in doInBackground");
	}

	private void markAll() {
		// re-draw marker again
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.insertPositionMap)).getMap();
			mMap.setOnMarkerClickListener(this);
			mMap.setOnInfoWindowClickListener(this);
			mMap.setOnMapClickListener(this);

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

		for (int i = 0; i < this.nearbyList.size(); i++) {
			myMarker(this.nearbyList.get(i).lat, this.nearbyList.get(i).lng,
					this.nearbyList.get(i).title);
		}

	}

	private void myMarker(String sLat, String sLng, String title) {
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

			mMap.addMarker(new MarkerOptions().position(accidentLatLng)
					.title(title).snippet(titileDetail));
		}
	}

	class RetreiveFeedTask extends AsyncTask<String, String, String> {
		protected String doInBackground(String... urls) {
			HttpResponse response = null;
			String inputStreamAsString = "undefined";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(
						"https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location="
								+ Info.lat
								+ ","
								+ Info.lng
								+ "&rankby=prominence&radius=500&sensor=false&key=AIzaSyCGwL4iF8lgumHDZvWmwArYtZknFZeGuYY"));
				response = client.execute(request);
				inputStreamAsString = convertStreamToString(response
						.getEntity().getContent());

			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return inputStreamAsString;
		}

		protected void onPostExecute(String inputStreamAsString) {
			// TODO: check this.exception
			// TODO: do something with the feed
			try {
				XmlDom xmlJa = new XmlDom(inputStreamAsString);
				nearByParsingToObj(xmlJa);
				// copy to template nearby;
				for (int i = 0; i < nearbyList.size(); i++) {
					nearbyTemPlateList.add(nearbyList.get(i).clone());
				}
				// after get nearby obj then draw to gMap
				markAll();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public String convertStreamToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public void nearByParsingToObj(XmlDom xml) {

		List<XmlDom> entries;
		// tempList = new ArrayList<News>();
		// List<String> titles = new ArrayList<String>();

		try {
			entries = xml.tags("result");
		} catch (NullPointerException e) {
			e.printStackTrace();
			return;
		}

		for (XmlDom entry : entries) {

			String name = entry.text("name");
			String vicinity = entry.text("vicinity");
			String type = entry.text("type");
			String geometry = entry.child("geometry").text();
			String lat = entry.child("geometry").child("location").child("lat")
					.text();
			String lng = entry.child("geometry").child("location").child("lng")
					.text();

			uniqueAddBearby(new Nearby(name + " " + vicinity + " " + type, lat,
					lng));

		}
	}

	public void uniqueAddBearby(Nearby nb) {
		for (int i = 0; i < this.nearbyList.size(); i++) {
			if (this.nearbyList.get(i).title.equalsIgnoreCase(nb.title)) {
				return;
			}
		}
		this.nearbyList.add(nb);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		String result = "undefined";

		if (marker.getTitle()
				.equalsIgnoreCase(getString(R.string.you_here_msg))) {
			result = Info.lat + "," + Info.lng;
		} else {
			result = getNearbyFromTitle(marker.getTitle()).lat + ","
					+ getNearbyFromTitle(marker.getTitle()).lng;
		}

		CaptureMapScreen();

		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", result);
		setResult(Info.RESULT_OK, returnIntent);
		finish();
	}

	@Override
	public void onMapClick(LatLng point) {
		// capture image
		CaptureMapScreen();

		String result = point.latitude + "," + point.longitude;
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", result);
		setResult(Info.RESULT_OK, returnIntent);
		finish();
	}

	public Nearby getNearbyFromTitle(String title) {
		for (int i = 0; i < this.nearbyList.size(); i++) {
			if (this.nearbyList.get(i).title.equalsIgnoreCase(title)) {
				return this.nearbyList.get(i);
			}
		}
		return null;
	}

	@Override
	public void afterTextChanged(Editable s) {

		nearbyList = new ArrayList<Nearby>();

		for (int i = 0; i < this.nearbyTemPlateList.size(); i++) {
			if (this.nearbyTemPlateList.get(i).title.indexOf(this.filterEdt
					.getText().toString()) != -1) {
				// found add it
				nearbyList.add(this.nearbyTemPlateList.get(i));
			}
		}

		this.reloadView();
		this.markAll();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public void CaptureMapScreen() {
		SnapshotReadyCallback callback = new SnapshotReadyCallback() {

			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				Info.getInstance().snapShot = snapshot;
			}
		};

		mMap.snapshot(callback);
	}

}
