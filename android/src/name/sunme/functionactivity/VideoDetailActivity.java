package name.sunme.functionactivity;

import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoDetailActivity extends Activity {
	String TAG = "VideoDetailActivity";
	private String path_myapp;
	 
	private String videopath;
	private String myjson;
	private DBHelper helper;
	private DBAdapter adapter;
	
	
	private VideoView videoview;
	TextView videodetail_submenutitle;
	TextView videodetail_exerciseintensity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		Log.d(TAG, "VideoDetailActivity");
		
		
		videoview = (VideoView) findViewById(R.id.videodetail_videoview);
		videodetail_submenutitle = (TextView) findViewById(R.id.videodetail_submenutitle);
		videodetail_exerciseintensity = (TextView) findViewById(R.id.videodetail_exerciseintensity);
		
		
		path_myapp = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "";
		
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
		
		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		if (myintent != null) {
			myjson = myintent.getStringExtra("json");
		} else {
			finish();
		}
		
		JSONArray ja;
		try {
			ja = new JSONArray(myjson);
			
			for(int i=0; i<ja.length(); i++) {
				String _subMenuId = null;
				Log.d(TAG, "반복문 시작 ");
				try {
					JSONObject jo = (JSONObject) ja.get(i); 
					try { _subMenuId = jo.getString("subMenuId");} catch ( Exception e ) { e.printStackTrace(); }
					
					Log.d(TAG, "_subMenuId : "+ _subMenuId);
					FitApiDataClass fd = adapter.get_fitapidata_fromSubMenuId(_subMenuId);
					
					videodetail_submenutitle.setText(fd._subMenuTitle);
					videodetail_exerciseintensity.setText(fd._exerciseIntensity);
					
					videopath = path_myapp + "/" + fd._nameVideo;
					Log.d(TAG, videopath);
					videoview.setVideoPath(videopath);
					videoview.start();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
