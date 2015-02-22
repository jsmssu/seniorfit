package name.sunme.maindrawbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar; 

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecordingFragment extends Fragment {
	String TAG = "RecordingFragment";
	DBAdapter dbAdapter;
	int[] walking_values;
	int[] stretching_values;
	String[] labels;
	Calendar[] days;
	int N_WALKING_POINTS = 7;
	private LineChart mChart;

	TextView recording_min_walking;
	TextView recording_min_stretcing;
	TextView recording_goal_min;
	public RecordingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// do modify
		Log.d(TAG, "onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_recording,
				container, false); 
		dbAdapter = new DBAdapter(getActivity());  
		
		mChart = (LineChart)rootView.findViewById(R.id.chart); 
		recording_min_walking = (TextView)rootView.findViewById(R.id.recording_min_walking); 
		recording_min_stretcing = (TextView)rootView.findViewById(R.id.recording_min_stretcing); 
		recording_goal_min = (TextView)rootView.findViewById(R.id.recording_goal_min);
		
		loadValues();

		
		
		












		
		Log.d(TAG, "setValueTextColor");
		mChart.setValueTextColor(Color.BLACK);
		mChart.setDescription("기록보기");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		mChart.setDrawGridBackground(false);
		mChart.setBackgroundColor(Color.WHITE);
		mChart.setGridWidth(1.25f); 
		mChart.setTouchEnabled(true); 
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		
/////////////////////////////////////////////////////////////////////////////value set
		Log.d(TAG, "value set");
		ArrayList<Entry> walkings = new ArrayList<Entry>();
		ArrayList<Entry> stretcings = new ArrayList<Entry>();

		int max_walking = 0;
		int max_stretcing = 0;
		for(int i=0;i<N_WALKING_POINTS; i++) {
			//
			if (max_walking<walking_values[i]) { max_walking = walking_values[i]; }
			if (max_stretcing<stretching_values[i]) { max_stretcing = stretching_values[i]; }
			//
			Entry walking_c = new Entry(walking_values[i], i); // 0 == quarter 1
			walkings.add(walking_c);	
			Entry stretcing_c = new Entry(stretching_values[i], i); // 0 == quarter 1
			stretcings.add(stretcing_c);	
		}
		  
		LineDataSet linedataset1 = new LineDataSet(walkings, "하루 걸음");
		LineDataSet linedataset2 = new LineDataSet(stretcings, "스트레칭");
		linedataset1.setColor(Color.parseColor("#ff0000"));
		linedataset2.setColor(Color.parseColor("#00ff00"));
		
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(linedataset1); 
		dataSets.add(linedataset2); 
		
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.parseColor("#ff0000"));
        rightAxis.setAxisMaxValue(max_walking+10);
        rightAxis.setDrawGridLines(false);
        
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTextColor(Color.parseColor("#00ff00"));
        leftAxis.setAxisMaxValue(max_stretcing+10);
        leftAxis.setDrawGridLines(true);

/////////////////////////////////////////////////////////////////////////////label set
		Log.d(TAG, "label set");
		ArrayList<String> xVals = new ArrayList<String>();
		for(int i=0; i<N_WALKING_POINTS; i++) {
			xVals.add(labels[i]);
		}
///////////////////////////////////////////////////////////////////////////// data set
		Log.d(TAG, "data set");
		LineData data = new LineData(xVals, dataSets); 
		mChart.setData(data);
///////////////////////////////////////////////////////////////////////////// view
		Log.d(TAG, "view");
		Legend l = mChart.getLegend();
		l.setFormSize(10f); // set the size of the legend forms/shapes
		l.setForm(LegendForm.CIRCLE); // set what type of form/shape should be
										// used
		l.setPosition(LegendPosition.BELOW_CHART_CENTER);

		XAxis xl = mChart.getXAxis();
		xl.setPosition(XAxisPosition.BOTTOM);
		xl.setGridColor(Color.parseColor("#3ec2c7"));
		
		
		return rootView;
	}


	void loadValues() {
		loadDays(); 
		loadGraphLabels(); 
		loadGraphWalkingValues();
		loadGraphStretchingValues();
		loadGoalMin();
		loadWalkingText();
		loadStrectingText();
	}
	void loadGoalMin() {
		if (dbAdapter.get_setting("goalMinutes")!=null) {
			int goal_min = Integer.parseInt(dbAdapter.get_setting("goalMinutes")); 
			int strecting_min = stretching_values[N_WALKING_POINTS-1];
			int left_min = goal_min-strecting_min;
			if (left_min<=0) {
				recording_goal_min.setText("(완료)");	
			} else {
				recording_goal_min.setText(left_min+"");
			}
		}
		else {
			recording_goal_min.setText("(목표를 설정해주세욥)");
		}
	}
	void loadWalkingText() {
		recording_min_walking.setText(walking_values[N_WALKING_POINTS-1]+"");
	}
	void loadStrectingText() {
		recording_min_stretcing.setText(stretching_values[N_WALKING_POINTS-1]+"");
	}
	void loadDays() {
		days = new Calendar[N_WALKING_POINTS];
		for(int i=0; i<N_WALKING_POINTS; i++) {
			days[i] = Calendar.getInstance();
			days[i].add(Calendar.DATE, -N_WALKING_POINTS+i+1);
		}
	}
	
	void loadGraphLabels() {
		labels = new String[N_WALKING_POINTS];
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");
		for(int i=0; i<N_WALKING_POINTS; i++) {
			labels[i] = format.format(days[i].getTime());
		} 
	}
	String db_str_walking = "wk_";
	void loadGraphWalkingValues() {
		SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
		walking_values = new int[N_WALKING_POINTS];
		for(int i=0; i<N_WALKING_POINTS; i++) {
			String key = db_str_walking + dbformat.format(days[i].getTime());
			String value = dbAdapter.get_setting(key);
			if (value!=null) {
				walking_values[i] =	Integer.parseInt(value);
			}
		} 
	}
	String db_str_stretcing = "tw_";
	void loadGraphStretchingValues() { //stretching_values
		SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
		stretching_values = new int[N_WALKING_POINTS];
		for(int i=0; i<N_WALKING_POINTS; i++) {
			String key = db_str_stretcing + dbformat.format(days[i].getTime());
			String value = dbAdapter.get_setting(key);
			if (value!=null) {
				stretching_values[i] =	Integer.parseInt(value)/60;
			}
		} 
	}
}