package name.sunme.maindrawbar;

import name.sunme.seniorfit.DBHelper;
import name.sunme.setting.SettingAlarmActivity;
import name.sunme.setting.SettingGoalActivity;
import name.sunme.setting.SettingProfileActivity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingFragment extends Fragment {
	
	
	
	String TAG = "SettingFragment";
	
	private LinearLayout setting_btn_profile;
	private LinearLayout setting_btn_goal;
	private LinearLayout setting_btn_alarm;
	private LinearLayout setting_btn_changeWorkingout;
	private LinearLayout setting_btn_sendmail;
	private LinearLayout setting_btn_help;
	private LinearLayout setting_btn_logout;
	
	
    public SettingFragment() {
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	//do modify
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        
        setting_btn_profile = (LinearLayout)rootView.findViewById(R.id.setting_btn_profile);
        setting_btn_goal = (LinearLayout)rootView.findViewById(R.id.setting_btn_goal);
        setting_btn_alarm = (LinearLayout)rootView.findViewById(R.id.setting_btn_alarm);
        setting_btn_changeWorkingout = (LinearLayout)rootView.findViewById(R.id.setting_btn_changeWorkingout);
        setting_btn_sendmail = (LinearLayout)rootView.findViewById(R.id.setting_btn_sendmail);
        setting_btn_help = (LinearLayout)rootView.findViewById(R.id.setting_btn_help);
        setting_btn_logout = (LinearLayout)rootView.findViewById(R.id.setting_btn_logout);
        
        
        setting_btn_profile.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingProfileActivity.class);
				getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_SETTING_PROFILE);
			}
		});
        setting_btn_goal.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingGoalActivity.class);
                startActivity(intent); 
			}
		});
        setting_btn_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
        setting_btn_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingAlarmActivity.class);
				getActivity().startActivity(intent);
			}
		});
        return rootView;
    }
}