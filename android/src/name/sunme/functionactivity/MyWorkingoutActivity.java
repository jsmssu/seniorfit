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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyWorkingoutActivity extends Activity {
	String TAG = "MyWorkingoutActivity";
	private ListView myworkingoutList;
	
	
	private DBHelper helper;
	private DBAdapter adapter;
	
	LinearLayout MyWorkingoutLayout;
	private SimpleAdapter simpleadapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_workingout);
		Log.d(TAG, "oncreate");
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		

		
		String programs = "[{\"title\":\"하루 운동\", \"list\":[[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"},{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"e\"}]]},{\"title\":\"주5회 세트\", \"list\":[[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"c\"}],[{\"mainMenuId\":\"a\"},{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"},{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"e\"}],[],[]]},{\"title\":\"주3회 세트\", \"list\":[[{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"}],[],[{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"c\"}],[],[{\"mainMenuId\":\"a\"},{\"mainMenuId\":\"b\"},{\"mainMenuId\":\"c\"},{\"mainMenuId\":\"d\"},{\"mainMenuId\":\"e\"}],[],[]]},{\"title\":\"다쓰기귀찮다\", \"list\":[[{\"subMenuId\":\"a_1\"},{\"subMenuId\":\"c_1\"},{\"subMenuId\":\"b_3\"},{\"subMenuId\":\"e_3\"},{\"subMenuId\":\"e_5\"}]]}]";
		
		
		
		myworkingoutList= (ListView) findViewById(R.id.left_listview);
		
		
		JSONArray jprograms;
		JSONArray jmainmenus;
		
		int today = 0;
		
		ArrayList<MyWorkingoutItem> mwiList = new ArrayList<MyWorkingoutItem>();
		try {
			jprograms = new JSONArray(programs);
			Log.d(TAG, "programs : "+jprograms.toString());
			for(int i=0; i<jprograms.length(); i++) { 
				
				JSONObject jo = jprograms.getJSONObject(i);
				
				String title = jo.getString("title");
				JSONArray jlist = jo.getJSONArray("list"); 
				Log.d(TAG, "title : " + title);
				
				JSONArray todaysWorkingout = jlist.getJSONArray(today%jo.length());
				Log.d(TAG, "raw list : " + todaysWorkingout.toString());
				
				ArrayList<FitApiDataClass> fads = new ArrayList<FitApiDataClass>();
				for(int j=0; j<todaysWorkingout.length(); j++) {
					JSONObject mainOrSub = todaysWorkingout.getJSONObject(j);
					try { 
						String mmid = mainOrSub.getString("mainMenuId");
						FitApiDataClass[] tfad = adapter.get_fitapidata_fromMainMenuId(mmid);
						for(int k=0; k<tfad.length; k++) { //어뎁터에 어레이리스트로 출력해주는거있으면 좋을듯... 나중에 해야지
							fads.add(tfad[k]);
							Log.d(TAG, "succ main added :"+tfad[k]._subMenuTitle);
						}
					} catch (Exception e) {};
					try {
						String smid = mainOrSub.getString("subMenuId");
						FitApiDataClass fad = adapter.get_fitapidata_fromSubMenuId(smid);
						fads.add(fad);
						Log.d(TAG, "succ sub added :"+fad._subMenuTitle);
					} catch (Exception e) {};  
				}
				
				MyWorkingoutItem mwItem = new MyWorkingoutItem(title, fads);
				mwiList.add(mwItem);  
			}
			
			Log.d(TAG, "mainmenu : "+adapter.get_setting("mainMenus"));
			jmainmenus = new JSONArray(adapter.get_setting("mainMenus")); //메인타이틀 목록을 뽑니다.
			for(int i=0; i<jmainmenus.length(); i++) {
				JSONObject jmainmenu = jmainmenus.getJSONObject(i);
				
				String title = jmainmenu.getString("mainMenuTitle");
				String mmid = jmainmenu.getString("mainMenuId");
				
				ArrayList<FitApiDataClass> fads = new ArrayList<FitApiDataClass>();
				FitApiDataClass[] tfad = adapter.get_fitapidata_fromMainMenuId(mmid);
				for(int k=0; k<tfad.length; k++) { //어뎁터에 어레이리스트로 출력해주는거있으면 좋을듯... 나중에 해야지
					fads.add(tfad[k]);
				}
				
				MyWorkingoutItem mwItem = new MyWorkingoutItem(title, fads);
				mwiList.add(mwItem);    
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ListView myworkingout_title_row = (ListView) findViewById(R.id.myworkingout_maintitle); 
		Log.d(TAG, "myworkingout_title_row " + myworkingout_title_row);
		MyWorkingoutListCustomAdapter mwoAdapter = new MyWorkingoutListCustomAdapter
				(this, R.layout.activity_my_workingout_row, mwiList);
		Log.d(TAG, "mwoAdapter " + mwoAdapter);
		myworkingout_title_row.setAdapter(mwoAdapter);
		myworkingout_title_row.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position); 
			}
			private void selectItem(int position) {
				Log.d(TAG, "selectItem : " +position);
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
