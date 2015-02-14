package name.sunme.maindrawbar;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class RecordingFragment extends Fragment {
	 
    public RecordingFragment() {
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	//do modify
        View rootView = inflater.inflate(R.layout.fragment_recording, container, false);
 
        return rootView;
    }
 
}