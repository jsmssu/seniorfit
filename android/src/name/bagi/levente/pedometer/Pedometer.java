/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package name.bagi.levente.pedometer;

import java.text.AttributedCharacterIterator.Attribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import name.sunme.maindrawbar.R;
import name.sunme.map.GoogleMapActivity;
import name.sunme.map.MenuListener;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.timer.WalkingTimer;
import name.sunme.walking.WalkingActivity;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Pedometer extends Activity {
	private static final String TAG = "Pedometer";
	private SharedPreferences mSettings;
	private PedometerSettings mPedometerSettings;
	private Utils mUtils;

	private TextView mStepValueView;
	private TextView mPaceValueView;
	private TextView mDistanceValueView;
	private TextView mSpeedValueView;
	private TextView mCaloriesValueView;
	TextView mDesiredPaceView;
	private int mStepValue = 0;
	private int mPaceValue = 0;
	private float mDistanceValue;
	private float mSpeedValue;
	private int mCaloriesValue;
	private float mDesiredPaceOrSpeed;
	private int mMaintain;
	private boolean mIsMetric;
	private float mMaintainInc;
	private boolean mQuitting = false; // Set when user selected Quit from menu,
										// can be used by onPause, onStop,
										// onDestroy

	String steps_string;//
	String steps_units;
	String pace_string;// steps
	String pace_units;// steps_per_minute
	String distance_string;// kilometers
	String distance_units;
	String speed_string;// kilometers / hour
	String speed_units;
	String calories_string;// calories burned

	private DBAdapter dbAdapter;
	SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");


	private PieChart walkingcircle;
	private LineChart walkingline;
;
	int N_WALKING_POINTS = 7;
	ImageView pedometer_start;
	/**
	 * True, when service is running.
	 */
	private boolean mIsRunning;

	
	ArrayList<Entry> walking_values;
	LineDataSet linedataset1;
	YAxis leftAxis;
	int max_wk;
	/** Called when the activity is first created. */
	 
	
	
	
	
	PieDataSet dataSet;
	ArrayList<Entry> yVals2;
	Entry circle_e1;
	Entry circle_e2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "[ACTIVITY] onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		walkingline = (LineChart) findViewById(R.id.pedometer_week_walkingline);
		walkingcircle = (PieChart) findViewById(R.id.pedometer_today_walkingcircle);
		pedometer_start = (ImageView) findViewById(R.id.pedometer_start);

		dbAdapter = new DBAdapter(getApplicationContext());

		
		MenuListener ml = new MenuListener(this); 
		ml.walking_calc.setBackgroundResource(R.drawable.walk_tapbar_selected);
		ml.walking_rec.setOnClickListener(new OnClickListener(){  
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Pedometer.this, GoogleMapActivity.class);
	            startActivity(intent);
			}
	    });
		
		
		mUtils = Utils.getInstance();
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		pedometer_start.setOnClickListener(start_clicklistener);
		pedometer_start.setImageResource(R.drawable.walk_icn_start);
		 
		
		
		walkingline.setValueTextColor(Color.BLACK);
		walkingline.setNoDataTextDescription("You need to provide data for the chart.");
		walkingline.setDrawGridBackground(false);
		walkingline.setBackgroundColor(Color.WHITE);
		walkingline.setGridWidth(1.25f); 
		walkingline.setTouchEnabled(true); 
		walkingline.setDragEnabled(true);
		walkingline.setScaleEnabled(true);
		walkingline.setPinchZoom(true);
		
		 
		
		SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");
		ArrayList<String> xVals = new ArrayList<String>();
		walking_values = new ArrayList<Entry>();
		Calendar day = Calendar.getInstance();
		max_wk = 0;
		
		day.add(Calendar.DATE, -6);
		for(int i=0; i<N_WALKING_POINTS; i++) {
			String dbf = dbformat.format(day.getTime());
			String walking_key = "wk_" + dbf;
			String walking_value = dbAdapter.get_setting(walking_key);
			if (walking_value!=null) {
				int v = Integer.parseInt(walking_value);
				Entry c = new Entry(v, i); 
				walking_values.add(c);
				if (max_wk < v) max_wk = v;
			} else {
				Entry c = new Entry(0, i); 
				walking_values.add(c);
			} 
			xVals.add(format.format(day.getTime())); 
			day.add(Calendar.DATE, +1);
		} 
		
		linedataset1 = new LineDataSet(walking_values, "걸음수");
		linedataset1.setAxisDependency(AxisDependency.LEFT);
		linedataset1.setColor(Color.parseColor("#3ec2c7"));
		linedataset1.setCircleColor(Color.parseColor("#3ec2c7"));
		linedataset1.setLineWidth(2f);
		linedataset1.setCircleSize(4f);
		linedataset1.setFillAlpha(65);
		linedataset1.setFillColor(Color.WHITE);
		linedataset1.setHighLightColor(Color.rgb(244, 117, 117));	
		
		linedataset1.setDrawValues(false);
		
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(linedataset1); 
		
		
		leftAxis = walkingline.getAxisLeft();
		leftAxis.setTextColor(Color.parseColor("#3ec2c7")); 
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMaxValue(max_wk+10);
        YAxis rightAxis = walkingline.getAxisRight();
        rightAxis.setEnabled(false);
        LineData data = new LineData(xVals, dataSets); 
        walkingline.setData(data);
        walkingline.setDescription("");
        
        
        Legend l = walkingline.getLegend();
		l.setFormSize(10f);
		l.setForm(LegendForm.CIRCLE);
		l.setPosition(LegendPosition.BELOW_CHART_CENTER);

		XAxis xl = walkingline.getXAxis();
		xl.setPosition(XAxisPosition.BOTTOM);
		xl.setGridColor(Color.WHITE);
		xl.setDrawGridLines(false);
		
		walkingline.invalidate();
