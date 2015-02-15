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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyWorkingoutActivity extends Activity {
	String TAG = "MyWorkingoutActivity";
	private ListView myworkingout_maintitle;
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
		
		String programs = "[{\"title\":\"하루 운동\", \"list\":[[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"},{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"e\"}]]},{\"title\":\"주5회 세트\", \"list\":[[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"a\"},{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"},{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"e\"}],[],[]]},{\"title\":\"주3회 세트\", \"list\":[[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"}],[],[{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"c\"}],[],[{\"mainMenuId\":\"a\"},{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"},{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"e\"}],[],[]]},{\"title\":\"다쓰기귀찮다\", \"list\":[[{\"subMenuId\":\"a_1\"},{\"subMenuId\":\"c_1\"},{\"subMenuId\":\"b_3\"},{\"subMenuId\":\"e_3\"},{\"subMenuId\":\"e_5\"}]]}]";
		
		JSONArray jprograms;
		JSONArray jmainmenus;
		try {
			jprograms = new JSONArray(programs);
			Log.d(TAG, "programs : "+jprograms.toString());
			for(int i=0; i<jprograms.length(); i++) {
				HashMap<String, String> elem = new HashMap<String, String>();
				elem.put("title", jprograms.getJSONObject(i).getString("title"));
				listdata.add(elem);
			}
			Log.d(TAG, "mainmenu : "+adapter.get_setting("mainMenus"));
			jmainmenus = new JSONArray(adapter.get_setting("mainMenus")); //메인타이틀 목록을 뽑니다.
			for(int i=0; i<jmainmenus.length(); i++) {
				HashMap<String, String> elem = new HashMap<String, String>();
				elem.put("title", jmainmenus.getJSONObject(i).getString("mainMenuTitle")); //추가한다.
				listdata.add(elem);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		
		myworkingout_maintitle = (ListView) findViewById(R.id.myworkingout_maintitle);
		//View child = getLayoutInflater().inflate(R.layout.row);
		if (listdata != null) {
			Log.d(TAG, "listdata != null");
			simpleadapter = new SimpleAdapter(
					MyWorkingoutActivity.this, listdata,
					R.layout.activity_my_workingout_row,
					new String[] { "title"},
					new int[] { R.id.myworkingout_title});
			myworkingout_maintitle.setAdapter(simpleadapter);
			listUpdate();
		}
		
		/*
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
		});*/
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
