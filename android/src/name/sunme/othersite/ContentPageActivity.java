package name.sunme.othersite;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import name.sunme.seniorfit.JsonOpener;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentPageActivity extends Activity {

	String TAG = "ContentPageActivity";
	private JSONObject postInfo;//title, content, custom_fields
	private JSONObject postJson;
	String post_id;
	String video_url;
	ImageView contentpage_videobutton;
	
	WebView contentpage_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
		setContentView(R.layout.activity_content_page);
		contentpage_view = (WebView)findViewById(R.id.contentpage_view);
		contentpage_videobutton = (ImageView)findViewById(R.id.contentpage_videobutton);
		Log.d(TAG, "oncreated");
		Intent myIntent = getIntent();
		try {
			Log.d(TAG, "postid : "+myIntent.getStringExtra("postInfo"));
			postInfo = new JSONObject(myIntent.getStringExtra("postInfo"));
			
			post_id = postInfo.getString("id");
			
			Log.d(TAG, "postid : "+post_id);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
		String url = "http://sunme.name/api/get_post/?post_id="+post_id+"&&include=title,content,custom_fields";
		Log.d(TAG, "url : " + url);
		(new JsonOpener(this, postHandler, url)).open();
		contentpage_videobutton.setOnClickListener(videostart_listener);
	}
	
	OnClickListener videostart_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(video_url);
            intent.setDataAndType(uri, "video/*");
            startActivity(intent);
		}
	};
	
	private final Handler postHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Log.d(TAG, "msg : " +msg.what);
			if (msg.what == -1) {
				return;
			} else if (msg.what == 1) {
				Bundle bundle = msg.getData();
				try {
					JSONObject jo = new JSONObject(bundle.getString("result"));  
					postJson = jo.getJSONObject("post");
					//contentpage_view.getSettings().setJavaScriptEnabled(true);
					//contentpage_view.setWebChromeClient(new WebChromeClient());
					//contentpage_view.setWebViewClient(new WebViewClient());		 
					
					contentpage_view.loadData(postJson.getString("content"), "text/html; charset=UTF-8", null);
					try {
						video_url = postJson.getJSONObject("custom_fields").getJSONArray("video_url").getString(0);
						contentpage_videobutton.setVisibility(View.VISIBLE);
					} catch (Exception e){}
					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}
