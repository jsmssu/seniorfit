package name.sunme.workingout;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.drive.internal.RemoveEventListenerRequest;
import com.google.android.gms.wearable.DataApi.DeleteDataItemsResult;

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
    
    public boolean[] setting_checked_subMenuIds;
    public boolean setting_checked = false;
    public boolean setting_folded = true;
    

    String dbkey_checked_subMenuId;
    String dbkey_checked;
    String dbkey_s_fold;
	
    // Constructor.
    public MyWorkingoutItem(String title, String mainMenuId, FitApiDataClass[] fads) {
        this.title = title;
        this.fads = fads;
        this.mainMenuId = mainMenuId; 
        
        this.dbkey_checked_subMenuId = "s_checked_"+mainMenuId+"_";
        this.dbkey_checked = "checked_"+mainMenuId;
        this.dbkey_s_fold = "fold_"+mainMenuId;
    }
    public void save_setting_checked_subMenuId(Context context, int position) {
    	DBAdapter dbAdapter = new DBAdapter(context);
    	dbAdapter.put_setting(dbkey_checked_subMenuId+position,Boolean.toString(setting_checked_subMenuIds[position]));
    }
	public void save_setting_checked(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
    	dbAdapter.put_setting(dbkey_checked,Boolean.toString(setting_checked));
	}
	public void save_setting_folded(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
    	dbAdapter.put_setting(dbkey_s_fold,Boolean.toString(setting_folded));
	}
    public void loadSetting(Context context){
    	/*DBAdapter dbAdapter = new DBAdapter(context);
    	
    	String setting_checked_s = dbAdapter.get_setting(dbkey_checked);
    	if (setting_checked_s==null) {
    		dbAdapter.put_setting(dbkey_checked, Boolean.toString(setting_checked));
    	} else {
    		setting_checked = Boolean.parseBoolean(setting_checked_s);
    	}
    	
    	String setting_folded_s = dbAdapter.get_setting(dbkey_s_fold);
    	if(setting_folded_s==null) {
    		dbAdapter.put_setting(dbkey_s_fold, Boolean.toString(setting_folded));
    	} else {
    		setting_folded = Boolean.parseBoolean(setting_folded_s);
    	} 
    	
    	
    	setting_checked_subMenuIds = new boolean[fads.length];
    	for(int i=0; i < fads.length; i++) {
    		String setting_checked_subMenuId = dbAdapter.get_setting(dbkey_checked_subMenuId+i);
    		if (setting_checked_subMenuId == null) {
    			setting_checked_subMenuIds[i] = false; 
    			dbAdapter.put_setting(dbkey_checked_subMenuId+i, Boolean.toString(setting_checked_subMenuIds[i]));
    		} else {
    			setting_checked_subMenuIds[i] = Boolean.parseBoolean(setting_checked_subMenuId);
    		}
    	} */
    	setting_checked_subMenuIds = new boolean[fads.length];
    	for(int i=0; i < fads.length; i++) {
    		setting_checked_subMenuIds[i] = false; 
    	}
    }
    
    public void reverse_setting_folded() {
    	setting_folded = !setting_folded;
    }
    public void check_setting_folded() {
    	setting_folded = true;
    }
    public void uncheck_setting_folded() {
    	setting_folded = false;
    }
    public void reverse_setting_checked() {
    	setting_checked = !setting_checked;
    }
    public void check_setting_checked() {
    	setting_checked = true;
    }
    public void uncheck_setting_checked() {
    	setting_checked = false;
    }
    public void reverse_check_subMenuTitle(int position) {
    	setting_checked_subMenuIds[position] = !setting_checked_subMenuIds[position];
    }
    public void check_setting_checked_subMenuId(int position) {
    	setting_checked_subMenuIds[position] = true;
    }
    public void uncheck_setting_checked_subMenuId(int position) {
    	setting_checked_subMenuIds[position] = false;
    }
    
	public static MyWorkingoutItem[] get_mainWorkingoutItems(Context context) {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			JSONArray jo = new JSONArray(dbAdapter.get_setting("mainMenus"));
			MyWorkingoutItem[] result = new MyWorkingoutItem[jo.length()];
			for (int i = 0; i < jo.length(); i++) {
				JSONObject jmainmenu = jo.getJSONObject(i);

				String title = jmainmenu.getString("mainMenuTitle");
				String menuId = jmainmenu.getString("mainMenuId");

				FitApiDataClass[] fads = dbAdapter
						.get_fitapidata_fromMainMenuId(menuId);
				MyWorkingoutItem mwItem = new MyWorkingoutItem(title, menuId,
						fads);
				mwItem.loadSetting(context);
				result[i] = mwItem;
			}
			return result;
		} catch (Exception e) {

		}
		return null;
	}
}
