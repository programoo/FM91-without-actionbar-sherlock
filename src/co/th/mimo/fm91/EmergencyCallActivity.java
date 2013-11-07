package co.th.mimo.fm91;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
		
		ImageButton emerImgBtn = (ImageButton) findViewById(R.id.emergency_btn);
		emerImgBtn.setVisibility(View.GONE);
		
		contactList = new ArrayList<Contact>();
		//contactList.add(new Contact("15666", "fan", "undefined"));
		//contactList.add(new Contact(eR[0].split(",")[0], "fan", "undefined"));

		String[] eR = getResources().getStringArray(
				R.array.emergencyNumberArr);
		
		for (int i = 0; i < eR.length; i++)
		{
			contactList.add(new Contact(eR[i].split(",")[0], eR[i].split(",")[1], null));
		}
		
		contactList.get(0).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1644);
		contactList.get(1).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1100);
		contactList.get(2).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1103);
		contactList.get(3).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1113);
		contactList.get(4).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1123);
		contactList.get(5).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1125);
		contactList.get(6).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1129);
		contactList.get(7).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1130);
		contactList.get(8).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1135);
		contactList.get(9).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1138);
		contactList.get(10).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1146);
		contactList.get(11).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1155);
		contactList.get(12).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1165);
		contactList.get(13).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1166);
		contactList.get(14).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1178);
		contactList.get(15).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1190);
		contactList.get(16).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1193);
		contactList.get(17).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1197);
		contactList.get(18).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1199);
		contactList.get(19).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1200);
		contactList.get(20).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1322);
		contactList.get(21).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1356);
		contactList.get(22).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1543);
		contactList.get(23).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1554);
		contactList.get(24).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1556);
		contactList.get(25).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1586);
		contactList.get(26).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1599);
		contactList.get(27).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1646);
		contactList.get(28).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1650);
		contactList.get(29).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1661);
		contactList.get(30).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1669);
		contactList.get(31).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1672);
		contactList.get(32).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1677);
		contactList.get(33).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1690);
		contactList.get(34).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1691);
		contactList.get(35).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1696);
		contactList.get(36).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1860);
		contactList.get(37).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e1899);
		contactList.get(38).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e191);
		contactList.get(39).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e199);
		contactList.get(40).imgUrl = BitmapFactory.decodeResource(getResources(),
                R.drawable.e0222264444);
		
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