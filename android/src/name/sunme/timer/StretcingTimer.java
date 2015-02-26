package name.sunme.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import name.sunme.seniorfit.DBAdapter;
import android.content.Context;
import android.util.Log;

public class StretcingTimer {
	static String TAG = "StretcingTimer";
	public static Context context;
	public static boolean threadstopped = true;
	public static String db_str_stretcing = "stt_";
	public static SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
	public static Date today = null;
	
	public static DBAdapter dbAdapter;
	
	public StretcingTimer(Context context) {
		this.context = context;
		dbAdapter = new DBAdapter(context);
	}

	public static String getKey(Date date) {
		return db_str_stretcing + dbformat.format(date);
	}
	public static String getKey(Calendar date) {
		return db_str_stretcing + dbformat.format(date.getTime());
	}
	public static int getValue(String key) {
		int result = 0;
		String value = dbAdapter.get_setting(key);
        if(value!=null) {
    		result = Integer.parseInt(value);
    	}
		return result;
	}
	public static Thread thread = new Thread(new Runnable() {
		
		
		DBAdapter dbAdapter;	
		
		
		String key = null;
		String value = null;
        public void run() {
        	int sec = 0;
        	threadstopped = false;
        	dbAdapter = new DBAdapter(context);
        	
            while (!threadstopped) {                
                try {
                	today = null;
                	key = null;
                    Thread.sleep(5000);
                } catch (InterruptedException ie) { 
                    ie.printStackTrace();
                }
                today = new Date();
                key = getKey(today);
                sec = getValue(key);
                sec = sec + 5;
        		dbAdapter.put_setting(key, Integer.toString(sec));
        		Log.d(TAG, "°È´ÂÁß "+key + " :  "+sec);
            }
        }
    });
	
	public void start() {
		if (!thread.isAlive()) {
			thread.start();
		}
	}
	public void stop() {
		threadstopped = true;
		if (thread.isAlive()) {
			thread.interrupt();
		}
	}
}