///////////////////////////////////////////////////////////////////////////////////////////
		 
		walkingcircle.setValueTextColor(Color.BLACK);
		walkingcircle
				.setNoDataTextDescription("You need to provide data for the chart.");
		walkingcircle.setBackgroundColor(Color.WHITE);
		walkingcircle.setTouchEnabled(true);
		walkingcircle.setUsePercentValues(false);
		walkingcircle.setHoleColorTransparent(false); 
		walkingcircle.setHoleRadius(80f);
		walkingcircle.setDescription("");
		walkingcircle.setDrawCenterText(false);
		walkingcircle.setDrawHoleEnabled(true);
		walkingcircle.setRotationAngle(270); 
		walkingcircle.setRotationEnabled(true); 
		//walkingcircle.setCenterText("걸음수"); 
		yVals2 = new ArrayList<Entry>();
		
		circle_e1 = new Entry(0, 0);
		circle_e2 = new Entry(0, 1);
		key_walking_today = "wk_" + dbformat.format(day.getTime());
		String v = dbAdapter.get_setting(key_walking_today);
		if (v!=null) {
			circle_e1.setVal(Integer.parseInt(v)%100); 
		}  
		circle_e2.setVal(100-circle_e1.getVal());
		yVals2.add(circle_e1);
		yVals2.add(circle_e2); 
		
		ArrayList<String> xVals2 = new ArrayList<String>();
		xVals2.add("");
		xVals2.add(""); 
		dataSet = new PieDataSet(yVals2, "뚭뚭");
		dataSet.setSliceSpace(3f);
		dataSet.setDrawValues(false);
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(Color.parseColor("#3ec2c7"));
		colors.add(Color.parseColor("#eeeeee"));
		dataSet.setColors(colors);

		PieData data2 = new PieData(xVals2, dataSet);
		walkingcircle.setData(data2);
		walkingcircle.highlightValues(null);
		walkingcircle.invalidate(); 
		walkingcircle.setDrawSliceText(false);
		walkingcircle.setUsePercentValues(false); 
		walkingcircle.invalidate();
		
		walkingcircle.setPadding(0, 0, 0, 0);
		walkingcircle.setDrawLegend(false);
		
		
		timer = new WalkingTimer(getApplicationContext());
	}//mIsRunning
	WalkingTimer timer;
	 


	
	
	
	OnClickListener start_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!mIsRunning && mPedometerSettings.isNewStart()) {
				timer.start();
				startStepService();
				bindStepService();
				pedometer_start.setImageResource(R.drawable.walk_icn_end);
			} else if (mIsRunning) {
				timer.stop();
				unbindStepService();
 				stopStepService();
 				pedometer_start.setImageResource(R.drawable.walk_icn_start);
			} 
						
			
		}
	};	
	
	
	
	
	@Override
	protected void onStart() {
		Log.i(TAG, "[ACTIVITY] onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		this.overridePendingTransition(0,0);
		Log.i(TAG, "[ACTIVITY] onResume");
		super.onResume();

		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = mSettings.edit();
		e.putString("units", "metric");
		e.commit();

		mPedometerSettings = new PedometerSettings(mSettings);

		mUtils.setSpeak(mSettings.getBoolean("speak", false));
		// Read from preferences if the service was running on the last onPause
		mIsRunning = mPedometerSettings.isServiceRunning();

		
		
		
		
		// Start the service if this is considered to be an application start
		// (last onPause was long ago)
		if (!mIsRunning && mPedometerSettings.isNewStart()) {
			timer.start();
			bindStepService();
		} else if (mIsRunning) {
			bindStepService();
		}
		
		
		
		
		
		pedometer_start.setOnClickListener(start_clicklistener); 
		mPedometerSettings.clearServiceRunning(); 
		mStepValueView = (TextView) findViewById(R.id.step_value); 
		mIsMetric = mPedometerSettings.isMetric(); 
		steps_units = getString(R.string.steps);
		distance_units = getString(mIsMetric ? R.string.kilometers
				: R.string.miles);
		speed_units = getString(mIsMetric ? R.string.kilometers_per_hour
				: R.string.miles_per_hour);

		mMaintain = mPedometerSettings.getMaintainOption();
		
		
		
		
		
		if (mIsRunning==true) {
			pedometer_start.setImageResource(R.drawable.walk_icn_end);
		} else {
			pedometer_start.setImageResource(R.drawable.walk_icn_start);
		} 
	}

	private void displayDesiredPaceOrSpeed() {
		if (mMaintain == PedometerSettings.M_PACE) {
			mDesiredPaceView.setText("" + (int) mDesiredPaceOrSpeed);
		} else {
			mDesiredPaceView.setText("" + mDesiredPaceOrSpeed);
		}
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "[ACTIVITY] onPause");
		if (mIsRunning) {
			unbindStepService();
		}
		if (mQuitting) {
			mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
		} else {
			mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
		}

		super.onPause();
		savePaceSetting();
	} 

	private void setDesiredPaceOrSpeed(float desiredPaceOrSpeed) {
		if (mService != null) {
			if (mMaintain == PedometerSettings.M_PACE) {
				mService.setDesiredPace((int) desiredPaceOrSpeed);
			} else if (mMaintain == PedometerSettings.M_SPEED) {
				mService.setDesiredSpeed(desiredPaceOrSpeed);
			}
		}
	}

	private void savePaceSetting() {
		mPedometerSettings.savePaceOrSpeedSetting(mMaintain,
				mDesiredPaceOrSpeed);
	}

	private StepService mService;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = ((StepService.StepBinder) service).getService();

			mService.registerCallback(mCallback);
			mService.reloadSettings();

		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};

	private void startStepService() {
		if (!mIsRunning) {
			Log.i(TAG, "[SERVICE] Start");
			mIsRunning = true;
			startService(new Intent(Pedometer.this, StepService.class));
		}
	}

	private void bindStepService() {
		Log.i(TAG, "[SERVICE] Bind");
		bindService(new Intent(Pedometer.this, StepService.class), mConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}

	private void unbindStepService() {
		Log.i(TAG, "[SERVICE] Unbind");
		unbindService(mConnection);
	}

	private void stopStepService() {
		Log.i(TAG, "[SERVICE] Stop");
		if (mService != null) {
			Log.i(TAG, "[SERVICE] stopService");
			stopService(new Intent(Pedometer.this, StepService.class));
		}
		mIsRunning = false;
	}

	private void resetValues(boolean updateDisplay) {
		if (mService != null && mIsRunning) {
			mService.resetValues();
		} else {
			mStepValueView.setText("0");
			mPaceValueView.setText("0");
			mDistanceValueView.setText("0");
			mSpeedValueView.setText("0");
			mCaloriesValueView.setText("0");
			SharedPreferences state = getSharedPreferences("state", 0);
			SharedPreferences.Editor stateEditor = state.edit();
			if (updateDisplay) {
				stateEditor.putInt("steps", 0);
				stateEditor.putInt("pace", 0);
				stateEditor.putFloat("distance", 0);
				stateEditor.putFloat("speed", 0);
				stateEditor.putFloat("calories", 0);
				stateEditor.commit();
			}
		}
	}
 

	

	// TODO: unite all into 1 type of message
	private StepService.ICallback mCallback = new StepService.ICallback() {
		public void stepsChanged(int value) {
			mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0)); 
		}

		public void paceChanged(int value) {
			mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
		}

		public void distanceChanged(float value) {
			mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG,
					(int) (value * 1000), 0));
		}

		public void speedChanged(float value) {
			mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG,
					(int) (value * 1000), 0));
		}

		public void caloriesChanged(float value) {
			mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG,
					(int) (value), 0));
		}
	};

	private static final int STEPS_MSG = 1;
	private static final int PACE_MSG = 2;
	private static final int DISTANCE_MSG = 3;
	private static final int SPEED_MSG = 4;
	private static final int CALORIES_MSG = 5;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STEPS_MSG:
				 
				mStepValue = (int) msg.arg1; 
				key_walking_today = "wk_" + dbformat.format(new Date());
				
				
				Entry e = walking_values.remove(N_WALKING_POINTS-1);
				e.setVal(mStepValue);
				walking_values.add(N_WALKING_POINTS-1, e);
				if (max_wk < mStepValue) max_wk = mStepValue;
		        leftAxis.setAxisMaxValue(max_wk+10);
				walkingline.notifyDataSetChanged();
				walkingline.invalidate();   
				
				today = new Date();
				key_walking_today = "wk_" + dbformat.format(today);
				dbAdapter.put_setting(key_walking_today, Integer.toString(mStepValue));
				 
				//////////////////////// 
				 
				circle_e1.setVal(mStepValue % 100); 
				circle_e2.setVal(100 - circle_e1.getVal()); 
				walkingcircle.notifyDataSetChanged();
				walkingcircle.invalidate();  
				
				mStepValueView.setText(mStepValue+"걸음");
				
				break;
			case PACE_MSG: 
				if (mPaceValue <= 0) {
					pace_string = "0" + pace_units;
				} else {
					pace_string = ("" + (int) mPaceValue) + pace_units;
				}
				break;
			case DISTANCE_MSG: 
				if (mDistanceValue <= 0) {
					distance_string = "0" + distance_units;
				} else {
					distance_string = ("" + (mDistanceValue + 0.000001f)).substring(0, 5)
							+ distance_units;
				}
				break;
			case SPEED_MSG:
				mSpeedValue = ((int) msg.arg1) / 1000f;
				if (mSpeedValue <= 0) {
					speed_string = "0" + speed_units;
				} else {
					speed_string = (("" + (mSpeedValue + 0.000001f)).substring(0, 4))
							+ speed_units;
				}
				break;
			case CALORIES_MSG: 
				if (mCaloriesValue <= 0) {
					calories_string = "0" + "칼로리";
				} else {
					calories_string = ("" + (int) mCaloriesValue) + "칼로리";
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}

	};

	String[] label_linegraph;
	int[] value_linegraph;
	Date today;
	String key_walking_today; 
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
	 
}