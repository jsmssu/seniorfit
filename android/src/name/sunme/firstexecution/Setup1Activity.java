package name.sunme.firstexecution;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper; 
import name.sunme.setting.SettingProfileActivity;

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

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
		
		Log.d(TAG, helper.toString());
		Log.d(TAG, adapter.toString());
		setup1_age = (TextView)findViewById(R.id.setup1_age); 
		setup1_height = (TextView)findViewById(R.id.setup1_height);
		setup1_weight = (TextView)findViewById(R.id.setup1_weight);
		button_setup1_next = (Button)findViewById(R.id.button_setup1_next);
		
		setup1_radio_woman = (RadioButton)findViewById(R.id.setup1_radio_woman);
		setup1_radio_man = (RadioButton)findViewById(R.id.setup1_radio_man);
		setup1_radio_sex = (RadioGroup)findViewById(R.id.setup1_radio_sex);
		
		setup1_age.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                
                final View layout = inflater.inflate(R.layout.custom_numberpicker_dialog,(ViewGroup) findViewById(R.id.layout_root));
 
                    
                final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(Setup1Activity.this);
                InputDialogbuilder.setTitle("나이 입력");
                InputDialogbuilder.setView(layout);
                final NumberPicker dialogNumberPicker = (NumberPicker)layout.findViewById(R.id.customNumberPicker);
                dialogNumberPicker.setMinValue(40);
                dialogNumberPicker.setMaxValue(100); 

                InputDialogbuilder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String myInputText = Integer.toString(dialogNumberPicker.getValue());
                        adapter.put_setting("age", myInputText);
                        setup1_age.setText(myInputText);
                    }
                });
                InputDialogbuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog InputDialog = InputDialogbuilder.create();
                InputDialog.show();
			}
		});
		setup1_height.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                
                final View layout = inflater.inflate(R.layout.custom_numberpicker_dialog,(ViewGroup) findViewById(R.id.layout_root));
 
                    
                final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(Setup1Activity.this);
                InputDialogbuilder.setTitle("키 입력");
                InputDialogbuilder.setView(layout);
                final NumberPicker dialogNumberPicker = (NumberPicker)layout.findViewById(R.id.customNumberPicker);
                dialogNumberPicker.setMinValue(100);
                dialogNumberPicker.setMaxValue(200); 

                InputDialogbuilder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String myInputText = Integer.toString(dialogNumberPicker.getValue());
                        adapter.put_setting("height", myInputText);
                        setup1_height.setText(myInputText);
                    }
                });
                InputDialogbuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog InputDialog = InputDialogbuilder.create();
                InputDialog.show();
			}
		});
		setup1_weight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
		        
                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                
                final View layout = inflater.inflate(R.layout.custom_numberpicker_dialog,(ViewGroup) findViewById(R.id.layout_root));
 
                    
                final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(Setup1Activity.this);
                InputDialogbuilder.setTitle("무게 입력");
                InputDialogbuilder.setView(layout);
                final NumberPicker dialogNumberPicker = (NumberPicker)layout.findViewById(R.id.customNumberPicker);
                dialogNumberPicker.setMinValue(30);
                dialogNumberPicker.setMaxValue(200); 

                InputDialogbuilder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String myInputText = Integer.toString(dialogNumberPicker.getValue());
                        adapter.put_setting("weight", myInputText);
                        setup1_weight.setText(myInputText);
                    }
                });
                InputDialogbuilder.setNegativeButton("취소",null);
                final AlertDialog InputDialog = InputDialogbuilder.create();
                InputDialog.show();
			}
		});
		
		
		setup1_radio_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
				// TODO Auto-generated method stub
				
			}
		});
		
		
		button_setup1_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {  
				Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
				startActivity(intent);
				Setup1Activity.this.finish();
			}
		});  
		setInputValues();
	}
	private void setInputValues() {
		
		if (adapter.get_setting("age")!=null) {
			setup1_age.setText(adapter.get_setting("age"));
			Log.d(TAG, "GetValue, "+"age :"+adapter.get_setting("age"));
		}
		if (adapter.get_setting("height")!=null) {
			setup1_height.setText(adapter.get_setting("height"));
			Log.d(TAG, "GetValue, "+"height :"+adapter.get_setting("height"));
		}
		if (adapter.get_setting("weight")!=null) {
			setup1_weight.setText(adapter.get_setting("weight"));
			Log.d(TAG, "GetValue, "+"weight :"+adapter.get_setting("weight"));
		}
		if (adapter.get_setting("sex")!=null) {
			if (adapter.get_setting("sex").equals("woman")) {
				setup1_radio_woman.setChecked(true);
			}
			if (adapter.get_setting("sex").equals("man")) {
				setup1_radio_man.setChecked(true);
			}
			Log.d(TAG, "GetValue, "+"sex :"+adapter.get_setting("sex"));
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Setup1Activity.this.finish();
        return true;
    }
	
}
