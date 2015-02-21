package name.sunme.seniorfit;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor; 
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;  



public class DBAdapter {
	String TAG = "DBAdapter"; 
    private Context context;
    private SQLiteDatabase db;


    public DBAdapter(Context context) {
        this.context = context;
        this.open();
    } 
    public void open() throws SQLException {
        try {
            db = (new DBHelper(context).getWritableDatabase());
        } catch(SQLiteException e) { 
        	e.printStackTrace();
        }
    }
    public int get_settingCount(String key) {
    	Cursor cur = db.query(DBHelper.SETTING_TABLE_NAME, null, "key=?", new String[] { key }, null, null, null);
        return cur.getCount();
    }
    public String[] get_fitapiUrls() {
    	String selectQuery = "SELECT thumbnailUrl, exerciseVideosUrl FROM " + DBHelper.FITDATA_TABLE_NAME; 
        Cursor cur = db.rawQuery(selectQuery, null);
        if (cur.getCount() > 0) {
        	String[] result = new String[cur.getCount()*2]; 
        	int i = 0;
    		while(cur.moveToNext()) {
        		result[i] = cur.getString(cur.getColumnIndex("thumbnailUrl")); i = i + 1;
        		result[i] = cur.getString(cur.getColumnIndex("exerciseVideosUrl")); i = i + 1;
    		}	
    		return result;
    	}
    	return null;
    }

