package name.sunme.firstexecution;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.Utils;

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

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
	private DBAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		setup3_goalMinutes = (TextView) findViewById(R.id.setup3_goalMinutes);
		setup3_atTime = (TextView) findViewById(R.id.setup3_atTime);

		setup3_goalMinutes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

				final View layout = inflater.inflate(
						R.layout.custom_numberpicker_dialog,
						(ViewGroup) findViewById(R.id.layout_root));

				final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
						Setup3Activity.this);
				InputDialogbuilder.setTitle("분 입력");
				InputDialogbuilder.setView(layout);
				final NumberPicker dialogNumberPicker = (NumberPicker) layout
						.findViewById(R.id.customNumberPicker);
				dialogNumberPicker.setMinValue(1);
				dialogNumberPicker.setMaxValue(200);

				InputDialogbuilder.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker.getValue());
								adapter.put_setting("goalMinutes", myInputText);
								setup3_goalMinutes.setText(myInputText);
							}
						});
				InputDialogbuilder.setNegativeButton("취소",
						null);
				final AlertDialog InputDialog = InputDialogbuilder.create();
				InputDialog.show();

			}  

		});
		setup3_atTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				(new TimePickerDialog(Setup3Activity.this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						adapter.put_setting("atTimeHour", Integer.toString(hourOfDay));
						adapter.put_setting("atTimeMin", Integer.toString(minute));
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
		if (adapter.get_setting("goalMinutes")!=null) { 
			setup3_goalMinutes.setText(adapter.get_setting("goalMinutes"));
		}
	}
	private void changeAtTime() { 
		if (adapter.get_setting("atTime")!=null) { 
			String t = Utils.timeToString(Integer.parseInt(adapter.get_setting("atTimeHour")),Integer.parseInt(adapter.get_setting("atTimeMin")));
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
