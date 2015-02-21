package name.sunme.setting;

import java.util.Calendar;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.maindrawbar.R.id;
import name.sunme.maindrawbar.R.layout;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.Utils;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingAlarmActivity extends Activity {
	String TAG = "SettingAlarmActivity";
	private ImageView[] setting_alarm; 
	private Boolean[] bool_alarm;
	private boolean bool_switch;
	int[][] img_day;
			 
	private TextView setting_seledctedDay;
	private DBAdapter dbAdapter; 
	private Switch setting_alarm_switch;
	
	private PendingIntent pendingIntent;
	
	private TimePicker settingalarm_time;
	
	private AlarmController acrr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_alarm);
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		dbAdapter = new DBAdapter(getApplicationContext()); 
		acrr = new AlarmController(getApplicationContext());
		
		
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false};
		img_day = new int[][]{
					{drawable.setup_sun_black,drawable.setup_sun_white},
					{drawable.setup_mon_black,drawable.setup_mon_white},
					{drawable.setup_tue_black,drawable.setup_tue_white},
					{drawable.setup_wed_black,drawable.setup_wed_white},
					{drawable.setup_thu_black,drawable.setup_thu_white},
					{drawable.setup_fri_black,drawable.setup_fri_white},
					{drawable.setup_sat_black,drawable.setup_sat_white}
		};		
		setting_alarm = new ImageView[] {
				(ImageView)findViewById(R.id.setting_alarm_Sunday),
                (ImageView)findViewById(R.id.setting_alarm_Monday),
                (ImageView)findViewById(R.id.setting_alarm_Tuesday),
                (ImageView)findViewById(R.id.setting_alarm_Wednesday),
                (ImageView)findViewById(R.id.setting_alarm_Thursday),
                (ImageView)findViewById(R.id.setting_alarm_Friday),
                (ImageView)findViewById(R.id.setting_alarm_Saturday)
        };
		
		
		
		settingalarm_time = (TimePicker)findViewById(R.id.settingalarm_time);
		setting_alarm_switch = (Switch)findViewById(R.id.setting_alarm_switch);
		setting_seledctedDay = (TextView)findViewById(R.id.setting_seledctedDay);
        

		setting_alarm[0].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(0);applyDayString();setDayButton(0);}});
		setting_alarm[1].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(1);applyDayString();setDayButton(1);}});
		setting_alarm[2].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(2);applyDayString();setDayButton(2);}});
		setting_alarm[3].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(3);applyDayString();setDayButton(3);}});
		setting_alarm[4].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(4);applyDayString();setDayButton(4);}});
		setting_alarm[5].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(5);applyDayString();setDayButton(5);}});
		setting_alarm[6].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(6);applyDayString();setDayButton(6);}});
        
        setting_alarm_switch.setOnCheckedChangeListener(alarmSwitch_changelistener);
        
        loadDayValues();
        
        
	}
	OnCheckedChangeListener alarmSwitch_changelistener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			bool_switch = isChecked;
			setAlarm();
		}
	};
	public void saveSwitch() {
		dbAdapter.put_setting("alarmSwitch", Boolean.toString(bool_switch));
	}
	private void loadDaycheck() {
		for	(int i=0; i<7; i++) {
			if (dbAdapter.get_setting("alarm_day"+i)!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(dbAdapter.get_setting("alarm_day"+i));
				setDayButton(i);
			}
		}
	}
	private void loadAlarmSwitch() {
		String alarmSwitch = dbAdapter.get_setting("alarmSwitch"); 
		if(alarmSwitch!=null) {
			setting_alarm_switch.setChecked(Boolean.parseBoolean(alarmSwitch));
		}
	}
	private void loadAlarmDay() {
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false}; 
		for	(int i=0; i<7; i++) {
			if (dbAdapter.get_setting("alarm_day"+i)!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(dbAdapter.get_setting("alarm_day"+i));
			}
		}
	}
	private void applyDayString() { 
		int n=0;
		for	(int i=0; i<7; i++) { 
			if(bool_alarm[i]==true) {
				n = n + 1;
			} 
		}
		
		String seledctedDay = "";
		 
		if (n > 0) {
			if (bool_alarm[0]) seledctedDay = seledctedDay + "일,";
			if (bool_alarm[1]) seledctedDay = seledctedDay + "월,";
			if (bool_alarm[2]) seledctedDay = seledctedDay + "화,";
			if (bool_alarm[3]) seledctedDay = seledctedDay + "수,";
			if (bool_alarm[4]) seledctedDay = seledctedDay + "목,";
			if (bool_alarm[5]) seledctedDay = seledctedDay + "금,";
			if (bool_alarm[6]) seledctedDay = seledctedDay + "토,";
			seledctedDay = seledctedDay.substring(0, seledctedDay.length()-1);
		}
		setting_seledctedDay.setText(seledctedDay);
	}
	private void loadDayValues() {
		loadDaycheck();
		applyDayString();
		loadAlarmSwitch();
		loadTime();
		loadAlarmDay();
	}
	private void saveTime() { 
		int minute = settingalarm_time.getCurrentMinute();
		int hourOfDay = settingalarm_time.getCurrentHour();
		
		Log.d(TAG, "save hour : " +hourOfDay + ", min : "+minute);
		dbAdapter.put_setting("atTimeHour", Integer.toString(hourOfDay));
		dbAdapter.put_setting("atTimeMin", Integer.toString(minute));
	}
	private void loadTime() {
		String hour_str = dbAdapter.get_setting("atTimeHour");
		String min_str = dbAdapter.get_setting("atTimeMin");
		Log.d(TAG, "load h : "+hour_str + ", m :  " + min_str);
		if (hour_str!=null && min_str!=null) {
			settingalarm_time.setCurrentHour(Integer.parseInt(hour_str));
			settingalarm_time.setCurrentMinute(Integer.parseInt(min_str));
		}
	}
	private void saveAlarmDays() {
		for(int i=0; i<7; i++) {
			saveAlarmDay(i);
		}
	}
	private void saveAlarmDay(int day) {
		dbAdapter.put_setting("alarm_day"+day, Boolean.toString(bool_alarm[day]));
	}
	private void toggleDay(int day) {
		bool_alarm[day] = !bool_alarm[day];
	}
	private void setDayButton(int n) {
		if (bool_alarm[n] == true) {setting_alarm[n].setImageResource(img_day[n][1]);}
		else if (bool_alarm[n] == false) {setting_alarm[n].setImageResource(img_day[n][0]);}
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		setAlarm();
		finish();
		return true;
	} 
	public void setAlarm() {
		saveTime();
		saveAlarmDays();
		saveSwitch();
		acrr.setSimpleAlarm();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setAlarm();
		}
		return super.onKeyDown(keyCode, event);
	}
}
