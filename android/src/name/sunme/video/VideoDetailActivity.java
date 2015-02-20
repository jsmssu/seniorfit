package name.sunme.video;

import name.sunme.firstexecution.Setup1Activity;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.GlobalData;
import name.sunme.seniorfit.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.maindrawbar.R.layout;
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
	
	static int REQUEST_CODE_MOVIE = 1;
	final static int RESUL_CODE_PRE_MOVIE = 2;
	final static int RESUL_CODE_NEXT_MOVIE = 3;
	final static int RESUL_CODE_STOP_MOVIE = 4;
	private DBHelper helper;
	private DBAdapter adapter;
	JSONObject jo;
	FitApiDataClass[] fads;
	int idx = 0;
	
	TextView videodetail_exerciseintensity;
	TextView videodetail_submenutitle; 
	TextView videodetail_idxindicator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		Log.d(TAG, "VideoDetailActivity");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		videodetail_submenutitle = (TextView)findViewById(R.id.videodetail_submenutitle);
		videodetail_exerciseintensity = (TextView)findViewById(R.id.videodetail_exerciseintensity);
		videodetail_idxindicator = (TextView)findViewById(R.id.videodetail_idxindicator);
		
		
		
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
		
		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		 
		
		try {
			Log.d(TAG, "try");
			jo = new JSONObject(myintent.getStringExtra("json"));
			idx = Integer.parseInt(jo.getString("position"));
			JSONArray fads_ja = new JSONArray(jo.getString("fads"));
			fads = new FitApiDataClass[fads_ja.length()];
			Log.d(TAG, "length " + fads_ja.length());
			for(int i=0; i<fads_ja.length(); i++) {
				fads[i] = FitApiDataClass.parseObject(fads_ja.getJSONObject(i));
				Log.d(TAG, "title " + fads[i]._subMenuTitle);
			}
			showVideo(idx);
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
		if (REQUEST_CODE_MOVIE == requestCode && resultCode == RESULT_OK ) {
			int result = data.getIntExtra("result", -1);
			Log.d(TAG, "result : "+result);
			switch (result) {
			case RESUL_CODE_PRE_MOVIE: //다음동영상 틀어주기
				if (idx > 0) 
					idx--;
				else 
					idx = 0;
				try { jo.put("position", Integer.toString(idx)); } catch (JSONException e) {}
				showVideo(idx);
				Toast.makeText (getApplicationContext(), "이전 동영상재생", 1).show();
				break;
			case RESUL_CODE_NEXT_MOVIE: //이전동영상 틀어주기
				if (idx + 1 < fads.length) 
					idx ++;
				else 
					idx = fads.length-1;
				try { jo.put("position", Integer.toString(idx)); } catch (JSONException e) {}
				showVideo(idx);
				Toast.makeText (getApplicationContext(), "다음 동영상재생", 1).show();
				break;
			case RESUL_CODE_STOP_MOVIE:
				
				break;
			default:
				break;
			}
		}
		
	}
	int showVideo(int idx) {
		try {
			int maxIndex = fads.length;
			Log.d(TAG, "idx = "+idx + "max : "+maxIndex);
			FitApiDataClass fd = fads[idx];
			Log.d(TAG, fd._subMenuTitle);
			
			if (fd._subMenuTitle!=null) videodetail_submenutitle.setText(fd._subMenuTitle);
			else videodetail_submenutitle.setText("데이터가 없습니다.");
			if (fd._exerciseIntensity!=null) videodetail_exerciseintensity.setText(fd._exerciseIntensity);
			else videodetail_exerciseintensity.setText("");
			videodetail_idxindicator.setText((idx+1)+"/"+maxIndex);
			
			
			VideoThumbnailAdapter pageadapter;
			final Integer[] thumbnails;
			if (fd._subMenuId!=null) {
				thumbnails = GlobalData.getThumbnail(fd._subMenuId);
			} else {
				thumbnails = new Integer[]{R.drawable.thumbnail_tmp};
			}
			pageadapter = new VideoThumbnailAdapter(this, jo, fd, thumbnails);
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
			return 1;
		}catch (Exception e) {
			return -1;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
