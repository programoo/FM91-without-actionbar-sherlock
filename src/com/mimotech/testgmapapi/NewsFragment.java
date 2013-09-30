package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFragment extends Fragment implements OnItemClickListener,
		OnClickListener
{
	private String tag = this.getClass().getSimpleName();
	private View viewMainFragment;
	private ListView lv;
	private ArrayList<News> newsList;
	private Button newsBtn;
	private Button eventBtn;
	private DateTimeFormatter formatter;
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		
		locationListener = new MyLocationListener();
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		newsList = new ArrayList<News>();
		
		this.viewMainFragment = inflater.inflate(R.layout.news_fragment,
				container, false);
		
		lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);
		
		NewsListViewAdapter ardap = new NewsListViewAdapter(getActivity(),
				newsList);
		
		lv.setAdapter(ardap);
		
		Log.d(tag, "onCreateView");
		
		lv.setOnItemClickListener(this);
		
		newsBtn = (Button) viewMainFragment.findViewById(R.id.newsBtn);
		newsBtn.setOnClickListener(this);
		eventBtn = (Button) viewMainFragment.findViewById(R.id.eventBtn);
		eventBtn.setOnClickListener(this);
		
		// set default sub-menu color
		newsBtn.setTextColor(Color.parseColor("#8dc342"));
		eventBtn.setTextColor(Color.parseColor("#808080"));
		
		return viewMainFragment;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		Log.d(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
		new RequestTask("getRandomStr")
				.execute("http://api.traffy.in.th/apis/getKey.php?appid=abcb6710");
		
	}
	
	public void onStart()
	{
		super.onStart();
		Log.d(tag, "onStart");
		
		// update from old memory
		readNews();
		writeNews();
		reloadViewAfterRequestTaskComplete();
		
		// update already read list
		lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);
		NewsListViewAdapter ardap = new NewsListViewAdapter(getActivity(),
				newsList);
		lv.setAdapter(ardap);
		
		// update badge count unRead
		TextView tvBadgeCount = (TextView) getActivity().findViewById(
				R.id.badge_count);
		tvBadgeCount.setText(this.unReadNumber() + "");
		
		// get current location by gps
		Log.d(tag, "Request location");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);
		
	}
	
	public void sortNewsList()
	{
		for (int i = 0; i < this.newsList.size(); i++)
		{
			for (int j = 0; j < this.newsList.size() - 1; j++)
			{
				News jA = this.newsList.get(j);
				News jB = this.newsList.get(j + 1);
				if (jB.unixTime > jA.unixTime)
				{
					this.newsList.set(j + 1, jA);
					this.newsList.set(j, jB);
				}
			}
		}
		
	}
	
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreated");
		
	}
	
	public void reloadViewAfterRequestTaskComplete()
	{
		Log.d(tag, "reloadViewAfterRequestTaskComplete");
		try
		{
			this.sortNewsList();
			NewsListViewAdapter ardap = new NewsListViewAdapter(getActivity(),
					newsList);
			lv.setAdapter(ardap);
			
			TextView tvBadgeCount = (TextView) getActivity().findViewById(
					R.id.badge_count);
			// if zero hide it
			if (this.unReadNumber() == 0)
			{
				tvBadgeCount.setVisibility(View.GONE);
			} else
			{
				tvBadgeCount.setVisibility(View.VISIBLE);
			}
			tvBadgeCount.setText(this.unReadNumber() + "");
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void writeNews()
	{
		BufferedWriter bufferedWriter;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					getActivity().getFilesDir() + File.separator + "news.csv")));
			for (int i = 0; i < this.newsList.size(); i++)
			{
				bufferedWriter.write(this.newsList.get(i).toString() + "\n");
			}
			bufferedWriter.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		// sort before writh
		writeNews();
	}
	
	public void readNews()
	{
		BufferedReader bufferedReader;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getActivity().getFilesDir() + File.separator + "news.csv")));
			String read = "";
			
			while ((read = bufferedReader.readLine()) != null)
			{
				// Log.i(tag, read);
				String tmpNews[] = read.split(",");
				String startTime = tmpNews[4];
				
				News n = new News(
						tmpNews[0],
						tmpNews[1],
						tmpNews[2],
						tmpNews[3],
						tmpNews[4],
						tmpNews[5],
						tmpNews[6],
						tmpNews[7],
						tmpNews[8],
						tmpNews[9],
						tmpNews[10],
						tmpNews[11],
						tmpNews[12],
						tmpNews[13],
						tmpNews[14],
						tmpNews[15],
						tmpNews[16],
						tmpNews[17],
						Boolean.parseBoolean(tmpNews[18]),
						getHumanLanguageTime(formatter.parseDateTime(startTime)),
						formatter.parseDateTime(startTime).getMillis());
				this.uniqueAdd(n);
			}
			bufferedReader.close();
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		this.reloadViewAfterRequestTaskComplete();
	}
	
	public int unReadNumber()
	{
		int count = newsList.size();
		for (int i = 0; i < newsList.size(); i++)
		{
			if (newsList.get(i).isRead == true)
			{
				--count;
			}
		}
		return count;
	}
	
	public News getNews(String newsId)
	{
		for (int i = 0; i < newsList.size(); i++)
		{
			if (newsList.get(i).id.equalsIgnoreCase(newsId))
			{
				return newsList.get(i);
			}
		}
		return null;
	}
	
	public void uniqueAdd(News news)
	{
		
		for (int i = 0; i < newsList.size(); i++)
		{
			if (news.id.equalsIgnoreCase(newsList.get(i).id))
			{
				return;
			}
		}
		newsList.add(0, news);
		// after add sort it
		
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
			
			Log.i(tag, "your current location:" + latitude + "," + longitude);
			
			Info.lat = loc.getLatitude();
			Info.lng = loc.getLongitude();
			
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{
		}
		
		@Override
		public void onProviderEnabled(String provider)
		{
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
	}
	
	private class RequestTask extends AsyncTask<String, String, String>
	{
		private String tag = getClass().getSimpleName();
		public String AppID = "abcb6710";
		public String hiddenkey = "TxLYP6j1Ee";
		public String randomStr = "undefined";
		public String requestType = "";
		private String passKey = "";
		
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
				// TODO Handle problems..
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Handle problems..
				e.printStackTrace();
			}
			return responseString;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			Log.d(this.getClass().getSimpleName(), "onPostExecute");
			
			if (requestType.equalsIgnoreCase("getRandomStr"))
			{
				randomStr = result;
				try
				{
					passKey = convertToMD5(AppID + randomStr)
							+ convertToMD5(hiddenkey + randomStr);
				} catch (NoSuchAlgorithmException e)
				{
					e.printStackTrace();
				}
				/*
				 * http://api.traffy.in.th/apis/apitraffy.php?api=getIncident&key
				 * =(คีย์ที่ได้รับจากการลงทะเบียน)&appid=(id
				 * ที่ได้รับจากการลงทะเบียน
				 * )&format=XML&limit=10&offset=5&type=all&from=2011-11-05
				 * 17:41:13&to=2011-11-05 17:45:20
				 */
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
						Locale.getDefault());
				Date date = new Date();
				String dateStart = dateFormat.format(date) + "%2000:00:01";
				String dateEnd = dateFormat.format(date) + "%2023:59:59";
				
				// System.out.println(dateStart);
				
				// System.out.println(dateEnd);
				
				String traffy_request_url = "http://api.traffy.in.th/apis/apitraffy.php?api=getIncident&key="
						+ passKey
						+ "&format=XML&limit=10&offset=5&type=all&from="
						+ dateStart + "&to=" + dateEnd;
				new RequestTask("getData").execute(traffy_request_url);
			} else if (requestType.equalsIgnoreCase("getData"))
			{
				// this mean we get real data from traffy already
				readNews();
				this.traffyNewsXmlParser(result);
				writeNews();
				reloadViewAfterRequestTaskComplete();
				
				try
				{
					if (result == null)
					{
						new AlertDialog.Builder(getActivity())
								.setMessage(
										getString(R.string.internet_disconnect_alert))
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int which)
											{
												// continue with delete
											}
										}).show();
					} else
					{
						
						Toast.makeText(getActivity().getBaseContext(),
								R.string.internet_connect_alert,
								Toast.LENGTH_LONG).show();
					}
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
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
		
		public void traffyNewsXmlParser(String xmlString)
		{
			if (xmlString == null)
			{
				Log.e(tag, "traffyNewsXmlParser: nullString");
				return;
			}
			
			Document doc = null;
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(xmlString));
				doc = builder.parse(is);
				
			} catch (ParserConfigurationException e)
			{
				e.printStackTrace();
			} catch (SAXException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			NodeList nList = doc.getElementsByTagName("news");
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					
					Element eElement = (Element) nNode;
					String id = eElement.getAttribute("id");
					String type = eElement.getAttribute("type");
					String primarySource = eElement
							.getAttribute("primarysource");
					String secondarySource = eElement
							.getAttribute("secondarysource");
					String startTime = eElement.getAttribute("starttime");
					String endTime = eElement.getAttribute("endtime");
					
					String mediaType = eElement.getElementsByTagName("media")
							.item(0).getAttributes().getNamedItem("type")
							.getNodeValue();
					String mediaPath = eElement.getElementsByTagName("media")
							.item(0).getAttributes().getNamedItem("path")
							.getNodeValue();
					
					String title = eElement.getElementsByTagName("title")
							.item(0).getTextContent();
					String description = eElement
							.getElementsByTagName("description").item(0)
							.getTextContent();
					
					String locationType = eElement
							.getElementsByTagName("location").item(0)
							.getAttributes().getNamedItem("type")
							.getNodeValue();
					
					String roadName = getStringValueFromExistElement(eElement,
							"road", "name");
					String startPointName = getStringValueFromExistElement(
							eElement, "startpoint", "name");
					String startPointLat = getStringValueFromExistElement(
							eElement, "startpoint", "latitude");
					String startPointLong = getStringValueFromExistElement(
							eElement, "startpoint", "longitude");
					
					String endPointName = getStringValueFromExistElement(
							eElement, "endpoint", "name");
					String endPointLat = getStringValueFromExistElement(
							eElement, "endpoint", "latitude");
					String endPointLong = getStringValueFromExistElement(
							eElement, "endpoint", "longitude");
					
					// reformat to unix time
					News n = new News(id, type, primarySource, secondarySource,
							startTime, endTime, mediaType, mediaPath, title,
							description, locationType, roadName,
							startPointName, startPointLat, startPointLong,
							endPointName, endPointLat, endPointLong, false,
							getHumanLanguageTime(formatter
									.parseDateTime(startTime)), formatter
									.parseDateTime(startTime).getMillis());
					uniqueAdd(n);
					
				}
			}
			
		}// end xml parser
		
		public String getStringValueFromExistElement(Element eElement,
				String elementName, String attributeName)
		{
			try
			{
				String valueString = eElement.getElementsByTagName(elementName)
						.item(0).getAttributes().getNamedItem(attributeName)
						.getNodeValue();
				return valueString;
			} catch (NullPointerException e)
			{
				/*
				 * Log.d(tag, "element not found: " + elementName + "," +
				 * attributeName);
				 */
				return "undefined";
			}
			
		}
		
	}
	
	public String getHumanLanguageTime(DateTime newsTime)
	{
		String alreadyPassTime = "undefined";
		// joda time convert
		DateTime currentTime = new DateTime();
		Duration dur = new Duration(newsTime, currentTime);
		
		if (dur.getStandardDays() > 0)
		{
			alreadyPassTime = dur.getStandardDays() + " "
					+ getString(R.string.pass_day_text);
		} else if (dur.getStandardHours() > 0)
		{
			alreadyPassTime = dur.getStandardHours() + " "
					+ getString(R.string.pass_hour_text);
		} else if (dur.getStandardMinutes() > 0)
		{
			alreadyPassTime = dur.getStandardMinutes() + " "
					+ getString(R.string.pass_minute_text);
		} else
		{
			alreadyPassTime = getString(R.string.pass_second_text);
		}
		
		return alreadyPassTime;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		Log.d(tag, "item click: " + arg1 + "," + arg2);
		// mark as read
		News n = getNews(arg3 + "");
		n.isRead = true;
		
		Intent mapActivity = new Intent(getActivity(),
				NewsDetailsActivity.class);
		
		mapActivity.putExtra("description", n.description);
		mapActivity.putExtra("startPointLong", n.startPointLong);
		mapActivity.putExtra("startPointLat", n.startPointLat);
		mapActivity.putExtra("title", n.title);
		
		// myMarker(n);
		
		startActivity(mapActivity);
		
	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		Log.d(tag, "btn id" + v.getId());
		switch (v.getId())
		{
			case R.id.newsBtn:
				Log.d(tag, "news btn click");
				newsBtn.setTextColor(Color.parseColor("#8dc342"));
				eventBtn.setTextColor(Color.parseColor("#808080"));
				
				break;
			case R.id.eventBtn:
				Log.d(tag, "eventBtn btn click");
				newsBtn.setTextColor(Color.parseColor("#808080"));
				eventBtn.setTextColor(Color.parseColor("#8dc342"));
				break;
		}
	}
	
}
