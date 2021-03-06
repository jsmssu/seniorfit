package name.sunme.workingout;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.video.VideoDetailActivity;
import name.sunme.video.VideoShowActivity;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FitDetail_MainMenuTitleActivity extends Activity {
	String TAG = "FitDetail_MainMenuTitleActivity";
	private DBHelper helper;
	private DBAdapter dbadapter;
	private ListView list;
	private String mainMenuId = null;
	private String subMenuId = null;
	private Button fitdetail_mainmenu_programstart;
	private static ArrayList<HashMap<String, String>> listdata;
	private SimpleAdapter simpleadapter;

	private OtherProgramItem opi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fit_detail__main_menu_title);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		list = (ListView) findViewById(R.id.fitdetail_submenu_listview);

		fitdetail_mainmenu_programstart = (Button) findViewById(R.id.fitdetail_mainmenu_programstart);
		if (myintent != null) {
			JSONObject json;
			try {
				String jsonstr = myintent.getStringExtra("json");
				opi = OtherProgramItem.fromJSON(getApplicationContext(),
						new JSONObject(jsonstr));

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log.d(TAG, "opi title : " + opi.title);

			((ImageView) findViewById(R.id.fitdetail_mainmenu_topbg))
					.setImageResource(opi.background);
			((TextView) findViewById(R.id.fitdetail_mainmenu_title))
					.setText(opi.title);

			((TextView) findViewById(R.id.fitdetail_mainmenu_time))
					.setText(opi.time);

			((TextView) findViewById(R.id.fitdetail_mainmenu_day))
					.setText(opi.day);


			listdata = getSubListItems();
		} 
		if (listdata.size() > 0) {
			Log.d(TAG, "listdata > 0 ");
			simpleadapter = new SimpleAdapter(
					FitDetail_MainMenuTitleActivity.this, listdata,
					R.layout.activity_fit_detail__sub_menu_title_row,
					new String[] { "number", "title"}, new int[] {
							R.id.subtitlerow_number, R.id.subtitlerow_title }); 
			list.setAdapter(simpleadapter); 
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (opi.fads[position]._subMenuId!=null) {
						Intent intent = new Intent(getApplicationContext(),
								VideoShowActivity.class);
						
						JSONObject jo = new JSONObject();
						try {
							
							JSONArray ja = new JSONArray();
							for(int i=0; i<opi.fads.length; i++) {
								ja.put(opi.fads[i].toJSON());
							}
							jo.put("fads", ja);
							jo.put("position", Integer.toString(position));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						intent.putExtra("json", jo.toString());
						startActivity(intent);
					}
				}
			});
			listUpdate();
		} 
		// 시작하기 버튼을 누르면, 리스트를 전체 동영상을 보여주게 됨.
		fitdetail_mainmenu_programstart
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (opi != null) {
							Intent intent = new Intent(getApplicationContext(),
									VideoDetailActivity.class); 
							
							JSONObject jo = new JSONObject();
							try {
								
								JSONArray ja = new JSONArray();
								for(int i=0; i<opi.fads.length; i++) {
									ja.put(opi.fads[i].toJSON());
								}
								jo.put("fads", ja);
								jo.put("position", Integer.toString(0));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							intent.putExtra("json", jo.toString()); 
							startActivity(intent); 
						}
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

	private ArrayList<HashMap<String, String>> getSubListItems() {
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		if (opi != null && opi.fads != null) {
			int idx = 0;
			for (final FitApiDataClass fad : opi.fads) { 
				HashMap<String, String> dt = new HashMap<String, String>();
				dt.put("number", Integer.toString(idx)); 
				dt.put("title", fad._subMenuTitle); 
				if (fad._subMenuId != null) {
					dt.put("subMenuId", fad._subMenuId);
				}
				al.add(dt);
				idx = idx + 1;
			}
		}
		return al;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(),
				OtherProgramActivity.class);
		startActivity(intent);
		finish();
		return false;
	}
}
