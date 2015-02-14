package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fit_detail__main_menu_title);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		list = (ListView) findViewById(R.id.fitdetail_submenu_listview);
		listdata = new ArrayList<HashMap<String, String>>();
		fitdetail_mainmenu_programstart = (Button) findViewById(R.id.fitdetail_mainmenu_programstart);
		if (myintent != null) {
			mainMenuId = myintent.getStringExtra("mainMenuId");
			subMenuId = myintent.getStringExtra("subMenuId");

			// 탑 이미지 설정
			try {
				int background = Integer.parseInt(myintent
						.getStringExtra("background"));
				ImageView fitdetail_mainmenu = (ImageView) findViewById(R.id.fitdetail_mainmenu_topbg);
				fitdetail_mainmenu.setImageResource(background);
			} catch (Exception e) {
			}
			// 탑 타이틀 설정
			try {
				((TextView) findViewById(R.id.fitdetail_mainmenu_title))
						.setText(myintent.getStringExtra("title"));
			} catch (Exception e) {
			}

			helper = new DBHelper(getApplicationContext());
			dbadapter = new DBAdapter(getApplicationContext());

			// 동영상 리스트 보여줌
			int idx = 0;
			if (mainMenuId != null) {
				FitApiDataClass[] re = dbadapter
						.get_fitapidata_fromMainMenuId(mainMenuId);// adapter.get_fitapidata_fromMainMenuId(mainMenuId);
				for (final FitApiDataClass fad : re) {
					HashMap<String, String> dt = new HashMap<String, String>();
					dt.put("number", Integer.toString(idx));
					dt.put("title", fad._subMenuTitle);
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
						VideoShowActivity.class);
				Log.d(TAG, "setOnItemClickListener");

				String videoname = (dbadapter
						.get_fitapidata_fromSubMenuId(listdata.get(position)
								.get("subMenuId")))._nameVideo;

				intent.putExtra("videoname", videoname);
				startActivity(intent);

			}
		});

		if (listdata != null) {
			simpleadapter = new SimpleAdapter(
					FitDetail_MainMenuTitleActivity.this, listdata,
					R.layout.activity_fit_detail__sub_menu_title_row,
					new String[] { "number", "title", "subMenuId" }, new int[] {
							R.id.subtitlerow_number, R.id.subtitlerow_title });
			list.setAdapter(simpleadapter);
			listUpdate();
		}

		// 시작하기 버튼을 누르면, 리스트를 전체 동영상을 보여주게 됨.
		fitdetail_mainmenu_programstart
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (listdata != null) { 
							JSONArray fdsJson = new JSONArray();
							for (int i = 0; i < listdata.size(); i++) {
								JSONObject fdJson = new JSONObject();
								try {
									fdJson.put("subMenuId", listdata.get(i)
											.get("subMenuId"));
									fdsJson.put(fdJson);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}  
							Intent intent = new Intent(getApplicationContext(),
									VideoDetailActivity.class);
							intent.putExtra("json", fdsJson.toString());
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(),
				OtherProgramActivity.class);
		startActivity(intent);
		finish();
		return false;
	} 
}