    public FitApiDataClass[] get_fitapidata_fromMainMenuId(String mainMenuId) {
    	String selectQuery = "SELECT * FROM " + DBHelper.FITDATA_TABLE_NAME + " Where mainMenuId = ?"; 
        Cursor cur = db.rawQuery(selectQuery, new String[]{mainMenuId});
        FitApiDataClass[] result = new FitApiDataClass[cur.getCount()];
    	if (cur.getCount() > 0) {
    		for(int i=0; cur.moveToNext(); i++) {
    			result[i] = new FitApiDataClass();
    			result[i]._exerciseIntensity = cur.getString(0); 
    			result[i]._subMenuId = cur.getString(1); 
    			result[i]._exerciseVideosTime = cur.getString(2); 
    			result[i]._exerciseMethod = cur.getString(3); 
    			result[i]._mainMenuId = cur.getString(4); 
    			result[i]._subMenuTitle = cur.getString(5); 
    			result[i]._exerciseFrequency = cur.getString(6); 
    			result[i]._time = cur.getString(7); 
    			result[i]._exerciseVideosUrl = cur.getString(8); 
    			result[i]._new = cur.getString(9); 
    			result[i]._machine = cur.getString(10); 
    			result[i]._timeSecond = cur.getString(11); 
    			result[i]._inc = cur.getString(12); 
    			result[i]._preSubMenuId = cur.getString(13); 
    			result[i]._thumbnailUrl = cur.getString(14); 
    			result[i]._exerciseTime = cur.getString(15); 
    			result[i]._mainMenuTitle = cur.getString(16); 
    			result[i]._nextSubMenuId = cur.getString(17); 
    			result[i]._nameVideo = cur.getString(18); 
    			result[i]._nameThumbnail = cur.getString(19);  
    		}	 
    	}
    	return result;
    }
    public FitApiDataClass get_fitapidata_fromSubMenuId(String subMenuId) {
    	String selectQuery = "SELECT * FROM " + DBHelper.FITDATA_TABLE_NAME + " Where subMenuId = ?"; 
        Cursor cur = db.rawQuery(selectQuery, new String[]{subMenuId});
    	if (cur.getCount() > 0) {
    		cur.moveToNext();
    		FitApiDataClass result = new FitApiDataClass();
    		result._exerciseIntensity = cur.getString(cur.getColumnIndex("exerciseIntensity"));  
    		result._subMenuId = cur.getString(cur.getColumnIndex("subMenuId"));  
    		result._exerciseVideosTime = cur.getString(cur.getColumnIndex("exerciseVideosTime"));  
    		result._exerciseMethod = cur.getString(cur.getColumnIndex("exerciseMethod"));  
    		result._mainMenuId = cur.getString(cur.getColumnIndex("mainMenuId"));  
    		result._subMenuTitle = cur.getString(cur.getColumnIndex("subMenuTitle"));  
    		result._exerciseFrequency = cur.getString(cur.getColumnIndex("exerciseFrequency"));  
    		result._time = cur.getString(cur.getColumnIndex("time"));  
    		result._exerciseVideosUrl = cur.getString(cur.getColumnIndex("exerciseVideosUrl"));  
    		result._new = cur.getString(cur.getColumnIndex("new"));  
    		result._machine = cur.getString(cur.getColumnIndex("machine"));  
    		result._timeSecond = cur.getString(cur.getColumnIndex("timeSecond"));  
    		result._inc = cur.getString(cur.getColumnIndex("inc"));  
    		result._preSubMenuId = cur.getString(cur.getColumnIndex("preSubMenuId"));  
    		result._thumbnailUrl = cur.getString(cur.getColumnIndex("thumbnailUrl"));  
    		result._exerciseTime = cur.getString(cur.getColumnIndex("exerciseTime")); 
    		result._mainMenuTitle = cur.getString(cur.getColumnIndex("mainMenuTitle"));  
    		result._nextSubMenuId = cur.getString(cur.getColumnIndex("nextSubMenuId"));  
    		result._nameVideo = cur.getString(cur.getColumnIndex("nameVideo"));  
    		result._nameThumbnail = cur.getString(cur.getColumnIndex("nameThumbnail")); 
    		return result;
    	}
    	return null;
    }
    public FitApiDataClass[] get_fitapidata_all() {
    	String TAG = "get_fitapidata_all";
    	String selectQuery = "SELECT * FROM " + DBHelper.FITDATA_TABLE_NAME;  
        Cursor cur = db.rawQuery(selectQuery, null); 
    	if (cur.getCount() > 0) {
    		FitApiDataClass[] result = new FitApiDataClass[cur.getCount()];
    		for(int i=0; cur.moveToNext(); i++) {
    			result[i] = new FitApiDataClass();
    			result[i]._exerciseIntensity = cur.getString(cur.getColumnIndex("exerciseIntensity"));  
    			result[i]._subMenuId = cur.getString(cur.getColumnIndex("subMenuId"));  
    			result[i]._exerciseVideosTime = cur.getString(cur.getColumnIndex("exerciseVideosTime"));  
    			result[i]._exerciseMethod = cur.getString(cur.getColumnIndex("exerciseMethod"));  
    			result[i]._mainMenuId = cur.getString(cur.getColumnIndex("mainMenuId"));  
    			result[i]._subMenuTitle = cur.getString(cur.getColumnIndex("subMenuTitle"));  
    			result[i]._exerciseFrequency = cur.getString(cur.getColumnIndex("exerciseFrequency")); 
    			result[i]._time = cur.getString(cur.getColumnIndex("time"));  
    			result[i]._exerciseVideosUrl = cur.getString(cur.getColumnIndex("exerciseVideosUrl")); 
    			result[i]._new = cur.getString(cur.getColumnIndex("new")); 
    			result[i]._machine = cur.getString(cur.getColumnIndex("machine"));  
    			result[i]._timeSecond = cur.getString(cur.getColumnIndex("timeSecond"));  
    			result[i]._inc = cur.getString(cur.getColumnIndex("inc"));  
    			result[i]._preSubMenuId = cur.getString(cur.getColumnIndex("preSubMenuId"));  
    			result[i]._thumbnailUrl = cur.getString(cur.getColumnIndex("thumbnailUrl"));  
    			result[i]._exerciseTime = cur.getString(cur.getColumnIndex("exerciseTime"));  
    			result[i]._mainMenuTitle = cur.getString(cur.getColumnIndex("mainMenuTitle"));  
    			result[i]._nextSubMenuId = cur.getString(cur.getColumnIndex("nextSubMenuId"));  
    			result[i]._nameVideo = cur.getString(cur.getColumnIndex("nameVideo"));  
    			result[i]._nameThumbnail = cur.getString(cur.getColumnIndex("nameThumbnail"));  
    		}	
    		return result;
    	} 
    	return null;
    }
    public String[][] get_fitapidataIds() {
    	String selectQuery = "SELECT mainMenuId, subMenuId FROM " + DBHelper.FITDATA_TABLE_NAME; 
        Cursor cur = db.rawQuery(selectQuery, null);
    	if (cur.getCount() > 0) {
    		String[][] result = new String[cur.getCount()][2];
    		for(int i=0; cur.moveToNext(); i++) {
        		result[i][0] = cur.getString(0);
        		result[i][1] = cur.getString(1); 
    		}	
    		return result;
    	}
    	return null;
    }
    public int get_fitapidataCount(String mainMenuId, String subMenuId) {
    	Cursor cur = db.query(DBHelper.FITDATA_TABLE_NAME, null, "mainMenuId=? and subMenuId=?", new String[] { mainMenuId,  subMenuId }, null, null, null);
        return cur.getCount();
    }
    public void put_setting(String key, String value) {
    	ContentValues values = new ContentValues();
    	values.put("value", value);
    	if (get_settingCount(key) == 0) {
    		values.put("key", key);
    		db.insert(DBHelper.SETTING_TABLE_NAME, null, values); 
    	} else {
    		db.update(DBHelper.SETTING_TABLE_NAME, values, "key=?", new String[] { key } ); // new String[]{name} 
    	} 
    }
    public void put_fitapidata(ContentValues values) {//subMenuId
    	String mainMenuId = values.getAsString("mainMenuId");
    	String subMenuId = values.getAsString("subMenuId");
    	if (get_fitapidataCount(mainMenuId, subMenuId) == 0) {
    		db.insert(DBHelper.FITDATA_TABLE_NAME, null, values); 
    	} else {
    		db.update(DBHelper.FITDATA_TABLE_NAME, values, "mainMenuId=? and subMenuId=?", new String[] { mainMenuId,  subMenuId } );  
    	} 
    }
    public String get_setting(String key) {
    	Cursor cur = db.query(DBHelper.SETTING_TABLE_NAME, null, "key=?", new String[] { key }, null, null, null);
    	if(cur.getCount() == 0){ 
            return null;
        }  
    	cur.moveToNext(); 
        return cur.getString(2); 
    }
}
