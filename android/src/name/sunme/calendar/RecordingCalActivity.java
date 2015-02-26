package name.sunme.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.Inflater;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.maindrawbar.R.id;
import name.sunme.maindrawbar.R.layout;
import name.sunme.timer.StretcingTimer;
import name.sunme.timer.WalkingTimer;
import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordingCalActivity extends Activity {
	String TAG = "RecordingCalActivity";

	ArrayList<HashMap<String, String>> data;

	GridView recordingcal_grid;
	TextView recordingcal_stretching_min;
	TextView recordingcal_walking_min;
	TextView recordingcal_date;
	TextView recordingcal_title;
	CalendarCustomAdapter calAdapter;

	public SimpleDateFormat dateformat = new SimpleDateFormat(
			"yyyy년 MM월 dd일");
	public SimpleDateFormat titleformat = new SimpleDateFormat(
			"yyyy년 MM월");

	WalkingTimer wkt;
	StretcingTimer stt;

	Calendar calday;

	ImageView calendar_btn_left;
	ImageView calendar_btn_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recording_cal);

		wkt = new WalkingTimer(getApplicationContext());
		stt = new StretcingTimer(getApplicationContext());

		recordingcal_grid = (GridView) findViewById(R.id.recordingcal_grid);
		recordingcal_stretching_min = (TextView) findViewById(R.id.recordingcal_stretching_min);
		recordingcal_walking_min = (TextView) findViewById(R.id.recordingcal_walking_min);
		recordingcal_date = (TextView) findViewById(R.id.recordingcal_date);
		recordingcal_title = (TextView) findViewById(R.id.recordingcal_title);
		calendar_btn_right = (ImageView) findViewById(R.id.calendar_btn_right);
		calendar_btn_left = (ImageView) findViewById(R.id.calendar_btn_left);

		data = new ArrayList<HashMap<String, String>>();
		calAdapter = new CalendarCustomAdapter(this, data, textHandler);
		recordingcal_grid.setAdapter(calAdapter);

		calday = Calendar.getInstance();

		setCalendar(calday);

		// 처음 설정
		recordingcal_title.setText(titleformat.format(calday.getTime()));
		int todayofmonth = calday.get(Calendar.DAY_OF_MONTH) - 1;
		calday.set(Calendar.DAY_OF_MONTH, 1);
		int firstdayofmonth = calday.get(Calendar.DAY_OF_MONTH) - 1;
		Log.d(TAG, firstdayofmonth + "");
		Log.d(TAG, todayofmonth + "");
		textHandler.sendEmptyMessage(firstdayofmonth + todayofmonth + 7);
		//
		calendar_btn_left.setOnClickListener(left_click);
		calendar_btn_right.setOnClickListener(right_click);
	}

	OnClickListener left_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			calday.set(Calendar.MONTH, calday.get(Calendar.MONTH) - 1);
			setCalendar(calday);
		}
	};
	void setCalendar(Calendar cal) {
		recordingcal_title.setText(titleformat.format(cal.getTime()));
		setData(calday);
		calAdapter.notifyDataSetInvalidated();
		calAdapter.notifyDataSetChanged();
		
	}
	OnClickListener right_click = new OnClickListener() {

		@Override
		public void onClick(View v) { 
			calday.set(Calendar.MONTH, calday.get(Calendar.MONTH) + 1);
			setCalendar(calday);
		}
	};

	void setData(Calendar selectedCal) {

		data.clear();
		{
			data.add(new HashMap<String, String>() {
				{
					put("title", "일");
				}
			});
			data.add(new HashMap<String, String>() {
				{
					put("title", "월");
				}
			});
			data.add(new HashMap<String, String>() {
				{
					put("title", "화");
				}
			});
			data.add(new HashMap<String, String>() {
				{
					put("title", "수");
				}
			});
			data.add(new HashMap<String, String>() {
				{
					put("title", "목");
				}
			});
			data.add(new HashMap<String, String>() {
				{
					put("title", "금");
				}
			});
			data.add(new HashMap<String, String>() {
				{
					put("title", "토");
				}
			});
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, selectedCal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, selectedCal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int weekFirstDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		for (int i = 0; i < weekFirstDay; i++) {
			HashMap<String, String> t1 = new HashMap<String, String>();
			data.add(t1);
		}
		for (int i = 0; i < cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			HashMap<String, String> t2 = new HashMap<String, String>();
			t2.put("date", dateformat.format(cal.getTime()));
			t2.put("day", Integer.toString(i + 1) + "");
			int wks = wkt.getValue(wkt.getKey(cal));
			int sts = stt.getValue(stt.getKey(cal));
			t2.put("wkm", wks / 60 + "");
			t2.put("stm", sts / 60 + "");
			if (wks > 0)
				t2.put("wkc", "O");
			if (sts > 0)
				t2.put("stc", "O");
			data.add(t2);
		}
	}

	Handler textHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// android:id="@+id/recordingcal_stretching_min"
			HashMap<String, String> dt = data.get(msg.what);
			if (dt.containsKey("date")) {
				recordingcal_date.setText(dt.get("date"));
				if (dt.containsKey("wkm")) {
					recordingcal_walking_min.setText(dt.get("wkm"));
				}
				if (dt.containsKey("stm")) {
					recordingcal_stretching_min.setText(dt.get("stm"));
				}
			}
		}
	};
}
