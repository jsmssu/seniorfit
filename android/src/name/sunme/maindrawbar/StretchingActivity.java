package name.sunme.maindrawbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.functionactivity.ChooseProgramActivity;
import name.sunme.functionactivity.FitDetail_MainMenuTitleActivity;
import name.sunme.functionactivity.MyWorkingoutActivity;
import name.sunme.functionactivity.OtherProgramActivity;
 


import name.sunme.functionactivity.OtherProgramItem;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.GlobalData;
import name.sunme.video.VideoDetailActivity;
import name.sunme.video.VideoShowActivity;
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
import android.widget.VideoView;

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
				OtherProgramItem[] opis = GlobalData.getOtherProgramItem(getApplicationContext());
				DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
				
				String myProgram = dbAdapter.get_setting("myProgram");
				Log.d(TAG, "myProgram : " + myProgram);
				for (int i = 0; i < opis.length; i++) {
					if (opis[i].title.equals(myProgram)) {
						opis[i].isTodays = true;
						Intent intent = new Intent(getApplicationContext(),
								VideoDetailActivity.class); 
						JSONObject jo = new JSONObject();
						
						OtherProgramItem opi = opis[i];
						///////////////////////////////
						try {
							
							JSONArray ja = new JSONArray();
							for(int j=0; j<opi.fads.length; j++) {
								ja.put(opi.fads[j].toJSON());
							}
							jo.put("fads", ja);
							jo.put("position", Integer.toString(0));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						intent.putExtra("json", jo.toString()); 
						startActivity(intent); 
						////////////////////////////
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
