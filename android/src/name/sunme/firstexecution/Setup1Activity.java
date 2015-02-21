package name.sunme.firstexecution;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper; 
import name.sunme.setting.CustomDialogs;
import name.sunme.setting.SettingProfileActivity;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Setup1Activity extends Activity {

	String TAG = "Setup1Activity";
	private TextView setup1_age;
	private TextView setup1_height; 
	private TextView setup1_weight; 
	
	
	private RadioButton setup1_radio_woman;
	private RadioButton setup1_radio_man;
	private String imageview_sex;
	private Button button_setup1_next;
	private DBHelper helper;
	private DBAdapter adapter;
	
	private RadioGroup setup1_radio_sex;     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext()); 
		
		
		
		
		setup1_age = (TextView)findViewById(R.id.setup1_age); 
		setup1_height = (TextView)findViewById(R.id.setup1_height);
		setup1_weight = (TextView)findViewById(R.id.setup1_weight);
		button_setup1_next = (Button)findViewById(R.id.button_setup1_next); 
		setup1_radio_woman = (RadioButton)findViewById(R.id.setup1_radio_woman);
		setup1_radio_man = (RadioButton)findViewById(R.id.setup1_radio_man);
		setup1_radio_sex = (RadioGroup)findViewById(R.id.setup1_radio_sex);
		
		
		loadValues();
		
		setup1_age.setOnClickListener(age_clicklistener);
		setup1_height.setOnClickListener(height_clicklistener);  
		setup1_weight.setOnClickListener(weight_clicklistenr);  
		setup1_radio_sex.setOnCheckedChangeListener(sexradio_changelistener); 
		button_setup1_next.setOnClickListener(next_clicklistener);  
		
	}
	private void loadValues() {
		loadAge();
		loadHeight();
		loadHeight();
		loadWeight();
		loadSex();
	}
	void loadAge() {
		if (adapter.get_setting("age")!=null) {
			setup1_age.setText(adapter.get_setting("age")); 
		}
	}
	void loadHeight() {
		if (adapter.get_setting("height")!=null) {
			setup1_height.setText(adapter.get_setting("height")); 
		}
	}
	void loadWeight() {
		if (adapter.get_setting("weight")!=null) {
			setup1_weight.setText(adapter.get_setting("weight")); 
		}
	}
	void loadSex() {
		if (adapter.get_setting("sex")!=null) {
			if (adapter.get_setting("sex").equals("woman")) {
				setup1_radio_woman.setChecked(true);
			}
			if (adapter.get_setting("sex").equals("man")) {
				setup1_radio_man.setChecked(true);
			} 
		}
	}
	OnClickListener next_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {  
			Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
			startActivity(intent);
			Setup1Activity.this.finish();
		}
	};
	OnCheckedChangeListener sexradio_changelistener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == R.id.setup1_radio_woman) {
				adapter.put_setting("sex", "woman");
				Log.d(TAG, "여자 선택");
			}
			if (checkedId == R.id.setup1_radio_man) {
				adapter.put_setting("sex", "man");
				Log.d(TAG, "남자 선택");
			} 
		}
	};
	OnClickListener height_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomDialogs.change_height(Setup1Activity.this, setup1_height, null);
		}
	};
	OnClickListener age_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			CustomDialogs.change_age(Setup1Activity.this, setup1_age);
		}
	};	
	
	OnClickListener weight_clicklistenr = new OnClickListener() {
		@Override
		public void onClick(View v) { 
			CustomDialogs.change_weight(Setup1Activity.this, setup1_weight, null); 
		}
	};
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Setup1Activity.this.finish();
        return true;
    }
	
}
