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

public class RecordingFragment extends Fragment {
	String TAG = "RecordingFragment";
	DBAdapter dbAdapter;
	int[] values;
	String[] labels;
	Calendar[] days;
	int N_WALKING_POINTS = 7;
	private LineChart mChart;

	public RecordingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// do modify
		Log.d(TAG, "onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_recording,
				container, false);
		Log.d(TAG, "dbAdapter");
		dbAdapter = new DBAdapter(getActivity()); 
		Log.d(TAG, "mChart");
		
		mChart = (LineChart) rootView.findViewById(R.id.chart); 
		
		Log.d(TAG, "loadValues");
		loadValues();

		
		Log.d(TAG, "setValueTextColor");
		mChart.setValueTextColor(Color.BLACK);
		mChart.setDescription("기록보기");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		mChart.setDrawGridBackground(true);
		mChart.setBackgroundColor(Color.WHITE);
		mChart.setGridWidth(1.25f); 
		mChart.setTouchEnabled(true); 
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		
/////////////////////////////////////////////////////////////////////////////value set
		Log.d(TAG, "value set");
		ArrayList<Entry> walkings = new ArrayList<Entry>();
		for(int i=0;i<N_WALKING_POINTS; i++) {
			Entry c = new Entry(values[i], i); // 0 == quarter 1
			walkings.add(c);	
		}
		  
		LineDataSet linedataset1 = new LineDataSet(walkings, "하루 걸음");
		linedataset1.setColor(Color.parseColor("#3ec2c7"));
		
		
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(linedataset1); 
		
		
		
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
		Log.d(TAG, "loadDays");
		loadDays();
		Log.d(TAG, "loadGraphLabels");
		loadGraphLabels();
		Log.d(TAG, "loadGraphValues");
		loadGraphValues();
	}
	void loadDays() {
		days = new Calendar[N_WALKING_POINTS];
		for(int i=0; i<N_WALKING_POINTS; i++) {
			days[i] = Calendar.getInstance();
			days[i].add(Calendar.DATE, -N_WALKING_POINTS+i+1);
		}
	}
	String db_str_walking = "wk_";
	void loadGraphLabels() {
		labels = new String[N_WALKING_POINTS];
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");
		for(int i=0; i<N_WALKING_POINTS; i++) {
			labels[i] = format.format(days[i].getTime());
		} 
	}
	void loadGraphValues() {
		SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
		values = new int[N_WALKING_POINTS];
		for(int i=0; i<N_WALKING_POINTS; i++) {
			String key = db_str_walking + dbformat.format(days[i].getTime());
			String value = dbAdapter.get_setting(key);
			if (value!=null) {
				values[i] =	Integer.parseInt(value);
			}
		} 
	}
}