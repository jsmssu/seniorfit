package name.sunme.functionactivity;

import name.sunme.firstexecution.Setup1Activity;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.seniorfit.R;
import com.example.seniorfit.R.drawable;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoDetailActivity extends Activity {
	String TAG = "VideoDetailActivity";
	
	 
	
	private String myjson;
	private DBHelper helper;
	private DBAdapter adapter;
	 
	int idx = 0;
	int maxIndex = 0;
	JSONArray fdsJson = null;
	
	TextView videodetail_exerciseintensity;
	TextView videodetail_submenutitle; 
	TextView videodetail_idxindicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		Log.d(TAG, "VideoDetailActivity");
		
		
		
		videodetail_submenutitle = (TextView)findViewById(R.id.videodetail_submenutitle);
		videodetail_exerciseintensity = (TextView)findViewById(R.id.videodetail_exerciseintensity);
		videodetail_idxindicator = (TextView)findViewById(R.id.videodetail_idxindicator);
		
		
		
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
		
		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		
		 
		
		try {
			fdsJson =  new JSONArray(myintent.getStringExtra("json"));
			maxIndex = fdsJson.length();
			showNextVideo();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "resultCode : "+resultCode);
		switch (resultCode) {
		case 1: 
			showNextVideo(); 
			Log.d(TAG,  "정상종료. 다음 동영상재생을 위한 페이지");
			Toast.makeText (getApplicationContext(), "정상종료. 다음 동영상재생", 1).show();
			break;
		case -1:
			Log.d(TAG, "비정상종료. 다음 노노해");
			Toast.makeText (getApplicationContext(), "비정상종료. 다음 노노해", 1).show();
			break;

		default:
			break;
		}
	}
	void showNextVideo() { 
		try {
			if (idx + 1 > maxIndex) { return; }
			idx = idx + 1;
			JSONObject fdJson = fdsJson.getJSONObject(idx);
		
			FitApiDataClass fd = adapter.get_fitapidata_fromSubMenuId(fdJson.getString("subMenuId"));
			videodetail_submenutitle.setText(fd._subMenuTitle);
			videodetail_exerciseintensity.setText(fd._exerciseIntensity);
			videodetail_idxindicator.setText(idx+"/"+maxIndex);
			String[] t_thumbnails = (drawable.temp_thumbnail + "-" + drawable.temp_thumbnail + "-" + drawable.temp_thumbnail).split("-");  
			
			final int[] thumbnails = new int[t_thumbnails.length];
			for(int i=0; i<t_thumbnails.length; i++) {
				Log.d(TAG, "Thumbnamil " + i + " : " + t_thumbnails[i]);
				thumbnails[i] =  Integer.parseInt(t_thumbnails[i]);
			}
			
	
			VideoThumbnailAdapter pageadapter = new VideoThumbnailAdapter(this, fd, thumbnails);
			ViewPager viewPager = (ViewPager) findViewById(R.id.videodetail_pager);
			viewPager.setAdapter(pageadapter);
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageScrollStateChanged(int state) {
					Log.d(TAG, "StateChanged : " + state); 
				}
				@Override
				public void onPageSelected(int position) { 
				  Log.d(TAG, "Selected position" + position); 
				}


				int tpositionOffsetPixels;
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					if (thumbnails.length == (position+1) && positionOffsetPixels == 0
							&& tpositionOffsetPixels == positionOffsetPixels) {
						Log.d(TAG, "마지막페이지"); 
					}
					if (0 == position && positionOffsetPixels == 0
							&& tpositionOffsetPixels == positionOffsetPixels) {
						Log.d(TAG, "첫페이지"); 
					}
					tpositionOffsetPixels = positionOffsetPixels;
					Log.d(TAG, "Selected " + position+", "+positionOffset +", " + positionOffsetPixels ); 
					// TODO Auto-generated method stub
					
				} 
			});
		}catch (Exception e) {
			
		}
			
	}
}
