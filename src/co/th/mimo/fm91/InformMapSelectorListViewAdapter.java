package co.th.mimo.fm91;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InformMapSelectorListViewAdapter extends BaseAdapter {
	private String tag = getClass().getSimpleName();
	private Context context;
	private ArrayList<Nearby> nearbyList;
	private LayoutInflater inflater;

	public InformMapSelectorListViewAdapter(Context context, ArrayList<Nearby> newsList) {
		super();
		this.context = context;
		this.nearbyList = newsList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null)
			convertView = inflater.inflate(R.layout.inform_listview_selector,null);
		
		TextView tvTitle = (TextView) convertView.findViewById(R.id.placeDetail);
		tvTitle.setText(nearbyList.get(position).title+"\n");

		TextView tvHowFar = (TextView) convertView.findViewById(R.id.placeHowFar);
		tvHowFar.setText(this.context.getString(R.string.farfromyou_msg)+" "+nearbyList.get(position).howFar());
		return convertView;
	}

	@Override
	public int getCount() {
		return nearbyList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.nearbyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}