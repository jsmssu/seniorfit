package name.sunme.setting;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.maindrawbar.R.id;
import name.sunme.maindrawbar.R.layout;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

public class SettingAlarmActivity extends Activity {
	String TAG = "SettingAlarmActivity";
	private ImageView[] setting_alarm; 
	private Boolean[] bool_alarm;
	int[][] img_day;
			 
	private TextView setting_seledctedDay;
	private DBAdapter adapter; 
	private Switch setting_alarm_switch;
	
	private PendingIntent pendingIntent;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_alarm);
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		adapter = new DBAdapter(getApplicationContext());
		
		
		
		Intent alarmIntent = new Intent(SettingAlarmActivity.this, AlarmReceiver.class);
	    pendingIntent = PendingIntent.getBroadcast(SettingAlarmActivity.this, 0, alarmIntent, 0);
	    start();
		
		
		
		
		
		
		
		
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false};
		img_day = new int[][]{
					{drawable.setup_mon_black,drawable.setup_mon_white},
					{drawable.setup_tue_black,drawable.setup_tue_white},
					{drawable.setup_wed_black,drawable.setup_wed_white},
					{drawable.setup_thu_black,drawable.setup_thu_white},
					{drawable.setup_fri_black,drawable.setup_fri_white},
					{drawable.setup_sat_black,drawable.setup_sat_white},
					{drawable.setup_sun_black,drawable.setup_sun_white}
		};		
		setting_alarm = new ImageView[] {
                (ImageView)findViewById(R.id.setting_alarm_Monday),
                (ImageView)findViewById(R.id.setting_alarm_Tuesday),
                (ImageView)findViewById(R.id.setting_alarm_Wednesday),
                (ImageView)findViewById(R.id.setting_alarm_Thursday),
                (ImageView)findViewById(R.id.setting_alarm_Friday),
                (ImageView)findViewById(R.id.setting_alarm_Saturday),
                (ImageView)findViewById(R.id.setting_alarm_Sunday)
        };
		
		
		
		
		setting_alarm_switch = (Switch)findViewById(R.id.setting_alarm_switch);
		setting_seledctedDay = (TextView)findViewById(R.id.setting_seledctedDay);
        
        setting_alarm[0].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(0);chageDaynum();}});
        setting_alarm[1].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(1);chageDaynum();}});
        setting_alarm[2].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(2);chageDaynum();}});
        setting_alarm[3].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(3);chageDaynum();}});
        setting_alarm[4].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(4);chageDaynum();}});
        setting_alarm[5].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(5);chageDaynum();}});
        setting_alarm[6].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(6);chageDaynum();}});

        
        setting_alarm_switch.setOnCheckedChangeListener(alarmSwitch_changelistener);

        loadDayValues();
        
        
	}
	OnCheckedChangeListener alarmSwitch_changelistener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			adapter.put_setting("alarmSwitch", Boolean.toString(isChecked));
		}
	};
	private void loadDaycheck() {
		for	(int i=0; i<7; i++) {
			if (adapter.get_setting("alarm_day"+i)!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(adapter.get_setting("alarm_day"+i));
				setDayButton(i);
			}
		}
	}
	private void loadAlarmSwitch() {
		String alarmSwitch = adapter.get_setting("alarmSwitch"); 
		if(alarmSwitch!=null) {
			setting_alarm_switch.setChecked(Boolean.parseBoolean(alarmSwitch));
		}
	}
	private void chageDaynum() {
		Boolean[] bool_alarm;
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false};
		int n=0;
		for	(int i=0; i<7; i++) {
			if (adapter.get_setting("alarm_day"+i)!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(adapter.get_setting("alarm_day"+i));
				if(bool_alarm[i]==true) {
					n = n + 1;
				}
			}
		}
		
		String seledctedDay = "";
		
		 
		if (n > 0) {
			if (bool_alarm[0]) seledctedDay = seledctedDay + "월,";
			if (bool_alarm[1]) seledctedDay = seledctedDay + "화,";
			if (bool_alarm[2]) seledctedDay = seledctedDay + "수,";
			if (bool_alarm[3]) seledctedDay = seledctedDay + "목,";
			if (bool_alarm[4]) seledctedDay = seledctedDay + "금,";
			if (bool_alarm[5]) seledctedDay = seledctedDay + "토,";
			if (bool_alarm[6]) seledctedDay = seledctedDay + "일,";
			seledctedDay = seledctedDay.substring(0, seledctedDay.length()-1);
		}
		setting_seledctedDay.setText(seledctedDay);
	}
	private void loadDayValues() {
		loadDaycheck();
		chageDaynum();
		loadAlarmSwitch();
	}
	private void toggleDay(int day) {
		bool_alarm[day] = !bool_alarm[day];
		adapter.put_setting("alarm_day"+day, Boolean.toString(bool_alarm[day]));
		setDayButton(day);
	}
	private void setDayButton(int n) {
		if (bool_alarm[n] == true) {setting_alarm[n].setImageResource(img_day[n][1]);}
		else if (bool_alarm[n] == false) {setting_alarm[n].setImageResource(img_day[n][0]);}
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
	
	
	public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }
}
