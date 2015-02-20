package name.sunme.firstexecution;

import name.sunme.maindrawbar.MainActivity;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper; 
import name.sunme.seniorfit.UrlOpener;
import name.sunme.seniorfit.Utils;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setup4Activity extends Activity {
	private Button button_setup4_next;
	private TextView setup4_seledctedDay;
	private TextView setup4_atTime;
	private TextView setup4_goalMinutes;
	
	String TAG = "Setup4Activity";
	private DBHelper helper;
	private DBAdapter adapter;
	private Switch setup4_alarm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
		button_setup4_next = (Button)findViewById(R.id.button_setup4_next);
		setup4_seledctedDay = (TextView)findViewById(R.id.setup4_seledctedDay);
		
		setup4_atTime = (TextView)findViewById(R.id.setup4_atTime);
		setup4_goalMinutes = (TextView)findViewById(R.id.setup4_goalMinutes);
		
		
		setup4_alarm = (Switch)findViewById(R.id.setup4_alarm);
		
		setup4_alarm.setOnCheckedChangeListener(alarmSwitch_changelistener);
		button_setup4_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new UrlOpener(Setup4Activity.this, responsehandler).open(); 
			}
		});
		loadValues();
	}
	
	OnCheckedChangeListener alarmSwitch_changelistener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			adapter.put_setting("alarmSwitch", Boolean.toString(isChecked));
		}
	};
	
	private void loadValues() {
		loadDaynum();
		loadGoalMinutes();
		loadAtTime();
		loadAlarmSwitch();
	}
	private void loadAlarmSwitch() {
		String alarmSwitch = adapter.get_setting("alarmSwitch"); 
		if(alarmSwitch!=null) {
			setup4_alarm.setChecked(Boolean.parseBoolean(alarmSwitch));
		}
	}
	private void loadGoalMinutes() {
		String goalMinutes = adapter.get_setting("goalMinutes");
		if (goalMinutes!=null) { setup4_goalMinutes.setText("하루 "+goalMinutes+"분 이상"); }
		
	}
	private void loadAtTime() { 
		String atTimeHour = adapter.get_setting("atTimeHour");
		String atTimeMin = adapter.get_setting("atTimeMin");
		if (atTimeHour!=null&&atTimeMin!=null) { 
			String t = Utils.timeToString(Integer.parseInt(atTimeHour),Integer.parseInt(atTimeMin));
			setup4_atTime.setText(t);
		}
	}
	
	private void loadDaynum() {
		Boolean[] bool_alarm;
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false};
		for	(int i=0; i<7; i++) {
			if (adapter.get_setting("alarm_day"+i)!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(adapter.get_setting("alarm_day"+i));
			}
		}
		
		String seledctedDay = "";
		
		
		int n = Integer.parseInt(adapter.get_setting("dayN")); 
		
		Log.d(TAG, "선택된 요일은 총 "+n+"개입니다.");
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
		seledctedDay = seledctedDay + " 주 " + n + "회";
		Log.d(TAG, "만들어진 스트링 : "+seledctedDay);
		setup4_seledctedDay.setText(seledctedDay);
	}
	
	private final Handler downloadendhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0) {
				Log.d(TAG, "정상 다운로드 완료");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    			startActivity(intent);
    			Setup4Activity.this.finish();
			}
			if (msg.what == -1) {
				Log.d(TAG, "비정상 다운로드 완료");
            	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    			startActivity(intent);
    			Setup4Activity.this.finish();
			}  
		}
	};
	private final Handler responsehandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			Log.d(TAG, "got api json data");
			Utils.saveFitApiData(getApplicationContext(), result);
			Utils.showDialog_downloadResource(Setup4Activity.this, downloadendhandler);
			adapter.put_setting("firstsetting", "true");
		}
	};
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        Setup4Activity.this.finish();
        return true;
    }
}
