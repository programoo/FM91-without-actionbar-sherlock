package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EmergencyCallActivity extends Activity implements
		OnItemClickListener, TextWatcher
{
	private String TAG = this.getClass().getSimpleName();
	private ArrayList<Contact> contactList;
	private EditText searchEmergencyEdt;
	private ListView lv;
	private String phoneNum;
	private String nameTel;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergencycall_fragment_activity);
		
		lv = (ListView) findViewById(R.id.emergencyLv);
		searchEmergencyEdt = (EditText) findViewById(R.id.searchEmergencyEdt);
		
		contactList = new ArrayList<Contact>();
		contactList.add(new Contact("1555", "home", "undefined"));
		contactList.add(new Contact("15666", "fan", "undefined"));
		
		EmergencyCallListViewAdapter adapter = new EmergencyCallListViewAdapter(
				this, contactList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		searchEmergencyEdt.addTextChangedListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		TextView phoneTv = (TextView) arg1.findViewById(R.id.phoneEmergencyTv);
		phoneNum = phoneTv.getText().toString();
		
		TextView nameTv = (TextView) arg1.findViewById(R.id.nameEmergencyTv);
		nameTel = nameTv.getText().toString();
		
		
		Log.i(TAG, phoneNum);
		
		// redirect to user profile page
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage(nameTel);
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + phoneNum));
				startActivity(intent);
				
				dialog.cancel();
				
			}
		});
		builder1.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		
		AlertDialog alert11 = builder1.create();
		alert11.show();
		
	}
	
	@Override
	public void afterTextChanged(Editable s)
	{
		ArrayList<Contact> passFilter = new ArrayList<Contact>();
		
		for (int i = 0; i < this.contactList.size(); i++)
		{
			if (this.contactList.get(i).toString()
					.indexOf(this.searchEmergencyEdt.getText().toString()) != -1)
			{
				passFilter.add(this.contactList.get(i));
			}
		}
		
		EmergencyCallListViewAdapter adapter = new EmergencyCallListViewAdapter(
				this, passFilter);
		lv.setAdapter(adapter);
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
	}
	
	public void emergencyBtnOnClick(View view)
	{
		/*
		 * Log.d(TAG, "emergencyBtnOnClick"); Intent shareBtn = new
		 * Intent(EmergencyCallActivity.this, EmergencyCallActivity.class);
		 * startActivity(shareBtn);
		 */
	}
	
}