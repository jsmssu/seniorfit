package name.sunme.maindrawbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;

import name.sunme.firstexecution.Setup4Activity;
import name.sunme.map.GoogleMapActivity;
import name.sunme.seniorfit.UrlOpenerBasic;
import name.sunme.seniorfit.UrlOpenerProgress;
import name.sunme.seniorfit.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ForaWalkFragment extends Fragment {
	ImageView forawalk_start;
	
	String TAG = "ForaWalkFragment";
	
	public ForaWalkFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {

		// do modify 
		View rootView = inflater.inflate(R.layout.fragment_fora_walk,
				container, false);
		forawalk_start = (ImageView)rootView.findViewById(R.id.forawalk_start);
		forawalk_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				String apiurl = "http://openapi.seoul.go.kr:8088/6e66446956696c6f39336f4c6b7058/json/GeoInfoWalkwayWGS/0/100";
				new UrlOpenerBasic(responsehandler,apiurl).open();
			}
			
		});
		return rootView;
	}
	
	private final Handler responsehandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == -1) {
				 
			} else { 
				Bundle bundle = msg.getData();
				String result = bundle.getString("result");  
				Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
				intent.putExtra("json", result);
				startActivity(intent);
			}  
		}
	};
}
