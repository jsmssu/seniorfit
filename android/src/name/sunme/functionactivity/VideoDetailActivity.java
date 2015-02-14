package name.sunme.functionactivity;

import name.sunme.firstexecution.Setup1Activity;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.ImageAdapter;
import name.sunme.seniorfit.VideoThumbnailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.seniorfit.R;
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
import android.widget.VideoView;

public class VideoDetailActivity extends Activity {
	String TAG = "VideoDetailActivity";
	
	 
	
	private String myjson;
	private DBHelper helper;
	private DBAdapter adapter;
	 
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
		
		
		JSONObject set = null; 
		
		try {
			set =  new JSONObject(myintent.getStringExtra("json"));
			Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
		
		
		
		try {
			int idx;
			idx = Integer.parseInt(set.getString("idx"));
			
			int maxIndex = Integer.parseInt(set.getString("maxIndex"));
			JSONArray fdsJson = set.getJSONArray("fdsJson");
			JSONObject fdJson = fdsJson.getJSONObject(idx);
			FitApiDataClass fd = adapter.get_fitapidata_fromSubMenuId(fdJson.getString("subMenuId"));
			videodetail_submenutitle.setText(fd._subMenuTitle);
			videodetail_exerciseintensity.setText(fd._exerciseIntensity);
			videodetail_idxindicator.setText(idx+"/"+maxIndex);
			String[] t_thumbnails = (fdJson.getString("thumbnails")).split("-");  
			
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
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
