package name.sunme.functionactivity;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.FitApiDataClass;

public class MyWorkingoutItem {
	String TAG = "MyWorkingoutItem";
	public String title;
	public String mainMenuId;
	  
	
    public FitApiDataClass[] fads;
    public boolean[] setting_subMenuIds;
    public boolean setting_checked;
    
    String dbkey_m_fold = "m_fold_";
	String dbkey_s_fold = "s_fold_";
	
    // Constructor.
    public MyWorkingoutItem(String title, String mainMenuId, FitApiDataClass[] fads) {
        this.title = title;
        this.fads = fads;
        this.mainMenuId = mainMenuId; 
    }
    public void loadSetting(Context context){
    	DBAdapter dbAdapter = new DBAdapter(context);
    	setting_subMenuIds = new boolean[fads.length];
    	for(int i=0; i < fads.length; i++) {
    		try { setting_subMenuIds[i] = Boolean.parseBoolean(dbAdapter.get_setting(dbkey_s_fold+i)); }
    		catch (Exception e) { setting_subMenuIds[i] = false; }
    	}
    }
    public void reverse_check_mainMenuTitle() {
    	setting_checked = !setting_checked;
    }
    public void reverse_check_subMenuTitle(int position) {
    	setting_subMenuIds[position] = !setting_subMenuIds[position];
    } 
    public void check_mainMenuTitle() {
    	setting_checked = true;
    }
    public void check_subMenuTitle(int position) {
    	setting_subMenuIds[position] = true;
    }
    public void uncheck_mainMenuTitle() {
    	setting_checked = false;
    }
    public void uncheck_subMenuTitle(int position) {
    	setting_subMenuIds[position] = false;
    }
}
