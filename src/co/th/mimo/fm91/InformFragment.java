package co.th.mimo.fm91;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class InformFragment extends Fragment implements OnClickListener,
		OnMarkerClickListener, OnInfoWindowClickListener, OnMapClickListener
{
	private String TAG = this.getClass().getSimpleName();
	private View v;
	private RelativeLayout mainLayout;
	private RelativeLayout detailLayout;
	private TextView tv;
	private AQuery aq;
	private Context context;
	private ArrayList<Nearby> nearbyList;
	private GoogleMap mMap;
	private Dialog dialog;
	private ImageButton addImgImgBtn;
	private ImageButton addPlaceImgBtn;
	private ImageButton sendBtn;
	private ImageButton clearBtn;
	
	// data for sending to server
	private String pathImgSelected;
	private String imgName;
	private String latLngSelected;
	private String titleTraffy;
	private String userImgUrl;
	private String userName;
	private String phoneNum;
	private EditText descriptionEdt;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		aq = new AQuery(getActivity());
		nearbyList = new ArrayList<Nearby>();
		pathImgSelected = "";
		latLngSelected = "0,0";
		titleTraffy = "undefined";
		userImgUrl = "undefined";
		userName = "undefined";
		phoneNum = "undefined";
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		this.v = inflater.inflate(R.layout.inform_fragment, container, false);
		
		mainLayout = (RelativeLayout) v.findViewById(R.id.informMainLayout);
		detailLayout = (RelativeLayout) v.findViewById(R.id.informDetailLayout);
		
		// for handle back press
		Info.getInstance().mainLayout = mainLayout;
		Info.getInstance().detailLayout = detailLayout;
		
		clearBtn = (ImageButton) v.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// mainLayout.setVisibility(View.VISIBLE);
				// detailLayout.setVisibility(View.GONE);
				
				descriptionEdt.setText("");
				addImgImgBtn.setImageResource(R.drawable.add_image);
				addPlaceImgBtn.setImageResource(R.drawable.add_location);
			}
		});
		
		sendBtn = (ImageButton) v.findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// post image
				String params[] = new String[2];
				params[0] = "http://203.185.131.171/CrimeMap/Json/upload.php";
				params[1] = pathImgSelected;
				new UploadImage().execute(params);
				
				// post data
				String url = "http://203.185.131.171/CrimeMap/Json/inform.php?tel="
						+ phoneNum
						+ "&title="
						+ titleTraffy
						+ "&name="
						+ userName
						+ "&description="
						+ descriptionEdt.getText().toString()
						+ "&Lat="
						+ latLngSelected.split(",")[0]
						+ "&Lng="
						+ latLngSelected.split(",")[1] + "&image=" + imgName;
				aq.ajax(url, String.class, new AjaxCallback<String>()
				{
					
					@Override
					public void callback(String url, String html,
							AjaxStatus status)
					{
					}
					
				});
				
				mainLayout.setVisibility(View.VISIBLE);
				detailLayout.setVisibility(View.GONE);
				
				descriptionEdt.setText("");
				addImgImgBtn.setImageResource(R.drawable.add_image);
				addPlaceImgBtn.setImageResource(R.drawable.add_location);
				
			}
		});
		
		tv = (TextView) v.findViewById(R.id.typeOfInformTv);
		
		ImageButton traffBtn = (ImageButton) v.findViewById(R.id.trafficImgBtn);
		traffBtn.setOnClickListener(this);
		traffBtn.setTag(getString(R.string.traffic_text));
		
		ImageButton accidentImgBtn = (ImageButton) v
				.findViewById(R.id.accidentImgBtn);
		accidentImgBtn.setOnClickListener(this);
		accidentImgBtn.setTag(getString(R.string.accident_text));
		
		ImageButton crimeImgBtn = (ImageButton) v
				.findViewById(R.id.crimeImgBtn);
		crimeImgBtn.setOnClickListener(this);
		crimeImgBtn.setTag(getString(R.string.crime_text));
		
		ImageButton transportImgBtn = (ImageButton) v
				.findViewById(R.id.transportImgBtn);
		transportImgBtn.setOnClickListener(this);
		transportImgBtn.setTag(getString(R.string.transport_text));
		
		ImageButton powerShutdownImgBtn = (ImageButton) v
				.findViewById(R.id.powerShutdownImgBtn);
		powerShutdownImgBtn.setOnClickListener(this);
		powerShutdownImgBtn.setTag(getString(R.string.powershutdown_text));
		
		ImageButton hydrantImgBtn = (ImageButton) v
				.findViewById(R.id.hydrantImgBtn);
		hydrantImgBtn.setOnClickListener(this);
		hydrantImgBtn.setTag(getString(R.string.hydrant_text));
		
		ImageButton firealarmImgBtn = (ImageButton) v
				.findViewById(R.id.firealarmImgBtn);
		firealarmImgBtn.setOnClickListener(this);
		firealarmImgBtn.setTag(getString(R.string.firealarm_text));
		
		ImageButton otherImgBtn = (ImageButton) v
				.findViewById(R.id.otherImgBtn);
		otherImgBtn.setOnClickListener(this);
		otherImgBtn.setTag(getString(R.string.other_text));
		
		descriptionEdt = (EditText) v.findViewById(R.id.informDetailEdt);
		
		addImgImgBtn = (ImageButton) v.findViewById(R.id.addImgInformImgBtn);
		addImgImgBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				
				showChooser();
			}
			
		});
		
		addPlaceImgBtn = (ImageButton) v
				.findViewById(R.id.addPlaceInformImgBtn);
		addPlaceImgBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				Intent i = new Intent(getActivity(),
						InformMapSelectorActivity.class);
				startActivityForResult(i, Info.RESULT_SELECTED_POSITION);
			}
			
		});
		
		this.mainLayout.setVisibility(View.VISIBLE);
		this.detailLayout.setVisibility(View.GONE);
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}
	
	private void markAll()
	{
		
		// re-draw marker again
		for (int i = 0; i < this.nearbyList.size(); i++)
		{
			myMarker(this.nearbyList.get(i).lat, this.nearbyList.get(i).lng,
					this.nearbyList.get(i).title);
		}
		
	}
	
	private void myMarker(String sLat, String sLng, String title)
	{
		LatLng accidentLatLng;
		// set accident lat long
		if (sLat.equalsIgnoreCase("undefined")
				|| sLng.equalsIgnoreCase("undefined"))
		{
			accidentLatLng = new LatLng(0, 0);
			
		} else
		{
			
			accidentLatLng = new LatLng(Double.parseDouble(sLat),
					Double.parseDouble(sLng));
			
		}
		
		if (mMap == null)
		{
			
			if (mMap == null)
			{
				mMap = ((SupportMapFragment) getActivity()
						.getSupportFragmentManager().findFragmentById(
								R.id.insertPositionMap)).getMap();
				mMap.setOnMarkerClickListener(this);
				mMap.setOnInfoWindowClickListener(this);
				mMap.setOnMapClickListener(this);
				
			}
			
		}
		
		if (mMap != null)
		{
			mMap.clear();
			// calculate distance between user and event
			double howFar = (int) (Info.getInstance().distance(
					accidentLatLng.latitude, accidentLatLng.longitude,
					Info.lat, Info.lng, "K") * 100) / 100.0;
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";
			
			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(title)
					.snippet(titileDetail));
			
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
					Info.lat, Info.lng), 15));
			
		}
		
	}
	
	class RetreiveFeedTask extends AsyncTask<String, String, String>
	{
		protected String doInBackground(String... urls)
		{
			HttpResponse response = null;
			String inputStreamAsString = "undefined";
			try
			{
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
				
			} catch (URISyntaxException e)
			{
				e.printStackTrace();
			} catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return inputStreamAsString;
		}
		
		protected void onPostExecute(String inputStreamAsString)
		{
			try
			{
				XmlDom xmlJa = new XmlDom(inputStreamAsString);
				nearByParsingToObj(xmlJa);
				// after get nearby obj then draw to gMap
				markAll();
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	public String convertStreamToString(InputStream inputStream)
			throws IOException
	{
		if (inputStream != null)
		{
			Writer writer = new StringWriter();
			
			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			} finally
			{
				inputStream.close();
			}
			return writer.toString();
		} else
		{
			return "";
		}
	}
	
	public void nearByParsingToObj(XmlDom xml)
	{
		List<XmlDom> entries;
		try
		{
			entries = xml.tags("result");
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			return;
		}
		
		for (XmlDom entry : entries)
		{
			
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
	
	public void uniqueAddBearby(Nearby nb)
	{
		for (int i = 0; i < this.nearbyList.size(); i++)
		{
			if (this.nearbyList.get(i).title.equalsIgnoreCase(nb.title))
			{
				return;
			}
		}
		this.nearbyList.add(nb);
	}
	
	public String readProfiles()
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getActivity().getFilesDir() + File.separator
							+ "profile.csv")));
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
	
	@Override
	public void onClick(View v)
	{
		String userProfileSetting = this.readProfiles();
		if (!userProfileSetting.equalsIgnoreCase("undefined"))
		{
			try
			{
				// have user profile read it and share to
				this.mainLayout.setVisibility(View.GONE);
				this.detailLayout.setVisibility(View.VISIBLE);
				titleTraffy = v.getTag().toString();
				userImgUrl = userProfileSetting.split(",")[0];
				userName = userProfileSetting.split(",")[1];
				phoneNum = userProfileSetting.split(",")[2];
				
				tv.setText(v.getTag().toString());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		} else
		{
			// redirect to user profile page
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getActivity());
			builder1.setMessage(getString(R.string.insert_your_data));
			builder1.setCancelable(true);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							dialog.cancel();
							
							Intent it = new Intent(getActivity(),
									InsertProfileActivity.class);
							startActivity(it);
						}
					});
			builder1.setNegativeButton("No",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							dialog.cancel();
						}
					});
			
			AlertDialog alert11 = builder1.create();
			alert11.show();
		}
		
	}
	
	private void showChooser()
	{
		// Use the GET_CONTENT intent from the utility class
		/*
		 * Intent target = FileUtils.createGetContentIntent(); // Create the
		 * chooser Intent Intent intent = Intent.createChooser(target,
		 * getString(R.string.chooser_label));
		 */
		
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, Info.RESULT_SELECTED_IMAGE);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == Info.RESULT_SELECTED_POSITION)
		{
			
			if (resultCode == Info.RESULT_OK)
			{
				String result = data.getStringExtra("result");
				addPlaceImgBtn.setImageBitmap(Info.getInstance().snapShot);
				latLngSelected = result;
			}
			if (resultCode == Info.RESULT_CANCELED)
			{
				// Write your code if there's no result
			}
		} else if (requestCode == Info.RESULT_SELECTED_IMAGE)
		{
			if (resultCode == -1)
			{
				if (data != null)
				{
					Uri uri = data.getData();
					try
					{
						// Create a file instance from the URI
						String url = Info.getInstance().getRealPathFromURI(
								getActivity(), uri);
						File file = new File(url);
						pathImgSelected = url;
						imgName = file.getName();
						
						Bitmap bitmapSelected = Info.decodeFile(file, 128);
						addImgImgBtn.setImageBitmap(bitmapSelected);
					} catch (Exception e)
					{
					}
				}
			}
			
		}
	}// onActivityResult
	
	@Override
	public boolean onMarkerClick(Marker marker)
	{
		return false;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker)
	{
		dialog.dismiss();
	}
	
	@Override
	public void onMapClick(LatLng point)
	{
		mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
		dialog.dismiss();
	}
	
	private class UploadImage extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... uri)
		{
			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;
			
			String urlServer = uri[0];// "http://203.185.131.171/CrimeMap/Json/upload.php";
			String pathToOurFile = uri[1];// selectedPath;
			
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			String responseString = "";
			try
			{
				FileInputStream fileInputStream = new FileInputStream(new File(
						pathToOurFile));
				
				URL url = new URL(urlServer);
				connection = (HttpURLConnection) url.openConnection();
				
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				
				connection.setRequestMethod("POST");
				
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				
				outputStream = new DataOutputStream(
						connection.getOutputStream());
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream
						.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
								+ pathToOurFile + "\"" + lineEnd);
				outputStream.writeBytes(lineEnd);
				
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				
				while (bytesRead > 0)
				{
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens
						+ lineEnd);
				
				int serverResponseCode = connection.getResponseCode();
				String serverResponseMessage = connection.getResponseMessage();
				
				responseString = serverResponseMessage + "";
				
				fileInputStream.close();
				outputStream.flush();
				outputStream.close();
				
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			return responseString;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			
			Toast.makeText(getActivity(),result,
					Toast.LENGTH_LONG).show();
			
			/*
			Toast.makeText(getActivity(), getString(R.string.sending_data_complete_text),
					Toast.LENGTH_LONG).show();
			*/
			
		}
	}
	
}
