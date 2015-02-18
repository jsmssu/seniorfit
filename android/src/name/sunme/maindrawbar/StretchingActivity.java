package name.sunme.maindrawbar;

import name.sunme.functionactivity.ChooseProgramActivity;
import name.sunme.functionactivity.FitDetail_MainMenuTitleActivity;
import name.sunme.functionactivity.MyWorkingoutActivity;
import name.sunme.functionactivity.OtherProgramActivity;
 


import name.sunme.functionactivity.OtherProgramItem;
import name.sunme.functionactivity.VideoDetailActivity;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.GlobalData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class StretchingActivity extends Activity {
	String TAG = "StretchingActivity";
	private Button stretcing_otherprogram;
	private Button stretcing_myworkingout; 
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
        stretcing_myworkingout = (Button)findViewById(R.id.stretcing_myworkingout);
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
        stretcing_myworkingout.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				Log.d(TAG, "button_chooseprogram");
				Intent intent = new Intent(getApplicationContext(), MyWorkingoutActivity.class);
				startActivity(intent);
				finish();
			}
		});
		stretcing_startmyworkingout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "button_chooseprogram");
				OtherProgramItem[] opis = GlobalData
						.getOtherProgramItem(getApplicationContext());
				for (int i = 0; i < opis.length; i++) {
					if (opis[i].isTodays == true) {
						Intent intent = new Intent(getApplicationContext(),
								VideoDetailActivity.class);
						intent.putExtra("subMenuIds",
								opis[i].getJson_submenuids());
						startActivity(intent);
						finish();
					}
				}
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
