package name.sunme.maindrawbar;

import name.sunme.functionactivity.ChooseProgramActivity;
import name.sunme.functionactivity.MyWorkingoutActivity;
import name.sunme.functionactivity.OtherProgramActivity;
 


import com.example.seniorfit.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class StretchingActivity extends Activity {
	String TAG = "StretchingActivity";
	private Button stretcing_otherprogram;
	private Button stretcing_chooseprogram;
	private ImageView stretcing_startmyworkingout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stretching);
		Log.d(TAG, "set actioinbar");
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
        Log.d(TAG, "set actioinbar");
        stretcing_otherprogram = (Button)findViewById(R.id.stretcing_otherprogram);
        stretcing_chooseprogram = (Button)findViewById(R.id.stretcing_chooseprogram);
        stretcing_startmyworkingout = (ImageView)findViewById(R.id.stretcing_startmyworkingout);
        stretcing_otherprogram.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				Log.d(TAG, "button_otherprogram");
				Intent intent = new Intent(getApplicationContext(), OtherProgramActivity.class);
				startActivity(intent);
				finish();
			}
		});
        stretcing_chooseprogram.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				Log.d(TAG, "button_chooseprogram");
				Intent intent = new Intent(getApplicationContext(), ChooseProgramActivity.class);
				startActivity(intent);
				finish();
			}
		});
        stretcing_startmyworkingout.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				Log.d(TAG, "button_chooseprogram");
				Intent intent = new Intent(getApplicationContext(), MyWorkingoutActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
