package name.sunme.functionactivity;

import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.GlobalData;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OtherProgramActivity extends Activity {
	String TAG = "OtherProgramActivity";
	private SimpleAdapter simpleadapter; 
	private ListView list;
	private static ArrayList<HashMap<String, String>> listdata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_program);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		Log.d("TAG", "start");
		list = (ListView)findViewById(R.id.otherprogram_listview);
		Log.d("TAG", "findViewById");
		listdata = new ArrayList<HashMap<String, String>>(); 
		listdata.add(new HashMap<String, String>() {{
	         put("title", "하루 운동");
	          put("time", "2시간");
	          put("day", "주 1-2회");
	          put("mainMenuId", "DayWorkingout");
	          put("background", R.drawable.otherprogram_bg1+"");
	          
	      }});
	      listdata.add(new HashMap<String, String>() {{
	         put("title", "준비운동");
	          put("time", "15분");
	          put("day", "운동 전");
	          put("mainMenuId", "a");
	          put("background", R.drawable.otherprogram_bg2+"");
	          
	      }});
	      listdata.add(new HashMap<String, String>() {{
	         put("title", "근력강화");
	          put("time", "25분");
	          put("day", "주 2-3회");
	          put("mainMenuId", "b");
	          put("background", R.drawable.otherprogram_bg3+"");
	      }});
	      listdata.add(new HashMap<String, String>() {{
	         put("title", "심폐 지구력 강화");
	          put("time", "45분");
	          put("day", "주 2-3회");
	          put("mainMenuId", "c");
	          put("background", R.drawable.otherprogram_bg4+"");
	      }});
	      listdata.add(new HashMap<String, String>() {{
	         put("title", "유연성강화");
	          put("time", "15분");
	          put("day", "주 2-3회");
	          put("mainMenuId", "d");
	          put("background", R.drawable.otherprogram_bg5+"");
	      }});
	      listdata.add(new HashMap<String, String>() {{
	         put("title", "평행성/체력강화");
	          put("time", "25분");
	          put("day", "주 2-3회");
	          put("mainMenuId", "e");
	          put("background", R.drawable.otherprogram_bg6+"");
	      }});
	      listdata.add(new HashMap<String, String>() {{
	         put("title", "정리운동");
	          put("time", "15분");
	          put("day", "운동 후");
	          put("mainMenuId", "f");
	          put("background", R.drawable.otherprogram_bg7+"");
	      }});
		Log.d("TAG", "simpleadapter");
		simpleadapter = new SimpleAdapter(
				OtherProgramActivity.this,  
				listdata,
        		R.layout.activity_other_program_row,
        		new String[]{"title","time", "day", "background"}, 
        		new int[]{R.id.otherprogram_listrow_title, R.id.otherprogram_listrow_time, R.id.otherprogram_listrow_day, R.id.otherprogram_listrow_bg});
		list.setAdapter(simpleadapter); 
		Log.d(TAG, "setAdapter");
		list.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {
		    	
		      if (listdata.get(position).get("mainMenuId").equals("DayWorkingout")) {
		    	  
		      } else {
		    	  Intent intent = new Intent(getApplicationContext(), FitDetail_MainMenuTitleActivity.class);
		    	  intent.putExtra("title", listdata.get(position).get("title"));
			      intent.putExtra("mainMenuId", listdata.get(position).get("mainMenuId"));
			      intent.putExtra("background", listdata.get(position).get("background"));
			      Log.d(TAG, "putExtra :" + listdata.get(position).get("mainMenuId"));
			      startActivity(intent);  
			      finish();
		      }
		    }
		});
		Log.d("TAG", "listUpdate");
		listUpdate();
	}
	
	
	
	private final Handler mhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			listUpdate();
		}
	}; 
    private void listUpdate()
    {
    	//adapter.refresh_data();
    	simpleadapter.notifyDataSetChanged();
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
