package name.sunme.setting;

import name.sunme.firstexecution.Setup3Activity;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SettingBMIActivity extends Activity {

	String TAG = "SettingBMIActivity";
	private LinearLayout settingbmi_sexbox;
	private LinearLayout settingbmi_heightbox;
	private LinearLayout settingbmi_weightbox;
	private LinearLayout settingbmi_agebox;
	
	private LinearLayout settingbmi_bmicolor;

	private TextView settingbmi_bmitext;
	
	private TextView settingbmi_age;
	private TextView settingbmi_height;
	private TextView settingbmi_weight;
	private TextView settingbmi_sex;

	private DBHelper helper;
	private DBAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_bmi);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());

		settingbmi_sexbox = (LinearLayout) findViewById(R.id.settingbmi_sexbox);
		settingbmi_heightbox = (LinearLayout) findViewById(R.id.settingbmi_heightbox);
		settingbmi_weightbox = (LinearLayout) findViewById(R.id.settingbmi_weightbox);
		settingbmi_agebox = (LinearLayout) findViewById(R.id.settingbmi_agebox);

		settingbmi_bmicolor = (LinearLayout) findViewById(R.id.settingbmi_bmicolor);
		settingbmi_bmitext = (TextView) findViewById(R.id.settingbmi_bmitext);
		
		settingbmi_age = (TextView) findViewById(R.id.settingbmi_age);
		settingbmi_height = (TextView) findViewById(R.id.settingbmi_height);
		settingbmi_weight = (TextView) findViewById(R.id.settingbmi_weight);
		settingbmi_sex = (TextView) findViewById(R.id.settingbmi_sex);

		settingbmi_agebox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

				final View layout = inflater.inflate(
						R.layout.custom_numberpicker_dialog,
						(ViewGroup) findViewById(R.id.layout_root));

				final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
						SettingBMIActivity.this);
				InputDialogbuilder.setTitle("나이 입력");
				InputDialogbuilder.setView(layout);
				final NumberPicker dialogNumberPicker = (NumberPicker) layout
						.findViewById(R.id.customNumberPicker);
				dialogNumberPicker.setMinValue(40);
				dialogNumberPicker.setMaxValue(150);

				InputDialogbuilder.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker.getValue());
								adapter.put_setting("age", myInputText);
								loadAge();
							}
						});
				InputDialogbuilder.setNegativeButton("취소", null);
				InputDialogbuilder.create().show();
			}
		});
		settingbmi_heightbox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

				final View layout = inflater.inflate(
						R.layout.custom_numberpicker_dialog,
						(ViewGroup) findViewById(R.id.layout_root));

				final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
						SettingBMIActivity.this);
				InputDialogbuilder.setTitle("키 입력");
				InputDialogbuilder.setView(layout);
				final NumberPicker dialogNumberPicker = (NumberPicker) layout
						.findViewById(R.id.customNumberPicker);
				dialogNumberPicker.setMinValue(100);
				dialogNumberPicker.setMaxValue(200);

				InputDialogbuilder.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker.getValue());
								adapter.put_setting("height", myInputText);
								loadHeight();
							}
						});
				InputDialogbuilder.setNegativeButton("취소", null);
				InputDialogbuilder.create().show();
			}
		});
		settingbmi_weightbox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

				final View layout = inflater.inflate(
						R.layout.custom_numberpicker_dialog,
						(ViewGroup) findViewById(R.id.layout_root));

				final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
						SettingBMIActivity.this);
				InputDialogbuilder.setTitle("무게 입력");
				InputDialogbuilder.setView(layout);
				final NumberPicker dialogNumberPicker = (NumberPicker) layout
						.findViewById(R.id.customNumberPicker);
				dialogNumberPicker.setMinValue(40);
				dialogNumberPicker.setMaxValue(200);

				InputDialogbuilder.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker.getValue());
								adapter.put_setting("weight", myInputText);
								loadWeight();
							}
						});
				InputDialogbuilder.setNegativeButton("취소", null);
				InputDialogbuilder.create().show();
			}
		});
		loadValues();
	}
	 
	float t_weight;
	float t_height;
	private void loadValues() { 
		loadAge();
		loadSex();
		loadHeight();
		loadWeight();
	} 
	private void loadAge() {
		String age = adapter.get_setting("age");
		if (age != null) {settingbmi_age.setText(age+"세");}
	}
	private void loadSex() {
		String sex = adapter.get_setting("sex");
		if (sex.equals("woman")) {settingbmi_sex.setText("여성");}
		if (sex.equals("man")) {settingbmi_sex.setText("남성");}
	}
	private void loadHeight() {
		String height = adapter.get_setting("height");
		if (height != null) {settingbmi_height.setText(height+"cm");t_height = Float.parseFloat(height);}
		setBMI();
	}
	private void loadWeight() {
		String weight = adapter.get_setting("weight");
		if (weight != null) {settingbmi_weight.setText(weight+"kg");t_weight = Float.parseFloat(weight);}
		setBMI();
	}
	private void setBMI() {
		
		try {
			float bmi = t_weight/(t_height/100)/(t_height/100); 
			Log.d(TAG, "bmi : "+ bmi);
			String bmitext = String.format("%.2f", bmi)+"(";
			if (bmi<=18.5) {
				bmitext = bmitext + "저체중";
				settingbmi_bmicolor.setBackgroundColor(Color.rgb(0xD4, 0xD4, 0xD4));
			} else if (bmi<23) {
				bmitext = bmitext + "정상체중";
				settingbmi_bmicolor.setBackgroundColor(Color.rgb(0x90, 0xE3, 0xCB));
			} else if (bmi<25) {
				bmitext = bmitext + "과체중";
				settingbmi_bmicolor.setBackgroundColor(Color.rgb(0x37, 0xB7, 0xBB));
			} else if (bmi<30) {
				bmitext = bmitext + "비만";
				settingbmi_bmicolor.setBackgroundColor(Color.rgb(0x4C, 0x3D, 0x8F)); 
			} else {
				bmitext = bmitext + "고도비만";
				settingbmi_bmicolor.setBackgroundColor(Color.rgb(0xC8, 0x30, 0x44)); 
			}
			bmitext = bmitext+")";
			settingbmi_bmitext.setText(bmitext);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
