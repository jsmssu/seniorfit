package name.sunme.setting;

import name.sunme.firstexecution.Setup3Activity;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import name.sunme.seniorfit.DBAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SettingGoalActivity extends Activity {

	LinearLayout settinggoal_boxnumber;
	LinearLayout settinggoal_boxmin;
	TextView settinggoal_number;
	TextView settinggoal_min;
	
	private DBAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_goal);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
        adapter = new DBAdapter(getApplicationContext());
        
        
        settinggoal_boxnumber = (LinearLayout)findViewById(R.id.settinggoal_boxnumber);
        settinggoal_boxmin = (LinearLayout)findViewById(R.id.settinggoal_boxmin);
        settinggoal_number = (TextView)findViewById(R.id.settinggoal_number);
        settinggoal_min = (TextView)findViewById(R.id.settinggoal_min);
        
        
        String goalMinutes = adapter.get_setting("goalMinutes");
        String dayN = adapter.get_setting("dayN");
        if (goalMinutes != null) { settinggoal_min.setText(goalMinutes+"분"); }
        if (dayN != null) { settinggoal_number.setText(dayN+"회"); }  
        
        
        settinggoal_boxmin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

				final View layout = inflater.inflate(R.layout.custom_numberpicker_dialog,null);

				final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
						SettingGoalActivity.this);
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
								settinggoal_min.setText(myInputText+"분");
							}
						});
				InputDialogbuilder.setNegativeButton("취소",
						null);
				final AlertDialog InputDialog = InputDialogbuilder.create();
				InputDialog.show();
				
			}
		});
        settinggoal_boxnumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

				final View layout = inflater.inflate(R.layout.custom_numberpicker_dialog,null);

				final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
						SettingGoalActivity.this);
				InputDialogbuilder.setTitle("주 몇일 운동하겠습니까");
				InputDialogbuilder.setView(layout);
				final NumberPicker dialogNumberPicker = (NumberPicker) layout
						.findViewById(R.id.customNumberPicker);
				dialogNumberPicker.setMinValue(1);
				dialogNumberPicker.setMaxValue(7);

				InputDialogbuilder.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker.getValue());
								adapter.put_setting("dayN", myInputText);
								settinggoal_number.setText(myInputText+"회");
							}
						});
				InputDialogbuilder.setNegativeButton("취소",
						null);
				final AlertDialog InputDialog = InputDialogbuilder.create();
				InputDialog.show();
				
			}
		});
        
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }
}
