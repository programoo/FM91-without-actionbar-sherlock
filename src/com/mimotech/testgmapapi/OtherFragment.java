package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class OtherFragment extends Fragment implements OnItemClickListener
{
	private String TAG = this.getClass().getSimpleName();
	private View v;
	private ArrayList<String> strList;
	private ListView lv;
	// configuration variable
	private View viewSelected;
	private boolean crimTick;
	private boolean accidentTick;
	private boolean otherTick;
	private String latLnConfig;
	private String radius;
	private String rewind;
	
	private ImageButton crimTg;
	private ImageButton accidentTg;
	private ImageButton otherTg;
	private Dialog radiusSettingDialog;
	
	private ListView lvSettingDialog;
	private RadioButton oneKm;
	private RadioButton threeKm;
	private RadioButton fiveKm;
	private Dialog settingDialog;
	private Context ctx;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		latLnConfig = "0 0";
		crimTick = true;
		accidentTick = true;
		otherTick = true;
		radius = "1";
		rewind = "1";
		ctx = getActivity();
		strList = new ArrayList<String>();
		
		strList.add("Profile");
		strList.add("facebook");
		strList.add("twitter");
		strList.add("Setting");
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.v = inflater.inflate(R.layout.other_fragment, container, false);
		
		lv = (ListView) this.v.findViewById(R.id.otherLv);
		Log.i(TAG, "" + strList.size());
		OtherListViewAdapter adapter = new OtherListViewAdapter(getActivity(),
				strList, R.layout.other_fragment_listview);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(this);
		
		Log.i(TAG, "setting listview complete");
		
		return this.v;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// LEVEL 1 settings
		String dialogName = (String) this.lv.getItemAtPosition(arg2);
		Log.i(TAG, "dialogName: " + dialogName);
		if (dialogName.equalsIgnoreCase("profile"))
		{
			Intent it = new Intent(getActivity(), InsertProfileActivity.class);
			startActivity(it);
		} else if (dialogName.equalsIgnoreCase("facebook"))
		{
			Intent it = new Intent(getActivity(), WebViewActivity.class);
			it.putExtra("provider", "https://www.facebook.com/trafficradiofm91");
			startActivity(it);
		} else if (dialogName.equalsIgnoreCase("twitter"))
		{
			Intent it = new Intent(getActivity(), WebViewActivity.class);
			it.putExtra("provider", "https://twitter.com/fm91trafficpro");
			startActivity(it);
		} else if (dialogName.equalsIgnoreCase("setting"))
		{
			settingDialog = new Dialog(getActivity(),
					android.R.style.Theme_Light);
			settingDialog.getWindow();
			settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			settingDialog.setContentView(R.layout.setting_dialog);
			settingDialog.setCancelable(true);
			settingDialog.show();
			
			// SET DEFALUT VALUE BY READ CONFIG
			String settingCsv = Info.getInstance().readProfiles(getActivity(),
					"settings.csv");
			if (!settingCsv.equalsIgnoreCase("undefined"))
			{
				Log.i(TAG, settingCsv);
				
				this.crimTick = Boolean.parseBoolean(settingCsv.split(",")[0]);
				this.accidentTick = Boolean
						.parseBoolean(settingCsv.split(",")[1]);
				this.otherTick = Boolean.parseBoolean(settingCsv.split(",")[2]);
				this.latLnConfig = settingCsv.split(",")[3];
				this.radius = settingCsv.split(",")[4];
				this.rewind = settingCsv.split(",")[5];
			}
			
			ImageButton emergencyBtn = (ImageButton) settingDialog
					.findViewById(R.id.emergency_btn);
			emergencyBtn.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Log.d(TAG, "emergencyBtnOnClick");
					Intent shareBtn = new Intent(getActivity(),
							EmergencyCallActivity.class);
					startActivity(shareBtn);
				}
				
			});
			
			lvSettingDialog = (ListView) settingDialog
					.findViewById(R.id.settingDialogLv);
			
			ArrayList<String> strSettingList = new ArrayList<String>();
			strSettingList.add(getString(R.string.place_setting_text) + ","
					+ latLnConfig);
			strSettingList.add(getString(R.string.radious_setting_text) + ","
					+ radius + " " + getString(R.string.kilometer_text));
			strSettingList.add(getString(R.string.rewind_setting_text) + ","
					+ rewind + " " + getString(R.string.day_text));
			
			OtherListViewAdapter settingsAdapter = new OtherListViewAdapter(
					getActivity(), strSettingList,
					R.layout.other_fragment_settings_listview);
			lvSettingDialog.setAdapter(settingsAdapter);
			
			crimTg = (ImageButton) settingDialog.findViewById(R.id.crimeTgBtn);
			
			crimTg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (crimTick)
					{
						crimTg.setImageResource(R.drawable.inactive_btn);
						crimTick = false;
					} else
					{
						crimTg.setImageResource(R.drawable.active_btn);
						crimTick = true;
					}
					
				}
			});
			
			accidentTg = (ImageButton) settingDialog
					.findViewById(R.id.accidentTgBtn);
			accidentTg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (accidentTick)
					{
						accidentTg.setImageResource(R.drawable.inactive_btn);
						accidentTick = false;
					} else
					{
						accidentTg.setImageResource(R.drawable.active_btn);
						accidentTick = true;
					}
					
				}
			});
			
			otherTg = (ImageButton) settingDialog.findViewById(R.id.otherTgBtn);
			otherTg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (otherTick)
					{
						otherTg.setImageResource(R.drawable.inactive_btn);
						otherTick = false;
					} else
					{
						otherTg.setImageResource(R.drawable.active_btn);
						otherTick = true;
					}
				}
			});
			// crimTg.setChecked(this.crimTick);
			if (crimTick)
			{
				crimTg.setImageResource(R.drawable.active_btn);
				crimTick = true;
			}
			
			if (accidentTick)
			{
				accidentTg.setImageResource(R.drawable.active_btn);
				accidentTick = true;
			}
			
			if (otherTick)
			{
				otherTg.setImageResource(R.drawable.active_btn);
				otherTick = true;
			}
			
			// accidentTg.setChecked(this.accidentTick);
			// otherTg.setChecked(this.otherTick);
			
			Log.i(TAG, "is check: " + this.crimTick);
			Log.i(TAG, "is check: " + this.accidentTick);
			Log.i(TAG, "is check: " + this.otherTick);
			
			settingDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialog)
				{
					// crimTick = crimTick;
					// accidentTick = accidentTg.isChecked();
					// otherTick = otherTg.isChecked();
					
					Log.i(TAG, "is check: " + crimTick);
					Log.i(TAG, "is check: " + accidentTick);
					Log.i(TAG, "is check: " + otherTick);
					
					// kep all settings value here
					String writeStrCsv = "";
					writeStrCsv = crimTick + "," + accidentTick + ","
							+ otherTick + "," + latLnConfig + "," + radius
							+ "," + rewind;
					// attatch this to global variable
					Info.crimTick = crimTick;
					Info.accidentTick = accidentTick;
					Info.otherTick = otherTick;
					Info.getInstance().latLnConfig = latLnConfig;
					Info.getInstance().radius = radius;
					Info.getInstance().rewind = rewind;
					
					writeSettings(writeStrCsv);
				}
			});
			// LEVEL 2 LISTVEIW
			lvSettingDialog.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					// show select view
					viewSelected = view;
					String setType = ((String) lvSettingDialog
							.getItemAtPosition(position)).split(",")[0];
					
					if (setType
							.equalsIgnoreCase(getString(R.string.place_setting_text)))
					{
						Intent i = new Intent(getActivity(),
								InformMapSelectorActivity.class);
						startActivityForResult(i, Info.RESULT_SELECTED_POSITION);
					} else if (setType
							.equalsIgnoreCase(getString(R.string.rewind_setting_text)))
					{
						Log.i(TAG, "rewind text setting click");
						
						radiusSettingDialog = new Dialog(getActivity());
						radiusSettingDialog.getWindow();
						radiusSettingDialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						radiusSettingDialog
								.setContentView(R.layout.rewind_setting_dialog);
						radiusSettingDialog.setCancelable(true);
						radiusSettingDialog.show();
						
						oneKm = (RadioButton) radiusSettingDialog
								.findViewById(R.id.oneDayRadio);
						threeKm = (RadioButton) radiusSettingDialog
								.findViewById(R.id.threeDayRadio);
						fiveKm = (RadioButton) radiusSettingDialog
								.findViewById(R.id.sevenDayRadio);
						// retrieve rewind old state
						if (rewind.equals("1"))
						{
							oneKm.setChecked(true);
						} else if (rewind.equals("3"))
						{
							threeKm.setChecked(true);
						} else if (rewind.equals("7"))
						{
							fiveKm.setChecked(true);
						}
						
						ImageButton okRadiusBtn = (ImageButton) radiusSettingDialog
								.findViewById(R.id.okRadiusBtn);
						ImageButton cancelRadiusBtn = (ImageButton) radiusSettingDialog
								.findViewById(R.id.cancelRadiusBtn);
						
						okRadiusBtn.setOnClickListener(new OnClickListener()
						{
							
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, "select radius complete");
								// keep selected rewind state
								if (oneKm.isChecked())
								{
									rewind = 1 + "";
								} else if (threeKm.isChecked())
								{
									rewind = 3 + "";
									
								} else if (fiveKm.isChecked())
								{
									rewind = 7 + "";
								}
								TextView radiusShowAfterSettingsTv = (TextView) viewSelected
										.findViewById(R.id.otherSelectedDataTv);
								radiusShowAfterSettingsTv.setText(rewind + " "
										+ getString(R.string.day_text));
								radiusSettingDialog.dismiss();
							}
						});
						
						cancelRadiusBtn
								.setOnClickListener(new OnClickListener()
								{
									
									@Override
									public void onClick(View v)
									{
										radiusSettingDialog.dismiss();
									}
								});
						
						oneKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, oneKm.isChecked() + "");
								oneKm.setChecked(true);
								threeKm.setChecked(false);
								fiveKm.setChecked(false);
								// rewind = 1 + "";
							}
						});
						
						threeKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, threeKm.isChecked() + "");
								oneKm.setChecked(false);
								threeKm.setChecked(true);
								fiveKm.setChecked(false);
								// rewind = 3 + "";
								
							}
						});
						fiveKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, fiveKm.isChecked() + "");
								oneKm.setChecked(false);
								threeKm.setChecked(false);
								fiveKm.setChecked(true);
								// rewind = 7 + "";
								
							}
						});
						
					} else if (setType
							.equalsIgnoreCase(getString(R.string.radious_setting_text)))
					{
						Log.i(TAG, "radius text setting click");
						
						radiusSettingDialog = new Dialog(getActivity());
						radiusSettingDialog.getWindow();
						radiusSettingDialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						radiusSettingDialog
								.setContentView(R.layout.radious_setting_dialog);
						radiusSettingDialog.setCancelable(true);
						
						android.view.WindowManager.LayoutParams lp = radiusSettingDialog
								.getWindow().getAttributes();
						//lp.x = 100;
						//lp.y = 100;
						lp.width = LayoutParams.WRAP_CONTENT;
						lp.height = LayoutParams.WRAP_CONTENT;
						lp.gravity = Gravity.CENTER;
						//lp.dimAmount = 0;
						//lp.flags = LayoutParams.WRAP_CONTENT
						//		| LayoutParams.WRAP_CONTENT;
						//
						radiusSettingDialog.show();
						
						oneKm = (RadioButton) radiusSettingDialog
								.findViewById(R.id.oneKilometerRadio);
						threeKm = (RadioButton) radiusSettingDialog
								.findViewById(R.id.threeKilometerRadio);
						fiveKm = (RadioButton) radiusSettingDialog
								.findViewById(R.id.fiveKilometerRadio);
						
						ImageButton okRadiusBtn = (ImageButton) radiusSettingDialog
								.findViewById(R.id.okRadiusBtn);
						ImageButton cancelRadiusBtn = (ImageButton) radiusSettingDialog
								.findViewById(R.id.cancelRadiusBtn);
						
						// retrieve old radius state
						if (radius.equals("1"))
						{
							oneKm.setChecked(true);
						} else if (radius.equals("3"))
						{
							threeKm.setChecked(true);
						} else if (radius.equals("5"))
						{
							fiveKm.setChecked(true);
						}
						
						okRadiusBtn.setOnClickListener(new OnClickListener()
						{
							
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, "select radius complete");
								
								if (oneKm.isChecked())
								{
									radius = 1 + "";
								} else if (threeKm.isChecked())
								{
									radius = 3 + "";
									
								} else if (fiveKm.isChecked())
								{
									radius = 5 + "";
								}
								
								TextView radiusShowAfterSettingsTv = (TextView) viewSelected
										.findViewById(R.id.otherSelectedDataTv);
								radiusShowAfterSettingsTv.setText(radius + " "
										+ getString(R.string.kilometer_text));
								radiusSettingDialog.dismiss();
							}
						});
						
						cancelRadiusBtn
								.setOnClickListener(new OnClickListener()
								{
									
									@Override
									public void onClick(View v)
									{
										radiusSettingDialog.dismiss();
									}
								});
						
						oneKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, oneKm.isChecked() + "");
								oneKm.setChecked(true);
								threeKm.setChecked(false);
								fiveKm.setChecked(false);
								// radius = 1 + "";
							}
						});
						
						threeKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, threeKm.isChecked() + "");
								oneKm.setChecked(false);
								threeKm.setChecked(true);
								fiveKm.setChecked(false);
								// radius = 3 + "";
								
							}
						});
						fiveKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG, fiveKm.isChecked() + "");
								oneKm.setChecked(false);
								threeKm.setChecked(false);
								fiveKm.setChecked(true);
								// radius = 5 + "";
								
							}
						});
						
					} else
					{
						Log.e(TAG, "invalid listvew name: " + setType);
					}
					
				}
			});
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Info.RESULT_SELECTED_POSITION)
		{
			if (resultCode == Info.RESULT_OK)
			{
				String result = data.getStringExtra("result");
				Log.i(TAG, "result from selector" + result);
				latLnConfig = result;
				
				latLnConfig = latLnConfig.replaceAll(",", " ");
				
				TextView tv = (TextView) this.viewSelected
						.findViewById(R.id.otherSelectedDataTv);
				tv.setText(latLnConfig);
			}
			if (resultCode == Info.RESULT_CANCELED)
			{
				Log.e(TAG, "result onActivityResult error");
			}
		}
	}// onActivityResult
	
	public void writeSettings(String strCsv)
	{
		BufferedWriter bufferedWriter;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					getActivity().getFilesDir() + File.separator
							+ "settings.csv")));
			bufferedWriter.write(strCsv);
			bufferedWriter.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public String readSettings()
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getActivity().getFilesDir() + File.separator
							+ "settings.csv")));
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
