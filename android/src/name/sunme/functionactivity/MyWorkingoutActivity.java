package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;
 










import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.TodaysWorkingoutList;
import name.sunme.maindrawbar.MyDrawerListCustomAdapter;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyWorkingoutActivity extends Activity {
	String TAG = "MyWorkingoutActivity";
	private ListView myworkingoutList;
	
	
	private DBHelper helper;
	private DBAdapter adapter;
	
	Button myworkingout_next;
	LinearLayout MyWorkingoutLayout;
	private SimpleAdapter simpleadapter;
	MyWorkingoutItem[] mwiList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_workingout);
		Log.d(TAG, "oncreate");
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
		myworkingout_next = (Button)findViewById(R.id.myworkingout_next);
		myworkingoutList= (ListView)findViewById(R.id.left_listview);
		
		JSONArray jmainmenus;
		
		
		try {
			Log.d(TAG, "mainmenu : "+adapter.get_setting("mainMenus"));
			jmainmenus = new JSONArray(adapter.get_setting("mainMenus")); //메인타이틀 목록을 뽑니다.
			mwiList = new MyWorkingoutItem[jmainmenus.length()];
			for(int i=0; i<jmainmenus.length(); i++) {
				JSONObject jmainmenu = jmainmenus.getJSONObject(i);
				
				String title = jmainmenu.getString("mainMenuTitle");
				String mmid = jmainmenu.getString("mainMenuId");
				
				FitApiDataClass[] fads = adapter.get_fitapidata_fromMainMenuId(mmid);
				MyWorkingoutItem mwItem = new MyWorkingoutItem(title, mmid, fads);
				
				mwiList[i] = mwItem;    
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ListView myworkingout_maintitle = (ListView) findViewById(R.id.myworkingout_maintitle); 
		Log.d(TAG, "myworkingout_title_row " + myworkingout_maintitle);
		MyWorkingoutListCustomAdapter mwoAdapter = new MyWorkingoutListCustomAdapter
				(this, R.layout.activity_my_workingout_row, mwiList);
		Log.d(TAG, "mwoAdapter " + mwoAdapter);
		myworkingout_maintitle.setAdapter(mwoAdapter);
		
		
		myworkingout_maintitle.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
				 
				
			}
			private void selectItem(int position) {
				
				
				Log.d(TAG, "selectItem : " +position);
			}
		});
		myworkingout_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int mwiListLen = mwiList.length;
				JSONArray jo = new JSONArray();
				for(int i=0; i<mwiList.length; i++) {
					for(int j=0; j<mwiList[i].setting_subMenuIds.length; j++){
						if(mwiList[i].setting_subMenuIds[j]==true) {
							jo.put(mwiList[i].fads[j]._subMenuId);
						}
					}
				}
				Intent intent = new Intent(getApplicationContext(),
						VideoDetailActivity.class);
				intent.putExtra("subMenuIds",
						jo.toString());
				startActivity(intent);
			}
		});
		
	}
	
	
	private final Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			listUpdate();
		}
	};

	private void listUpdate() {
		// adapter.refresh_data();
		simpleadapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
