package name.sunme.setting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private boolean[] bool_alarm = new boolean[7];
	PendingIntent pendingIntent;
	int hourOfDay;
	int minute;
	
	boolean alarmSwitch;
	
	public AlarmController(Context context) {
		this.context = context;
		dbAdapter = new DBAdapter(context); 
	}

	void loadAlarmset() {
		
		String alarmSwitch_str = dbAdapter.get_setting("alarmSwitch");
		if (alarmSwitch_str == null || alarmSwitch_str.equals(Boolean.toString(false))) {
			alarmSwitch = false;
		} else {
			alarmSwitch = true;
		}
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
			Log.d(TAG,"time : h "+hourOfDay+", m: "+minute);
		}
	}
	
	String getDayString() {
		String seledctedDay = "";
		if (bool_alarm[0]) seledctedDay = seledctedDay + "��,";
		if (bool_alarm[1]) seledctedDay = seledctedDay + "��,";
		if (bool_alarm[2]) seledctedDay = seledctedDay + "ȭ,";
		if (bool_alarm[3]) seledctedDay = seledctedDay + "��,";
		if (bool_alarm[4]) seledctedDay = seledctedDay + "��,";
		if (bool_alarm[5]) seledctedDay = seledctedDay + "��,";
		if (bool_alarm[6]) seledctedDay = seledctedDay + "��,";
		if (seledctedDay.length()>0) seledctedDay = seledctedDay.substring(0, seledctedDay.length()-1);
		else seledctedDay = "(���� ���õ��� ����)";
		return seledctedDay;
	}
	public void setSimpleAlarm(){
		loadAlarmset();
		
		manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		alarmIntent = new Intent(context, AlarmReceiver.class);
		
		for(int i=0;i<7;i++) {
			pendingIntent = PendingIntent.getBroadcast(context, i, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT); 
			
			if (alarmSwitch==false) {  
				manager.cancel(pendingIntent);	 
			} else {  
				if(bool_alarm[i] == true) {
					Calendar calendar = Calendar.getInstance();
					Calendar now = Calendar.getInstance();
			        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			        calendar.set(Calendar.MINUTE, minute);
			        
			        SimpleDateFormat format = new SimpleDateFormat("yyyy�� MM�� dd�� hh�� mm�� ss��");
			        Log.d(TAG, "now.before(calendar)" + " now:"+format.format(now.getTime())+", calendar"+format.format(calendar.getTime()));
			        Log.d(TAG, ""+now.before(calendar));
			        while(now.before(calendar)) {
			        	calendar.add(Calendar.DATE, 7);
			        } 
			        Toast.makeText(context, getDayString() + " �˶� "+calendar.get(Calendar.HOUR_OF_DAY)+"�� "+calendar.get(Calendar.MINUTE)+"��", 3).show();
			        
			        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000*7, pendingIntent);
				} else {
					manager.cancel(pendingIntent);	
				} 
			}
		} 
	}  
}
