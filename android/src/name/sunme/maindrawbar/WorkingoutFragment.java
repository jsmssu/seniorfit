package name.sunme.maindrawbar;

import name.bagi.levente.pedometer.Pedometer;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WorkingoutFragment extends Fragment {
	String TAG = "WorkingoutFragment";
	Button button_stretching;
	Button button_walking;
    public WorkingoutFragment() {
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	//do modify
        View rootView = inflater.inflate(R.layout.fragment_workingout, container, false);
        button_stretching = (Button)rootView.findViewById(R.id.button_stretching);
        button_walking = (Button)rootView.findViewById(R.id.button_walking);
        Log.d(TAG,"button_stretching : "+button_stretching);
        Log.d(TAG,"button_stretching : "+button_walking);
        button_stretching.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG,"onClick button_stretching");
				Intent intent = new Intent(getActivity(), StretchingActivity.class);
				Log.d(TAG,"onClick StretchingActivity" + StretchingActivity.class);
                startActivity(intent); 
			}
		});
        button_walking.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG,"onClick button_walking");
				Intent intent = new Intent(getActivity(), Pedometer.class);
				Log.d(TAG,"onClick StretchingActivity" + Pedometer.class);
                startActivity(intent);
			}
		});
        return rootView;
    }
 
}