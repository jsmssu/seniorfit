package name.sunme.functionactivity;

import com.example.seniorfit.R;
import com.example.seniorfit.R.id;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoShowActivity extends Activity {

	private String videopath;
	private VideoView videoview;
	
	String TAG = "VideoShowActivity";
	
	
	private String path_myapp;
	private TextView testtext;
	private String videoname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_show);
		
		
		videoview = (VideoView) findViewById(R.id.videodetail_videoview); 
		path_myapp = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "";
		testtext = (TextView)findViewById(R.id.testtext);
		
		Intent myintent = getIntent(); // 값을 받기 위한 Intent 생성
		Log.d(TAG, "onCreate FitDetail_MainMenuTitleActivity");
		if (myintent != null) {
			videoname = myintent.getStringExtra("videoname");
			
			videopath = path_myapp + "/" + videoname;
			Log.d(TAG, videopath);
			videoview.setVideoPath(videopath);
			videoview.start();
			videoview.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					finish();
				} 
		    });
			
			
		} else {
			finish();
		} 
		testtext.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Toast.makeText (getApplicationContext(), "ㅌ ㅓ ㅊ  ㅣ", 1).show();
				return false;
			}
		});
		
	}
}
