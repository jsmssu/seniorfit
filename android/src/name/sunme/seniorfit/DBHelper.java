package name.sunme.seniorfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	String TAG = "DBHelper"; 
    SQLiteDatabase db;
    private static final String DB_NAME = "silverfit.db";
    static final String SETTING_TABLE_NAME = "SettingTable";
    static final String FITDATA_TABLE_NAME = "FitApiDataTable";
    private static final int DB_VER = 1;
    String sql_fitdata = "CREATE TABLE " + FITDATA_TABLE_NAME +" (" +
            "exerciseIntensity TEXT, " +
            "subMenuId TEXT, " +
            "exerciseVideosTime TEXT, " +
            "exerciseMethod TEXT, " +
            "mainMenuId TEXT, " +
            "subMenuTitle TEXT, " +
            "exerciseFrequency TEXT, " +
            "time TEXT, " +
            "exerciseVideosUrl TEXT," +
            "new TEXT, " +
            "machine TEXT, " +
            "timeSecond TEXT, " +
            "inc TEXT, " +
            "preSubMenuId TEXT, " +
            "thumbnailUrl TEXT, " +
            "exerciseTime TEXT, " +
            "mainMenuTitle TEXT, " +
            "nextSubMenuId TEXT, " + 
            
			"nameVideo TEXT, " + 
            "nameThumbnail TEXT, " +
            "_id INTEGER primary key autoincrement);";
    String sql_setting = "CREATE TABLE " + SETTING_TABLE_NAME +" (" +
            "_id INTEGER primary key autoincrement, " +
            "key TEXT, " +
            "value TEXT);";
    public DBHelper(Context context) {
        super(context,DB_NAME,null,DB_VER); 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    	db.execSQL(sql_setting);
        db.execSQL(sql_fitdata);
        initData();
    }
    void initData() {
    	if (db == null) {
    		db = getWritableDatabase();
    	}
    	ContentValues values = new ContentValues();
    	values.put("key", "myProgram");
    	values.put("value", "하루 운동");
    	db.insert(DBHelper.SETTING_TABLE_NAME, null, values);
    }
    
    void createSettingTable() {
    	if (db == null) {
    		db = getWritableDatabase();
    	}
        db.execSQL(sql_setting);
    }
    public void dropSettingTable() {
    	if (db == null) {
    		db = getWritableDatabase();
    	}
    	db.execSQL("drop table if exists "+SETTING_TABLE_NAME);
    }
    
    public void createFitDataTable() {
        db.execSQL(sql_fitdata);
    }
    void dropFitDataTable() {
    	db.execSQL("drop table if exists "+FITDATA_TABLE_NAME);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropSettingTable();
        dropFitDataTable();
        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}
