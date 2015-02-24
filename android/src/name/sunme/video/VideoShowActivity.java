package name.sunme.video;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import name.sunme.maindrawbar.MainActivity;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.Utils;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.id;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoShowActivity extends Activity {

	private String videopath;
	
	
	private VideoView videoview;
	private ImageView videoshow_play_intro_pre;
	private ImageView videoshow_play_intro_next;
	private ImageView videoshow_play_intro_exit;
	private LinearLayout videoshow_box_exit;
	private LinearLayout videoshow_box_next;
	private LinearLayout videoshow_box_pre; 
	private TextView videoshow_rec;
	private ImageView videoshow_play_intro_icn_play;
	String TAG = "VideoShowActivity";
	
	
	private String path_myapp;
	private File file_video; 
	private String videoname;
	
	RelativeLayout layout_videoshow_touch_control;
	RelativeLayout layout_videoshow_control;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_show);
		Log.d(TAG, "onCreate");
		layout_videoshow_touch_control = (RelativeLayout)findViewById(R.id.layout_videoshow_touch_control);
		layout_videoshow_control = (RelativeLayout)findViewById(R.id.layout_videoshow_control);
		videoview = (VideoView) findViewById(R.id.videodetail_videoview);
		videoshow_play_intro_pre = (ImageView) findViewById(R.id.videoshow_play_intro_pre); 
		videoshow_play_intro_next = (ImageView) findViewById(R.id.videoshow_play_intro_next); 
		videoshow_play_intro_exit = (ImageView) findViewById(R.id.videoshow_play_intro_exit); 
		videoshow_play_intro_icn_play = (ImageView) findViewById(R.id.videoshow_play_intro_icn_play); 
		
		videoshow_rec = (TextView) findViewById(R.id.videoshow_rec);
		
		videoshow_box_exit = (LinearLayout) findViewById(R.id.videoshow_box_exit); 
		videoshow_box_next = (LinearLayout) findViewById(R.id.videoshow_box_next); 
		videoshow_box_pre = (LinearLayout) findViewById(R.id.videoshow_box_pre); 
		
		path_myapp = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "";
		
		
		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		
		
		try {
			Log.d(TAG, "try");
			JSONObject jo = new JSONObject(myintent.getStringExtra("json"));
			int position = Integer.parseInt(jo.getString("position"));
			Log.d(TAG, "position " + position);
			JSONArray fads_ja = new JSONArray(jo.getString("fads"));
			
			FitApiDataClass fad =  FitApiDataClass.parseObject(fads_ja.getJSONObject(position));
			

			Log.d(TAG, "title " + fad._subMenuTitle);
			Log.d(TAG, "video " + fad._nameVideo);
			
			
			
			videopath = path_myapp + "/" + fad._nameVideo;
			Log.d(TAG, "video url " + videopath);
			
			file_video = new File(videopath);
			if (!file_video.isFile()) {
				Log.d(TAG, "there is not the file.");
				Utils.showDialog_downloadResource(VideoShowActivity.this, videohandler);
			} else {
				videohandler.sendEmptyMessage(0);
			} 
			
		} catch(Exception e) {
			finish();
		}  
		
		layout_videoshow_touch_control.setOnClickListener(layout_clicklistener);
		videoshow_play_intro_icn_play.setOnClickListener(pause_clicklistener);
		
	 
		
		videoshow_box_exit.setOnClickListener(pre_clicklistener); 
		videoshow_box_next.setOnClickListener(next_clicklistener); 
		videoshow_box_pre.setOnClickListener(exit_clicklistener);
		
		videoview.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("result", VideoDetailActivity.RESUL_CODE_NEXT_MOVIE);
				setResult(RESULT_OK, intent);
				finish();
			} 
	    });
		
	}
	
	
	OnClickListener next_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("result", VideoDetailActivity.RESUL_CODE_NEXT_MOVIE);
			setResult(RESULT_OK, intent);
			finish();
		} 
	};
	OnClickListener pre_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("result", VideoDetailActivity.RESUL_CODE_PRE_MOVIE);
			setResult(RESULT_OK, intent);
			finish();
		} 
	};
	OnClickListener exit_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("result", VideoDetailActivity.RESUL_CODE_STOP_MOVIE);
			setResult(RESULT_OK, intent);
			finish();
		} 
	};
	OnClickListener pause_clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (videoview != null && !videoview.isPlaying()) {
				
				layout_videoshow_control.setVisibility(View.GONE);
				layout_videoshow_touch_control.setVisibility(View.VISIBLE);
		
				videoview.start();
			}
		}

	};
	
	OnClickListener layout_clicklistener = new OnClickListener() {
 

		@Override
		public void onClick(View v) {
			if (videoview != null && videoview.isPlaying()) {
				videoview.pause();
				videoshow_rec.setText("");//
				layout_videoshow_control.setVisibility(View.VISIBLE);
				layout_videoshow_touch_control.setVisibility(View.GONE);
			}
		}
	};
	
	private final Handler videohandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0) {
				videoview.setVideoPath(videopath);
				videoview.start();
			}
			if (msg.what == -1) {
				Toast.makeText (getApplicationContext(), "다운로드 실패", 1).show();
			}  
		}
	};
}
