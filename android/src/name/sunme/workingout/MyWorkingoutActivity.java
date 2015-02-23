package name.sunme.workingout;

import java.util.ArrayList;
import java.util.HashMap;
  






import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.Utils;
import name.sunme.video.VideoDetailActivity;
import name.sunme.firstexecution.Setup4Activity;
import name.sunme.maindrawbar.MyDrawerListCustomAdapter;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	
	
	private DBAdapter dbAdapter;
	private SimpleAdapter simpleadApter;
	
	
	private Button myworkingout_next;
	private ListView myworkingout_maintitle;
	
	public final static int STATE_CHECK_CHANGED = 1;
	public final static int STATE_FOLD_CHANGED = 2;
	public final static int STATE_CHECK_SUBMENU_CHANGED = 3;
	
	private MyWorkingoutItem[] mwiList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_workingout);
		
		
		dbAdapter = new DBAdapter(getApplicationContext());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
		
		myworkingout_maintitle = (ListView) findViewById(R.id.myworkingout_maintitle); 
		myworkingout_next = (Button)findViewById(R.id.myworkingout_next);
		
		
		if((mwiList = MyWorkingoutItem.get_mainWorkingoutItems(getApplicationContext())) == null) { 
			finish(); 
		} else {
			for(int i=0; i<mwiList.length; i++) {
				mwiList[i].loadSetting(getApplicationContext());
			}
		}
		
		
		MyWorkingoutListCustomAdapter mwoAdapter = new MyWorkingoutListCustomAdapter
				(this, R.layout.activity_my_workingout_row, mwiList, mhandler);
		
		myworkingout_maintitle.setAdapter(mwoAdapter);
		
		 
		
		
		
		
		myworkingout_maintitle.setOnItemClickListener(mainmenu_clicklistener);
		myworkingout_next.setOnClickListener(start_clicklistener);
		
		
		
		
	}
	
	OnClickListener start_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			for(int i=0; i<mwiList.length; i++) {
				for(int j=0; j<mwiList[i].setting_checked_subMenuIds.length; j++){
					if(mwiList[i].setting_checked_subMenuIds[j]==true) {
						ja.put(mwiList[i].fads[j].toJSON());
					}
				}
			}
			try {
				jo.put("fads", ja);
				jo.put("position", Integer.toString(0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent(getApplicationContext(),
					VideoDetailActivity.class);
			intent.putExtra("json", jo.toString());
			startActivity(intent);
		}
	};
	
	OnItemClickListener mainmenu_clicklistener = new ListView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			selectItem(position);
		}
		private void selectItem(int position) {
			Log.d(TAG, "selectItem : " +position);
		}
	};
	
	private final Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, msg.what+"  mhandler");
			switch (msg.what) {
			case STATE_CHECK_CHANGED:
				break;
			case STATE_CHECK_SUBMENU_CHANGED:
				break;
			case STATE_FOLD_CHANGED:
				break;
			/*case 100:
				Bundle bundle = msg.getData();
				String result = bundle.getString("position");
				Log.d(TAG, result);
				break;*/
				/*Message msg = new Message();
    			msg.what = 100;
    			Bundle data = new Bundle();
    			data.putString("position", position + "");
    			msg.setData(data);
    			mHandler.sendMessage(msg);*/
			default:
				
				break;
			}
			//listUpdate();
		}
	};

	private void listUpdate() {
		// adapter.refresh_data();
		simpleadApter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
