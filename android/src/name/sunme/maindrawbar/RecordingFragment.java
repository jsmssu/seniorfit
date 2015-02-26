package name.sunme.maindrawbar;

import java.text.DecimalFormat;
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
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import name.sunme.calendar.RecordingCalActivity;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.Utils;
import name.sunme.seniorfit.WalkingGraphData;
import name.sunme.setting.SettingGoalActivity;
import name.sunme.setting.SettingProfileActivity;
import name.sunme.timer.StretcingTimer;
import name.sunme.timer.WalkingTimer;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RecordingFragment extends Fragment {
	String TAG = "RecordingFragment";
	DBAdapter dbAdapter;
	ArrayList<Entry> walking_values;
	ArrayList<Entry> stretching_values;
	String[] labels;

	Calendar[] days;
	int N_WALKING_POINTS = 7;
	private LineChart mChart;
	LinearLayout recording_showgoal;
	TextView recording_min_walking;
	TextView recording_min_stretcing;
	TextView recording_goal_min;
	
	
	ImageView recording_progress1;
	ImageView recording_progress2;
	
	
	LinearLayout recording_boxcal;
	
	StretcingTimer st;
	WalkingTimer wt;
	
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
		
		
		recording_progress1 = (ImageView)rootView.findViewById(R.id.recording_progress1);
		recording_progress2 = (ImageView)rootView.findViewById(R.id.recording_progress2);
		
		recording_showgoal = (LinearLayout)rootView.findViewById(R.id.recording_showgoal);		
		recording_boxcal = (LinearLayout)rootView.findViewById(R.id.recording_boxcal);
		recording_boxcal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RecordingCalActivity.class);
				startActivity(intent);				
			}
		});
		
		
		st = new StretcingTimer(getActivity());
		wt = new WalkingTimer(getActivity());
		
		
		loadValues();



		
		Log.d(TAG, "setValueTextColor");
		mChart.setValueTextColor(Color.BLACK);
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		mChart.setDrawGridBackground(false);
		mChart.setBackgroundColor(Color.WHITE);
		mChart.setGridWidth(1.25f); 
		mChart.setTouchEnabled(true); 
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setPinchZoom(true);
		
		 
		//mChart.animateX(500);
/////////////////////////////////////////////////////////////////////////////value set
		Log.d(TAG, "value set");

		  
		LineDataSet linedataset1 = new LineDataSet(walking_values, "하루 걸음");
		linedataset1.setAxisDependency(AxisDependency.RIGHT);
		linedataset1.setColor(Color.parseColor("#3ec2c7"));
		linedataset1.setCircleColor(Color.parseColor("#3ec2c7"));
		linedataset1.setLineWidth(2f);
		linedataset1.setCircleSize(4f);
		linedataset1.setFillAlpha(65);
		linedataset1.setFillColor(Color.WHITE);
		linedataset1.setHighLightColor(Color.rgb(244, 117, 117));
		
		
		LineDataSet linedataset2 = new LineDataSet(stretching_values, "스트레칭");
		linedataset2.setAxisDependency(AxisDependency.LEFT);
		linedataset2.setColor(Color.parseColor("#ffa655"));
		linedataset2.setCircleColor(Color.parseColor("#ffa655"));
		linedataset2.setLineWidth(2f);
		linedataset2.setCircleSize(4f);
		linedataset2.setFillAlpha(65);
		linedataset2.setFillColor(Color.WHITE);
		linedataset2.setHighLightColor(Color.rgb(244, 117, 117));		
		
		
		linedataset1.setDrawValues(false);
		linedataset2.setDrawValues(false);
		
		
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(linedataset1); 
		dataSets.add(linedataset2); 
		
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.parseColor("#3ec2c7")); 
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMaxValue(max_wk+10);
        
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTextColor(Color.parseColor("#ffa655")); 
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMaxValue(max_tw+10);

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
		mChart.setDescription("");
///////////////////////////////////////////////////////////////////////////// view
		Log.d(TAG, "view");
		Legend l = mChart.getLegend();
		l.setFormSize(10f);
		l.setForm(LegendForm.CIRCLE);
		l.setPosition(LegendPosition.BELOW_CHART_CENTER);

		XAxis xl = mChart.getXAxis();
		xl.setPosition(XAxisPosition.BOTTOM);
		xl.setGridColor(Color.WHITE);
		xl.setDrawGridLines(false);
		
		return rootView;
	}


	void loadValues() {
		loadDays(); 
		loadGraphLabels(); 
		loadGraphValues();
		loadGoalMin();
		loadWalkingText();
		loadStrectingText();
	}
	void loadGoalMin() {
		if (dbAdapter.get_setting("goalMinutes")!=null) {
			int goal_min = Integer.parseInt(dbAdapter.get_setting("goalMinutes")); 
			int strecting_min = (int) stretching_values.get(N_WALKING_POINTS-1).getVal();
			int walking_min = (int) walking_values.get(N_WALKING_POINTS-1).getVal();
			int left_min = goal_min-strecting_min-walking_min;
			if (left_min<=0) {
				left_min = 0;
				recording_goal_min.setText("오늘의 목표 완료");	
			} else {
				recording_goal_min.setText("오늘의 목표까지  "+(int)left_min+"분");
			}
				
			LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			p1.weight = strecting_min+walking_min;
			recording_progress1.setLayoutParams(p1);
			
			LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			p2.weight = left_min;
			recording_progress2.setLayoutParams(p2);
		}
		else {
			recording_goal_min.setText("목표를 설정해주세요.");
		}
		recording_showgoal.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SettingGoalActivity.class);
				startActivity(intent);  
			}
		});
	}
	@Override
	public
	void onResume() {
		super.onResume();
		loadValues();
	}
	void loadWalkingText() {
		recording_min_walking.setText((int)walking_values.get(N_WALKING_POINTS-1).getVal()+"");
	}
	void loadStrectingText() {
		recording_min_stretcing.setText((int)stretching_values.get(N_WALKING_POINTS-1).getVal()+"");
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
	String db_str_stretcing = "tw_";
	int max_wk = 0;
	int max_tw = 0;
	void loadGraphValues() {
		SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
		walking_values = new ArrayList<Entry>();
		stretching_values = new ArrayList<Entry>();
		for(int i=0; i<N_WALKING_POINTS; i++) {
			String dbf = dbformat.format(days[i].getTime());
			
			
			int walking_value = wt.getValue(wt.getKey(days[i]))/60; 
			walking_values.add(new Entry(walking_value , i));
			if (max_wk < walking_value) max_wk = walking_value;
			
			
			int stretcing_value = st.getValue(st.getKey(days[i]))/60;
			stretching_values.add(new Entry(stretcing_value, i));
			if (max_wk < stretcing_value) max_tw = stretcing_value; 
		} 
	} 
}