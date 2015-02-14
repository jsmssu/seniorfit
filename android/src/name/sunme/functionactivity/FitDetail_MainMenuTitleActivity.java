package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FitDetail_MainMenuTitleActivity extends Activity {
	String TAG = "FitDetail_MainMenuTitleActivity";
	private DBHelper helper;
	private DBAdapter adapter;
	private ListView list;
	private String mainMenuId = null;
	private String subMenuId = null;
	private static ArrayList<HashMap<String, String>> listdata;
	private SimpleAdapter simpleadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fit_detail__main_menu_title);
		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		list = (ListView) findViewById(R.id.fitdetail_submenu_listview);
		if (myintent != null) {
			mainMenuId = myintent.getStringExtra("mainMenuId");
			subMenuId = myintent.getStringExtra("subMenuId");
			int background = Integer.parseInt(myintent
					.getStringExtra("background"));

			helper = new DBHelper(getApplicationContext());
			adapter = new DBAdapter(getApplicationContext());

			listdata = new ArrayList<HashMap<String, String>>();

			int idx = 0;
			if (mainMenuId != null) {
				FitApiDataClass[] re = adapter
						.get_fitapidata_fromMainMenuId(mainMenuId);// adapter.get_fitapidata_fromMainMenuId(mainMenuId);
				for (final FitApiDataClass fad : re) {
					HashMap<String, String> dt = new HashMap<String, String>();
					dt.put("number", Integer.toString(idx));
					dt.put("title", fad._subMenuTitle);
					dt.put("time", fad._exerciseTime);
					dt.put("subMenuId", fad._subMenuId);
					listdata.add(dt);
					idx = idx + 1;
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), "mainMenuId를 확인해주세요", 3)
					.show();
		}

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						VideoDetailActivity.class);
				Log.d(TAG, "setOnItemClickListener");
				try {
					JSONArray ja = new JSONArray();
					JSONObject jo = new JSONObject();
					jo.put("subMenuId", listdata.get(position).get("subMenuId"));
					jo.put("startTime", "0");
					jo.put("endTime", "0");
					jo.put("repeat", "0");
					ja.put(jo);
					Log.d(TAG, "putExtra : " + ja.toString());
					intent.putExtra("json", ja.toString());
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		if (listdata != null) {
			simpleadapter = new SimpleAdapter(
					FitDetail_MainMenuTitleActivity.this, listdata,
					R.layout.activity_fit_detail__sub_menu_title_row,
					new String[] { "number", "title", "time", "subMenuId" },
					new int[] { R.id.subtitlerow_textview_number,
							R.id.subtitlerow_textview_title,
							R.id.subtitlerow_textview_totalelapsedtime });
			list.setAdapter(simpleadapter);
			listUpdate();
		}
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
