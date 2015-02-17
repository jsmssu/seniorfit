package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.GlobalData;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OtherProgramActivity extends Activity {
	String TAG = "OtherProgramActivity";
	private SimpleAdapter simpleadapter;
	private ListView list;
	private static ArrayList<HashMap<String, String>> listdata;
	private DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_program);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		dbAdapter = new DBAdapter(getApplicationContext());

		list = (ListView) findViewById(R.id.otherprogram_listview);
		final ArrayList<OtherProgramItem> opis = new ArrayList<OtherProgramItem>();

		FitApiDataClass[] fads1 = dbAdapter
				.get_fitapidata_fromMainMenuId("DayWorkingout");
		opis.add(new OtherProgramItem("�Ϸ� �", "2�ð�", "�� 1-2ȸ", fads1,
				R.drawable.otherprogram_bg1, true));

		FitApiDataClass[] fads2 = dbAdapter.get_fitapidata_fromMainMenuId("a");
		opis.add(new OtherProgramItem("�غ�", "15��", "�� 1-2ȸ", fads2,
				R.drawable.otherprogram_bg2, false));

		FitApiDataClass[] fads3 = dbAdapter.get_fitapidata_fromMainMenuId("b");
		opis.add(new OtherProgramItem("�ٷ°�ȭ", "25��", "�� 2-3ȸ", fads3,
				R.drawable.otherprogram_bg3, false));

		FitApiDataClass[] fads4 = dbAdapter.get_fitapidata_fromMainMenuId("c");
		opis.add(new OtherProgramItem("���� ������ ��ȭ", "45��", "�� 2-3ȸ", fads4,
				R.drawable.otherprogram_bg4, false));

		FitApiDataClass[] fads5 = dbAdapter.get_fitapidata_fromMainMenuId("d");
		opis.add(new OtherProgramItem("��������ȭ", "15��", "�� 2-3ȸ", fads5,
				R.drawable.otherprogram_bg5, false));

		FitApiDataClass[] fads6 = dbAdapter.get_fitapidata_fromMainMenuId("e");
		opis.add(new OtherProgramItem("���༺/ü�°�ȭ", "25��", "�� 2-3ȸ", fads6,
				R.drawable.otherprogram_bg6, false));

		FitApiDataClass[] fads7 = dbAdapter.get_fitapidata_fromMainMenuId("f");
		opis.add(new OtherProgramItem("�����", "15��", "� ��", fads7,
				R.drawable.otherprogram_bg7, false));

		OtherProgramListCustomAdapter oplca = new OtherProgramListCustomAdapter(
				this, R.layout.activity_other_program_row, opis);
		list.setAdapter(oplca);
		Log.d(TAG, "oplca");
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						FitDetail_MainMenuTitleActivity.class);
				intent.putExtra("json", opis.get(position).toJson());
				startActivity(intent);
				finish();
			}
		});
		Log.d("TAG", "listUpdate");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
