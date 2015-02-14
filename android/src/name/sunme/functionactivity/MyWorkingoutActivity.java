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

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyWorkingoutActivity extends Activity {
	String TAG = "MyWorkingoutActivity";
	private ListView list;
	private static ArrayList<HashMap<String, String>> listdata;
	
	private DBHelper helper;
	private DBAdapter adapter;
	
	
	private SimpleAdapter simpleadapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_workingout);
		Log.d(TAG, "oncreate");
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
		Log.d(TAG, "listdata");
		listdata = new ArrayList<HashMap<String, String>>(); 
		listdata.add(new HashMap<String, String>() {{put("title", "준비 운동");put("mainMenuId", "a");}});
		listdata.add(new HashMap<String, String>() {{put("title", "근력 강화");put("mainMenuId", "b");}});
		listdata.add(new HashMap<String, String>() {{put("title", "심폐 지구력 강화");put("mainMenuId", "c");}});
		listdata.add(new HashMap<String, String>() {{put("title", "유연성강화");put("mainMenuId", "d");}});
		listdata.add(new HashMap<String, String>() {{put("title", "평행성/체력강화");put("mainMenuId", "e");}});
		listdata.add(new HashMap<String, String>() {{put("title", "정리운동");put("mainMenuId", "f");}});
		
		list = (ListView) findViewById(R.id.workingout_listview);
		//View child = getLayoutInflater().inflate(R.layout.row);
		if (listdata != null) {
			Log.d(TAG, "listdata != null");
			simpleadapter = new SimpleAdapter(
					MyWorkingoutActivity.this, listdata,
					R.layout.activity_my_workingout_row,
					new String[] { "title"},
					new int[] { R.id.myworkingout_title});
			list.setAdapter(simpleadapter);
			listUpdate();
		}
		
		
		for(int i=0; i<listdata.size(); i++) {
			 ArrayList<HashMap<String, String>> sublistdata = new ArrayList<HashMap<String, String>>();
			 FitApiDataClass[] fad = adapter.get_fitapidata_fromMainMenuId(listdata.get(i).get("mainMenuId"));
			 for(int j=0; j<fad.length; j++) {
				 HashMap<String, String> hm = new HashMap<String, String>();
				 hm.put("subMenuTitle", fad[i]._subMenuTitle);
				 hm.put("exerciseTime",fad[i]._exerciseTime);
				 hm.put("exerciseFrequency",fad[i]._exerciseFrequency);
				 sublistdata.add(hm);
			 }
			 
		}
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//listdata.get(position).get("listdata"); 
				//http://stackoverflow.com/questions/9817165/how-to-add-listview-in-listview-in-android
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
}
