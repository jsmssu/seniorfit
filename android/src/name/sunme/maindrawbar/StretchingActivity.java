package name.sunme.maindrawbar;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.view.KeyEvent;
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
	
	private DBAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stretching);
		Log.d(TAG, "set actioinbar");
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        dbAdapter = new DBAdapter(getApplicationContext());
        
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
			}
		});
        stretcing_myworkingout.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				Log.d(TAG, "button_chooseprogram");
				Intent intent = new Intent(getApplicationContext(), MyWorkingoutActivity.class);
				startActivity(intent); 
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
		timecheck_thread.start();  
        
	}
	String db_str_stretcing = "tw_";
	SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
	
	boolean threadstopped =true;
	Thread timecheck_thread = new Thread(new Runnable() {  
		Date today = new Date();
		String key = db_str_stretcing + dbformat.format(today);
		
        public void run() {
        	int sec = 0;
        	threadstopped = false;
        	String value = dbAdapter.get_setting(key);
        	if(value!=null) {
        		sec = Integer.parseInt(value);
        	}
            while (!threadstopped) {                
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) { 
                    ie.printStackTrace();
                } 
                sec = sec + 5;
        		dbAdapter.put_setting(key, Integer.toString(sec));
        		Log.d(TAG, "¿îµ¿Áß "+sec);
            }
        }
    }); 
	public void stop() {
        threadstopped = true; 
        if (timecheck_thread.isAlive()) {
        timecheck_thread.interrupt(); 
        }
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item) { 
		stop();
        finish();  
        return false;
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			stop();
		}
		return super.onKeyDown(keyCode, event);
	}
}
