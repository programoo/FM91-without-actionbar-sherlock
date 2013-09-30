package com.mimotech.testgmapapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class CameraFragment extends Fragment implements TextWatcher
{
	private String TAG = this.getClass().getSimpleName();
	private View v;
	private ArrayList<Camera> camListFilter;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private GridView gv;
	private EditText searchCameraEdt;
	private AQuery aq;
	private String camSaveState;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Info.getInstance().camList = new ArrayList<Camera>();
		camListFilter = new ArrayList<Camera>();
		aq = new AQuery(getActivity());
		
		// read bookmark cam
		camSaveState = Info.getInstance().readProfiles(getActivity(),
				"camera.csv");
		if (!camSaveState.equalsIgnoreCase("undefined"))
		{
			
		}
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		
		locationListener = new MyLocationListener();
		Log.d(TAG, "Request location");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);
	}
	
	public boolean isBookMark(String camId)
	{
		String camBookList[] = camSaveState.split(",");
		Log.i(TAG, "read camera.csv: " + camSaveState);
		for (int i = 0; i < camBookList.length; i++)
		{
			try
			{
				
				String camIdA = camBookList[i].split("-")[0];
				boolean isBook = Boolean
						.parseBoolean(camBookList[i].split("-")[1]);
				if (camId.equalsIgnoreCase(camIdA))
				{
					if (isBook)
					{
						return true;
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.v = inflater.inflate(R.layout.camera_fragment, container, false);
		
		gv = (GridView) v.findViewById(R.id.cameraGridView);
		CameraGridViewAdapter ardap = new CameraGridViewAdapter(getActivity()
				.getApplicationContext(), Info.getInstance().camList);
		
		gv.setAdapter(ardap);
		
		// handle edittext event
		searchCameraEdt = (EditText) v.findViewById(R.id.searchCameraEdt);
		searchCameraEdt.addTextChangedListener(this);
		
		// handle gridview click
		gv.setOnItemClickListener(
		
		new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3)
			{
				
				Log.d(TAG, "arg2: " + arg2 + "," + "arg3: " + arg3);
				Camera cam = (Camera) gv.getItemAtPosition(arg2);
				
				Intent cameraDetail = new Intent(getActivity(),
						CameraDetailsActivity.class);
				
				cameraDetail.putExtra("id", cam.id);
				cameraDetail.putExtra("imgList", cam.imgList);
				startActivity(cameraDetail);
				
			}
		});
		
		// handle click event
		Button positionBtn = (Button) v.findViewById(R.id.positionBtn);
		positionBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Log.d(TAG, "positionBtnsetOnClickListener ja");
				Intent cameraMap = new Intent(getActivity(),
						CameraPositionMapActivity.class);
				
				cameraMap.putExtra("description", "kak description");
				cameraMap.putExtra("imgList", "image");
				startActivity(cameraMap);
				
			}
		});
		
		Button cctvBtn = (Button) v.findViewById(R.id.cctvBtn);
		cctvBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Log.d(TAG, "cctvOnClickListener ja");
				
				camListFilter = new ArrayList<Camera>();
				
				for (int i = 0; i < Info.getInstance().camList.size(); i++)
				{
					if (Info.getInstance().camList.get(i).toString()
							.indexOf(searchCameraEdt.getText().toString()) != -1)
					{
						camListFilter.add(Info.getInstance().camList.get(i));
					}
				}
				
				CameraGridViewAdapter ardap = new CameraGridViewAdapter(
						getActivity().getApplicationContext(), camListFilter);
				
				gv.setAdapter(ardap);
			}
		});
		
		Log.i(TAG, "onCreateView");
		return v;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		Log.i(TAG, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
		
		new RequestTask("getRandomStr")
				.execute("http://api.traffy.in.th/apis/getKey.php?appid=abcb6710");
		
	}
	
	public void onStart()
	{
		super.onStart();
	}
	
	public void onResume()
	{
		super.onResume();
		
	}
	
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	
	private void reloadViewAfterRequestTaskComplete()
	{
		// get current location by gps
		
		Log.d(TAG, "Request location");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);
		// filter with
		camListFilter = new ArrayList<Camera>();
		
		for (int i = 0; i < Info.getInstance().camList.size(); i++)
		{
			if (Info.getInstance().camList.get(i).toString()
					.indexOf(searchCameraEdt.getText().toString()) != -1)
			{
				camListFilter.add(Info.getInstance().camList.get(i));
			}
		}
		
		// request for gpis
		asyncJson();
	}
	
	private class RequestTask extends AsyncTask<String, String, String>
	{
		private String tag = getClass().getSimpleName();
		public String AppID = "abcb6710";
		public String hiddenkey = "TxLYP6j1Ee";
		public String randomStr = "undefined";
		public String requestType = "";
		
		public RequestTask(String requestType)
		{
			this.requestType = requestType;
		}
		
		@Override
		protected String doInBackground(String... uri)
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try
			{
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK)
				{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else
				{
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return responseString;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			Log.d(this.getClass().getSimpleName(), "cctv onPostExecute");
			// Log.i(tag, "result: " + result);
			
			// Do anything with response..
			if (requestType.equalsIgnoreCase("getRandomStr"))
			{
				randomStr = result;
				String passKey = "";
				try
				{
					passKey = convertToMD5(AppID + randomStr)
							+ convertToMD5(hiddenkey + randomStr);
				} catch (NoSuchAlgorithmException e)
				{
					e.printStackTrace();
				}
				
				/*
				 * http://athena.traffy.in.th/apis/apitraffy.php?format=XML&api=
				 * getCCTV&key=(passkey from combination)&appid=(appId from
				 * registation)
				 */
				// http://athena.traffy.in.th/apis/apitraffy.php?format=…&api=…&available=…&key=…&appid=..
				// .
				Log.i(tag, "cctv passkey: " + passKey);
				String traffy_request_url = "http://athena.traffy.in.th/apis/apitraffy.php?format="
						+ "XML&api=getCCTV&available=t&key="
						+ passKey
						+ "&appid=" + AppID;
				
				new RequestTask("getData").execute(traffy_request_url);
			} else if (requestType.equalsIgnoreCase("getData"))
			{
				// Log.i(tag, "result cctv: " + result);
				// this mean we get real data from traffy already
				this.traffyCameraXmlParser(result);
				reloadViewAfterRequestTaskComplete();
			}
			
		}
		
		public String convertToMD5(String msg) throws NoSuchAlgorithmException
		{
			String plaintext = msg;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32)
			{
				hashtext = "0" + hashtext;
			}
			
			return hashtext;
		}
		
		public void traffyCameraXmlParser(String xmlString)
		{
			if (xmlString == null)
			{
				Log.e(tag, "traffyNewsXmlParser: nullString");
				return;
			}
			
			Document doc = null;
			NodeList nList = null;
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(xmlString));
				doc = builder.parse(is);
				nList = doc.getElementsByTagName("cctv");
				
			} catch (ParserConfigurationException e)
			{
				e.printStackTrace();
			} catch (SAXException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (NullPointerException e)
			{
				e.printStackTrace();
			}
			
			if (nList != null)
				for (int temp = 0; temp < nList.getLength(); temp++)
				{
					
					Node nNode = nList.item(temp);
					
					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
						
						Element eElement = (Element) nNode;
						String id = eElement.getAttribute("no");
						String nameEng = eElement.getAttribute("name");
						String nameTH = eElement.getAttribute("name_th");
						
						String lat = eElement.getElementsByTagName("point")
								.item(0).getAttributes().getNamedItem("lat")
								.getNodeValue();
						String lng = eElement.getElementsByTagName("point")
								.item(0).getAttributes().getNamedItem("lng")
								.getNodeValue();
						String available = eElement.getAttribute("available");
						String imgUrl = eElement.getElementsByTagName("url")
								.item(0).getTextContent();
						
						String lastupdate = eElement
								.getElementsByTagName("lastupdate").item(0)
								.getTextContent();
						String src = eElement.getElementsByTagName("src")
								.item(0).getTextContent();
						String description = eElement
								.getElementsByTagName("desc").item(0)
								.getTextContent();
						String imgList = eElement.getElementsByTagName("list")
								.item(0).getTextContent();
						
						Camera cam = new Camera(id, nameEng, nameTH, lat, lng,
								available, imgUrl, lastupdate, description,
								src, imgList);
						// set bookmark
						cam.isBookmark = isBookMark(cam.id);
						this.uniqueAdd(cam);
						
					}
				}
			
		}// end xml parser
		
		public void uniqueAdd(Camera cam)
		{
			
			for (int i = 0; i < Info.getInstance().camList.size(); i++)
			{
				if (cam.id
						.equalsIgnoreCase(Info.getInstance().camList.get(i).id))
				{
					return;
				}
			}
			Info.getInstance().camList.add(cam);
		}
		
	}// end private request class
	
	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "afterTextChanged" + s.toString());
		// re-create new list for show only user
		camListFilter = new ArrayList<Camera>();
		
		for (int i = 0; i < Info.getInstance().camList.size(); i++)
		{
			if (Info.getInstance().camList.get(i).toString()
					.indexOf(s.toString()) != -1)
			{
				camListFilter.add(Info.getInstance().camList.get(i));
			}
		}
		// re-draw gridview
		CameraGridViewAdapter ardap = new CameraGridViewAdapter(getActivity()
				.getApplicationContext(), camListFilter);
		gv.setAdapter(ardap);
		
		// re-draw camera
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "beforeTextChanged" + s.toString());
		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onTextChanged" + s.toString());
		
	}
	
	public void asyncJson()
	{
		
		// perform a Google search in just a few lines of code
		
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ Info.lat + "," + Info.lng + "&sensor=true";
		
		// String url = "http://www.thairath.co.th/rss/news.xml";
		aq.ajax(url, JSONObject.class, this, "jsonCallback");
		
	}
	
	public void jsonCallback(String url, JSONObject json, AjaxStatus status)
	{
		
		if (json != null)
		{
			// successful ajax call
			Log.i(TAG, "json: " + json.toString());
			String reverseGpsName = json.toString().split(
					"\"formatted_address\":\"")[1].split("\",\"")[0];
			Info.reverseGpsName = reverseGpsName;
			
		} else
		{
			// ajax error
		}
		
	}
	
	private class MyLocationListener implements LocationListener
	{
		
		@Override
		public void onLocationChanged(Location loc)
		{
			/*
			 * Toast.makeText( getActivity().getBaseContext(),
			 * "Location changed: Lat: " + loc.getLatitude() + " Lng: " +
			 * loc.getLongitude(), Toast.LENGTH_SHORT).show();
			 */
			String longitude = "Longitude: " + loc.getLongitude();
			String latitude = "Latitude: " + loc.getLatitude();
			
			Log.i(TAG, "your current location:" + latitude + "," + longitude);
			
			Info.lat = loc.getLatitude();
			Info.lng = loc.getLongitude();
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{
			Toast.makeText(getActivity(),
					"onProviderDisabled" + R.string.gps_disconnect_alert,
					Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onProviderEnabled(String provider)
		{
			Toast.makeText(getActivity(),
					"onProviderEnabled" + R.string.gps_disconnect_alert,
					Toast.LENGTH_LONG).show();
			
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			Toast.makeText(getActivity(),
					"onStatusChanged" + R.string.gps_disconnect_alert,
					Toast.LENGTH_LONG).show();
			
		}
	}
	
}
