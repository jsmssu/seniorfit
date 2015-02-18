package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.GlobalData;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.StretchingActivity;
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
		
		
		opis = GlobalData.getOtherProgramItem(getApplicationContext());

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
			startActivity(new Intent(getApplicationContext(), StretchingActivity.class));
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
			getActionBar().setTitle(getString(R.string.title_activity_other_program_setting));
			otherprogram_settinghelp.setVisibility(View.VISIBLE);
		} else if (MODE == MODE_NORMAL) {
			list.setOnItemClickListener(listclick_normal);
			getActionBar().setTitle(getString(R.string.title_activity_other_program));
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
