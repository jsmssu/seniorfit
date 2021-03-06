package name.sunme.firstexecution;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.workingout.FitDetail_MainMenuTitleActivity;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setup2Activity extends Activity {
	String TAG = "Setup2Activity";
	private ImageView[] setup_alarm; 
	private Boolean[] bool_alarm;
	int[][] img_day;
			
	private Button button_setup2_next;
	private TextView setup2_daynum; 
	private DBAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2); 
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        adapter = new DBAdapter(getApplicationContext()); 
		
		bool_alarm = new Boolean[]{false,false,false,false,false,false,false};
		img_day = new int[][]{
				{drawable.setup_sun_white, drawable.setup_sun_black},
				{drawable.setup_mon_white, drawable.setup_mon_black},
				{drawable.setup_tue_white, drawable.setup_tue_black},
				{drawable.setup_wed_white, drawable.setup_wed_black},
				{drawable.setup_thu_white, drawable.setup_thu_black},
				{drawable.setup_fri_white, drawable.setup_fri_black},
				{drawable.setup_sat_white, drawable.setup_sat_black}
		};		
		setup_alarm = new ImageView[] {
				(ImageView)findViewById(R.id.setup2_alarm_Sunday),
				(ImageView)findViewById(R.id.setup2_alarm_Monday),
				(ImageView)findViewById(R.id.setup2_alarm_Tuesday),
				(ImageView)findViewById(R.id.setup2_alarm_Wednesday),
				(ImageView)findViewById(R.id.setup2_alarm_Thursday),
				(ImageView)findViewById(R.id.setup2_alarm_Friday),
				(ImageView)findViewById(R.id.setup2_alarm_Saturday)
		};  
		button_setup2_next = (Button)findViewById(R.id.setup2_button_next);
		setup2_daynum = (TextView)findViewById(R.id.setup2_daynum);
		
		
		loadDayValues();
		
		
		
		
		setup_alarm[0].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(0);chageDaynum();}});
		setup_alarm[1].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(1);chageDaynum();}});
		setup_alarm[2].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(2);chageDaynum();}});
		setup_alarm[3].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(3);chageDaynum();}});
		setup_alarm[4].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(4);chageDaynum();}});
		setup_alarm[5].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(5);chageDaynum();}});
		setup_alarm[6].setOnClickListener(new OnClickListener() { public void onClick(View v) { toggleDay(6);chageDaynum();}}); 
		button_setup2_next.setOnClickListener(next_clicklistener); 
		
		
		
	}
	OnClickListener next_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			for	(int i=0; i<7; i++) {
				adapter.put_setting("alarm_day"+i, Boolean.toString(bool_alarm[i]));
			}
			String ns = adapter.get_setting("dayN");
			if (ns == null) {
				adapter.put_setting("dayN", "0");
			}
			Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
			startActivity(intent);
			finish();
		}
	};
	private void loadDayValues() {
		loadDaycheck();
		chageDaynum();
	}
	private void loadDaycheck() {
		for	(int i=0; i<7; i++) {
			if (adapter.get_setting("alarm_day"+i)!=null) { 
				bool_alarm[i] = Boolean.parseBoolean(adapter.get_setting("alarm_day"+i));
				setDayButton(i);
			}
		}
	}
	private void chageDaynum() {
		int n = 0;
		for(int i=0; i<7; i++) {
			if (bool_alarm[i]==true) {
				n = n + 1;
			}
		}
		setup2_daynum.setText("�� "+n+"ȸ");
		adapter.put_setting("dayN", n+"");
	}
	private void toggleDay(int day) {
		bool_alarm[day] = !bool_alarm[day];
		adapter.put_setting("alarm_day"+day, Boolean.toString(bool_alarm[day]));
		setDayButton(day);
	}
	private void setDayButton(int n) {
		if (bool_alarm[n] == true) {setup_alarm[n].setImageResource(img_day[n][1]);}
		else if (bool_alarm[n] == false) {setup_alarm[n].setImageResource(img_day[n][0]);}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        Setup2Activity.this.finish();
        return false;
    }
	@Override
	protected void onResume() {
		this.overridePendingTransition(0,0); 
		super.onResume();
	}
}
