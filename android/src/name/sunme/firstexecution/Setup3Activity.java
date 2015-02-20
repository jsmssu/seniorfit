package name.sunme.firstexecution;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.Utils;
import name.sunme.setting.CustomDialogs;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class Setup3Activity extends Activity {

	private TextView setup3_goalMinutes;
	private TextView setup3_atTime;
	private Button button_setup3_next;

	private DBHelper helper;
	private DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		helper = new DBHelper(getApplicationContext());
		dbAdapter = new DBAdapter(getApplicationContext());
		setup3_goalMinutes = (TextView) findViewById(R.id.setup3_goalMinutes);
		setup3_atTime = (TextView) findViewById(R.id.setup3_atTime);

		setup3_goalMinutes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomDialogs.change_min(Setup3Activity.this, setup3_goalMinutes, null);
			}  

		});
		setup3_atTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				(new TimePickerDialog(Setup3Activity.this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						dbAdapter.put_setting("atTimeHour", Integer.toString(hourOfDay));
						dbAdapter.put_setting("atTimeMin", Integer.toString(minute));
						setup3_atTime.setText(Utils.timeToString(hourOfDay, minute));
					}
				}, 7, 0, false)).show();
				
			}
		});

		button_setup3_next = (Button) findViewById(R.id.button_setup3_next);
		button_setup3_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Setup4Activity.class);
				startActivity(intent);
				Setup3Activity.this.finish();
			}
		});
		loadValues();
	}

	private void loadValues() {
		changeGoalMinutes();
		changeAtTime();
	}
	private void changeGoalMinutes() {
		if (dbAdapter.get_setting("goalMinutes")!=null) { 
			setup3_goalMinutes.setText(dbAdapter.get_setting("goalMinutes"));
		}
	}
	private void changeAtTime() { 
		if (dbAdapter.get_setting("atTime")!=null) { 
			String t = Utils.timeToString(Integer.parseInt(dbAdapter.get_setting("atTimeHour")),Integer.parseInt(dbAdapter.get_setting("atTimeMin")));
			setup3_atTime.setText(t); 
		}
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(),
				Setup2Activity.class);
		startActivity(intent);
		Setup3Activity.this.finish();
		return true;
	}
}
