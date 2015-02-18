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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.TodaysWorkingoutList;
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
import android.widget.Adapter;
import android.widget.Button;
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
	String pace_units;//steps_per_minute
	String distance_string;// kilometers
	String distance_units;
	String speed_string;// kilometers / hour
	String speed_units;
	String calories_string;// calories burned

	private DBAdapter dbadapter;
	SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
	String db_str_walking = "wk_";
	
	
	
	private PieGraph pedometer_today_walkingcircle;
	private LineGraph pedometer_week_walkingline;
	private PieSlice slice;
	/**
	 * True, when service is running.
	 */
	private boolean mIsRunning;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "[ACTIVITY] onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
		pedometer_week_walkingline = (LineGraph)findViewById(R.id.pedometer_week_walkingline);
		pedometer_today_walkingcircle = (PieGraph) findViewById(R.id.pedometer_today_walkingcircle); 
		
		
		dbadapter = new DBAdapter(getApplicationContext()); 

		loadLastDataForGraph();
		mUtils = Utils.getInstance();
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "[ACTIVITY] onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
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
			startStepService();
			bindStepService();
		} else if (mIsRunning) {
			bindStepService();
		}

		mPedometerSettings.clearServiceRunning();

		mStepValueView = (TextView) findViewById(R.id.step_value);

		mIsMetric = mPedometerSettings.isMetric();

		steps_units = getString(R.string.steps);
		distance_units = getString(mIsMetric ? R.string.kilometers
				: R.string.miles);
		speed_units = getString(mIsMetric ? R.string.kilometers_per_hour
				: R.string.miles_per_hour);

		mMaintain = mPedometerSettings.getMaintainOption();
		/*
		 * ((LinearLayout)
		 * this.findViewById(R.id.desired_pace_control)).setVisibility(
		 * mMaintain != PedometerSettings.M_NONE ? View.VISIBLE : View.GONE );
		 * if (mMaintain == PedometerSettings.M_PACE) { mMaintainInc = 5f;
		 * mDesiredPaceOrSpeed = (float)mPedometerSettings.getDesiredPace(); }
		 * else if (mMaintain == PedometerSettings.M_SPEED) {
		 * mDesiredPaceOrSpeed = mPedometerSettings.getDesiredSpeed();
		 * mMaintainInc = 0.1f; } Button button1 = (Button)
		 * findViewById(R.id.button_desired_pace_lower);
		 * button1.setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View v) { mDesiredPaceOrSpeed -= mMaintainInc;
		 * mDesiredPaceOrSpeed = Math.round(mDesiredPaceOrSpeed * 10) / 10f;
		 * displayDesiredPaceOrSpeed();
		 * setDesiredPaceOrSpeed(mDesiredPaceOrSpeed); } }); Button button2 =
		 * (Button) findViewById(R.id.button_desired_pace_raise);
		 * button2.setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View v) { mDesiredPaceOrSpeed += mMaintainInc;
		 * mDesiredPaceOrSpeed = Math.round(mDesiredPaceOrSpeed * 10) / 10f;
		 * displayDesiredPaceOrSpeed();
		 * setDesiredPaceOrSpeed(mDesiredPaceOrSpeed); } }); if (mMaintain !=
		 * PedometerSettings.M_NONE) { ((TextView)
		 * findViewById(R.id.desired_pace_label)).setText( mMaintain ==
		 * PedometerSettings.M_PACE ? R.string.desired_pace :
		 * R.string.desired_speed ); }
		 * 
		 * 
		 * displayDesiredPaceOrSpeed();
		 */
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

	@Override
	protected void onStop() {
		Log.i(TAG, "[ACTIVITY] onStop");
		super.onStop();
	}

	protected void onDestroy() {
		Log.i(TAG, "[ACTIVITY] onDestroy");
		super.onDestroy();
	}

	protected void onRestart() {
		Log.i(TAG, "[ACTIVITY] onRestart");
		super.onDestroy();
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

	
	/* Creates the menu items */
	/*
	private static final int MENU_SETTINGS = 8;
	private static final int MENU_QUIT = 9;

	private static final int MENU_PAUSE = 1;
	private static final int MENU_RESUME = 2;
	private static final int MENU_RESET = 3;
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		
		menu.clear();
		if (mIsRunning) {
			menu.add(0, MENU_PAUSE, 0, R.string.pause)
					.setIcon(android.R.drawable.ic_media_pause)
					.setShortcut('1', 'p');
		} else {
			menu.add(0, MENU_RESUME, 0, R.string.resume)
					.setIcon(android.R.drawable.ic_media_play)
					.setShortcut('1', 'p');
		}
		menu.add(0, MENU_RESET, 0, R.string.reset)
				.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
				.setShortcut('2', 'r');
		menu.add(0, MENU_SETTINGS, 0, R.string.settings)
				.setIcon(android.R.drawable.ic_menu_preferences)
				.setShortcut('8', 's')
				.setIntent(new Intent(this, Settings.class));
		menu.add(0, MENU_QUIT, 0, R.string.quit)
				.setIcon(android.R.drawable.ic_lock_power_off)
				.setShortcut('9', 'q');
		return true;
		
		
	}
 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PAUSE:
			unbindStepService();
			stopStepService();
			return true;
		case MENU_RESUME:
			startStepService();
			bindStepService();
			return true;
		case MENU_RESET:
			resetValues(true);
			return true;
		case MENU_QUIT:
			resetValues(false);
			unbindStepService();
			stopStepService();
			mQuitting = true;
			finish();
			return true;
		}
		return false;
	}*/

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
				// 걸음걸을때
				mStepValue = (int) msg.arg1;
				loadStepsMsg(mStepValue);
				break;
			case PACE_MSG:
				mPaceValue = msg.arg1;
				loadPaceMsg(mPaceValue);
				break;
			case DISTANCE_MSG:
				mDistanceValue = ((int) msg.arg1) / 1000f;
				loadDistanceMsg(mDistanceValue);
				break;
			case SPEED_MSG:
				mSpeedValue = ((int) msg.arg1) / 1000f;
				loadSpeedMsg(mSpeedValue);
				break;
			case CALORIES_MSG:
				mCaloriesValue = msg.arg1;
				loadCalories(mCaloriesValue); 
				break;
			default:
				super.handleMessage(msg);
			}
		}

	};
	
	String[] label_linegraph;
	int[] value_linegraph; 
	Date today;
	String db_str_walking_today;
	
	void loadLastDataForGraph() {
		Log.d(TAG, "loadLastDataForGraph start");
		today = new Date();
		Date lastDay = new Date();
		
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");  
		
		
		db_str_walking_today = db_str_walking+dbformat.format(today); 
		
		
		
		label_linegraph = new String[7];
		value_linegraph = new int[7];
		
		
		Log.d(TAG, "lastDay "+lastDay);
		int idx = 6;
		for(int i=0; i<7; i++) {
			try {
				String dbkey = db_str_walking+dbformat.format(lastDay);
				String dbvalue = dbadapter.get_setting(dbkey);
				Log.d(TAG, "dbkey : " + dbkey + ", data : " + dbvalue);
				value_linegraph[6-i] = Integer.parseInt(dbvalue);
			} catch (Exception e) {
				value_linegraph[6-i] = 0;
			}
			label_linegraph[6-i] = format.format(lastDay);
			
			lastDay = this.getYesterday(lastDay);
		}
	}
	
	
	void loadStepsMsg(int steps) {
		steps_string = steps + steps_units;
		mStepValueView.setText(steps_string);
		
		dbadapter.put_setting(db_str_walking_today, steps+"");//
		value_linegraph[6] = steps;
		
		pedometer_today_walkingcircle.removeSlices();
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#3ec2c7"));
		slice.setValue(mStepValue%100);
		pedometer_today_walkingcircle.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(Color.parseColor("#eeeeee"));
		slice.setValue(100 - (mStepValue%100));
		pedometer_today_walkingcircle.addSlice(slice); 
		 
		
		
		pedometer_week_walkingline.removeAllLines();
		
		Line l = new Line();
		LinePoint p;		
		
		for(int i=0; i<7; i++) {
			p = new LinePoint(); 
			p.setX(i); 
			p.setY(value_linegraph[i]); 
			l.addPoint(p); 
			p.setLabel_string(label_linegraph[i]);
		} 
		
		l.setColor(Color.parseColor("#3ec2c7"));
		
		pedometer_week_walkingline.setMinY(0);
		pedometer_week_walkingline.addLine(l);
		pedometer_week_walkingline.setLabelSize(30);
		pedometer_week_walkingline.setRangeY(0, getMaxWalking());
		pedometer_week_walkingline.setLineToFill(1);
	}
	int getMaxWalking() {
		int max = 100; 
		for(int i=0; i<7; i++) {
			if (max < value_linegraph[i]) {
				max = value_linegraph[i];
			}
		}
		return max;
	}
	void loadPaceMsg(int pace) {
		if (pace <= 0) {
			pace_string = "0"+pace_units;
		} else {
			pace_string = ("" + (int) pace)+pace_units;
		}
	}
	void loadDistanceMsg(float distance) {
		if (distance <= 0) {
			distance_string = "0"+distance_units;
		} else {
			distance_string = ("" + (distance + 0.000001f))
					.substring(0, 5)+distance_units;
		}
	}
	void loadSpeedMsg(float speed) {
		if (speed <= 0) {
			speed_string = "0" +speed_units;
		} else {
			speed_string = (("" + (speed + 0.000001f)).substring(
					0, 4)) +speed_units;
		}
	}
	void loadCalories(int calories){
		if (calories <= 0) {
			calories_string = "0" + "칼로리";
		} else {
			calories_string = ("" + (int) calories) + "칼로리";
		}
	}

	public static Date getYesterday ( Date today )
	{
	    if ( today == null ) 
	        throw new IllegalStateException ( "today is null" );
	 
	    Date yesterday = new Date ( );
	    yesterday.setTime ( today.getTime ( ) - ( (long) 1000 * 60 * 60 * 24 ) );
	     
	    return yesterday;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}