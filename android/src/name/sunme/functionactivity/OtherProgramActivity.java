package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.GlobalData;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OtherProgramActivity extends Activity {
	String TAG = "OtherProgramActivity";
	private SimpleAdapter simpleadapter;
	private ListView list;
	private static ArrayList<HashMap<String, String>> listdata;
	private DBAdapter dbAdapter;
	
	
	int MODE_SETTING = 1;
	int MODE_NORMAL = 0;
	int MODE = MODE_NORMAL;
	
	OtherProgramListCustomAdapter oplca;
	OtherProgramItem[] opis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_program);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		dbAdapter = new DBAdapter(getApplicationContext());

		list = (ListView) findViewById(R.id.otherprogram_listview);
		opis = new OtherProgramItem[7];

		FitApiDataClass[] fads1 = dbAdapter
				.get_fitapidata_fromMainMenuId("DayWorkingout");
		opis[0] = new OtherProgramItem("하루 운동", "2시간", "주 1-2회", fads1,
				R.drawable.otherprogram_bg1, false);

		FitApiDataClass[] fads2 = dbAdapter.get_fitapidata_fromMainMenuId("a");
		opis[1] = new OtherProgramItem("준비운동", "15분", "주 1-2회", fads2,
				R.drawable.otherprogram_bg2, false);

		FitApiDataClass[] fads3 = dbAdapter.get_fitapidata_fromMainMenuId("b");
		opis[2] = new OtherProgramItem("근력강화", "25분", "주 2-3회", fads3,
				R.drawable.otherprogram_bg3, false);

		FitApiDataClass[] fads4 = dbAdapter.get_fitapidata_fromMainMenuId("c");
		opis[3] = new OtherProgramItem("심폐 지구력 강화", "45분", "주 2-3회", fads4,
				R.drawable.otherprogram_bg4, false);

		FitApiDataClass[] fads5 = dbAdapter.get_fitapidata_fromMainMenuId("d");
		opis[4] = new OtherProgramItem("유연성강화", "15분", "주 2-3회", fads5,
				R.drawable.otherprogram_bg5, false);

		FitApiDataClass[] fads6 = dbAdapter.get_fitapidata_fromMainMenuId("e");
		opis[5] = new OtherProgramItem("평행성/체력강화", "25분", "주 2-3회", fads6,
				R.drawable.otherprogram_bg6, false);

		FitApiDataClass[] fads7 = dbAdapter.get_fitapidata_fromMainMenuId("f");
		opis[6] = new OtherProgramItem("정리운동", "15분", "운동 후", fads7,
				R.drawable.otherprogram_bg7, false);

		checkTodays();

		oplca = new OtherProgramListCustomAdapter(
				this, R.layout.activity_other_program_row, opis);
		list.setAdapter(oplca);
		Log.d(TAG, "oplca");
		applyMode();
		Log.d("TAG", "listUpdate");
	}
	
	OnItemClickListener listclick_normal = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Intent intent = new Intent(getApplicationContext(),
					FitDetail_MainMenuTitleActivity.class);
			intent.putExtra("json", opis[position].toJson());
			startActivity(intent);
			finish();
		}
		
	};
	OnItemClickListener listclick_setting = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			dbAdapter.put_setting("myProgram", opis[position].title);
			for(int i=0; i<opis.length; i++) {
				opis[i].isTodays = false;
			}
			opis[position].isTodays = true;
			MODE = MODE_NORMAL;
			oplca.notifyDataSetChanged();
			applyMode();
		}
	};

	public void checkTodays() {
		String myProgram = dbAdapter.get_setting("myProgram");
		Log.d(TAG, "myProgram : " + myProgram);
		for (int i = 0; i < opis.length; i++) {
			if (opis[i].title.equals(myProgram)) {
				opis[i].isTodays = true;
			} else {
				opis[i].isTodays = false;
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case android.R.id.home:
			finish();
			break;
		case R.id.action_gear:
			if (MODE == MODE_SETTING) {
				MODE = MODE_NORMAL;
			} else if (MODE == MODE_NORMAL) {
				MODE = MODE_SETTING;
			}
			
			applyMode();
			break;
		default:
			break;
		}
		return false;
	}
	public void applyMode() {
		TextView otherprogram_settinghelp = (TextView)findViewById(R.id.otherprogram_settinghelp);
		if (MODE == MODE_SETTING) {
			list.setOnItemClickListener(listclick_setting);
			otherprogram_settinghelp.setVisibility(View.VISIBLE);
		} else if (MODE == MODE_NORMAL) {
			list.setOnItemClickListener(listclick_normal);
			otherprogram_settinghelp.setVisibility(View.GONE);
		} 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu ");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.custom_actionbar_bs, menu);
		Log.d(TAG, "inflater ");
		return true;
	}
}
