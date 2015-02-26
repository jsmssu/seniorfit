package name.sunme.calendar;

import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.maindrawbar.R;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarCustomAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> data;
	private Handler mHandler;

	public CalendarCustomAdapter(Context context,
			ArrayList<HashMap<String, String>> data, Handler mHandler) {
		this.context = context;
		this.data = data;
		this.mHandler = mHandler;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView = convertView;

		//gridView = new View(context);

		gridView = inflater.inflate(R.layout.calcell_content, parent, false);

		TextView celltitme = (TextView) gridView
				.findViewById(R.id.calcell_title);
		if (data.get(position).get("title") != null) {
			celltitme.setText(data.get(position).get("title"));
		}  
		TextView cellday = (TextView) gridView.findViewById(R.id.calcell_day);
		if (data.get(position).get("day") != null) {
			cellday.setText(data.get(position).get("day"));
		} 
		ImageView wk = (ImageView) gridView.findViewById(R.id.wk_check);
		if (data.get(position).get("wkc") != null) {
			wk.setVisibility(View.VISIBLE);
		}  
		ImageView st = (ImageView) gridView.findViewById(R.id.st_check);
		if (data.get(position).get("stc") != null) {
			st.setVisibility(View.VISIBLE);
		}  
		gridView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mHandler != null)
					mHandler.sendEmptyMessage(position);
			}
		});

		return gridView;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}