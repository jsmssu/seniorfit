package name.sunme.setting;

import java.util.Calendar;

import name.sunme.seniorfit.DBAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmController {
	String TAG = "AlarmController";
	Context context;
	AlarmManager manager;
	Intent alarmIntent;
	DBAdapter dbAdapter;
	private Boolean[] bool_alarm;
	PendingIntent[] pendingIntent;
	int hourOfDay;
	int minute;
	public AlarmController(Context context) {
		this.context = context;
		manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmIntent = new Intent(context, AlarmReceiver.class);
		dbAdapter = new DBAdapter(context);
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false};
		pendingIntent = new PendingIntent[7];
		for(int i=0; i<7; i++) {
			pendingIntent[i] = PendingIntent.getBroadcast(context, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
	}

	void loadAlarmset() {
		for(int i=0; i<7; i++) {
			String alarm_str = dbAdapter.get_setting("alarm_day"+i);
			if (alarm_str!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(alarm_str);
			}
		}
		String hour_str = dbAdapter.get_setting("atTimeHour");
		String min_str = dbAdapter.get_setting("atTimeMin");
		if (hour_str!=null && min_str!=null) {
			hourOfDay = Integer.parseInt(hour_str);
			minute = Integer.parseInt(min_str); 
		}
	}
	public void setSimpleAlarm(){
		String alarmSwitch_str = dbAdapter.get_setting("alarmSwitch");
		if (alarmSwitch_str==null || Boolean.parseBoolean(alarmSwitch_str)==false) {
			for(int i=0; i<7; i++) {
				resetAlarm(i);
			}
		} else {
			loadAlarmset();
			for(int i=0; i<7; i++) {
				Log.d(TAG, i + " boolean : "+bool_alarm[i]);
				if(bool_alarm[i]==true) {
					setAlarm(i, hourOfDay, minute);
				} else {
					resetAlarm(i);
				}
			}
		}
	}
	public void stopAlarm(){
		for(int i=0; i<7; i++) {
			resetAlarm(i);
		}
	}
	
	
	public void setAlarm(int idx, int hour, int min) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, idx);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[idx]);
	}
	public void resetAlarm(int idx) { 
        manager.cancel(pendingIntent[idx]);
    }
	
	
}
