package name.sunme.seniorfit;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import name.sunme.firstexecution.Setup4Activity;
import name.sunme.maindrawbar.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Utils {
	
	public static void saveFitApiData(Context context, String input_data) {
		String TAG = "saveFitApiData";
		DBHelper helper = new DBHelper(context);
		DBAdapter adapter = new DBAdapter(context);
		JSONArray jsonarray;
		
		ArrayList<String> mainMenuIds = new ArrayList<String>();
		JSONArray jmainMenuIds = new JSONArray();
		ArrayList<String> subMenuIds = new ArrayList<String>();
		JSONArray jsubMenuIds = new JSONArray();
		try {
			jsonarray = new JSONArray(input_data);
			for (int i=0; i<jsonarray.length(); i++) {
				FitApiDataClass fitapidata = FitApiDataClass.parseObject(jsonarray.getJSONObject(i));
				
				if (mainMenuIds.contains(fitapidata._mainMenuId)==false) {
					mainMenuIds.add(fitapidata._mainMenuId);
					JSONObject jo = new JSONObject();
					jo.put("mainMenuId", fitapidata._mainMenuId);
					jo.put("mainMenuTitle", fitapidata._mainMenuTitle);
					jmainMenuIds.put(jo);
				}
				if (subMenuIds.contains(fitapidata._subMenuId)==false) {
					subMenuIds.add(fitapidata._subMenuId);
					JSONObject jo = new JSONObject();
					jo.put("subMenuId", fitapidata._subMenuId);
					jo.put("subMenuTitle", fitapidata._subMenuTitle);
					jsubMenuIds.put(jo);
				}
				adapter.put_fitapidata(fitapidata.getContentValues());
				
			}
			Log.d(TAG, "subMenus" + jsubMenuIds.toString());
			Log.d(TAG, "mainMenus" + jmainMenuIds.toString());
			adapter.put_setting("subMenus", jsubMenuIds.toString());
			adapter.put_setting("mainMenus", jmainMenuIds.toString());
			
			Log.d(TAG, "put api data");
			adapter.put_setting("firstsetting", "true");
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	
	
	public static Bitmap getSquareBitmap(Bitmap srcBmp) {
        Bitmap dstBmp;
        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();
        if (srcBmp.getWidth() >= srcBmp.getHeight()){
            dstBmp = Bitmap.createBitmap(srcBmp, width/2 - height/2, 0, height, height);
        }else{
            dstBmp = Bitmap.createBitmap(srcBmp, 0, height/2 - width/2, width, width);
        }
        return dstBmp;
    }
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);


        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 200, 200, 200);
        final int color = Color.GRAY;
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();
        return output;
    }
    public static byte[] bitmapToByteArray( Bitmap mbitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        mbitmap.compress( Bitmap.CompressFormat.PNG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }
    
    public static void hideSoftKeyboard(Activity activity) {
    	((InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE))
    	.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public static String timeToString(int hourOfDay, int minute) {
    	String t = "";
		if(hourOfDay < 12) {
			t = t + "���� ";
		} else {
			hourOfDay = hourOfDay-12;
			t = t + "���� ";
		}
		if (hourOfDay == 0) hourOfDay = 12;
		t = t + String.format("%02d", hourOfDay) +":" + String.format("%02d", minute);
		return t;
    } 
    
    
    
    
    
    public static float getBMI_number(float weight, float height_cm) {
    	if (height_cm < 10) {return 0;}
    	float bmi = weight/(height_cm/100)/(height_cm/100);
    	return  bmi;
    }
    
    
    public static String getBMI_string(float bmi) {
		if (bmi<=18.5) {
			return "��ü��"; 
		} else if (bmi<23) {
			return "����ü��"; 
		} else if (bmi<25) {
			return "��ü��";
		} else if (bmi<30) {
			return "��"; 
		} else {
			return "����"; 
		} 
		 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void downloadResource(Context context, Handler downloadendhandler) {
		String TAG = "downloadResource";
		DBHelper helper = new DBHelper(context);
		DBAdapter adapter = new DBAdapter(context);
		String[] urls = adapter.get_fitapiUrls();
		for (String url_str : urls) {
			Log.d(TAG, "download start : " + url_str);
		}
		new DownloadFileAsync(context, downloadendhandler).execute(urls);		
	}
    public static void showDialog_downloadResource(final Context context, final Handler downloadendhandler) {
    	final String TAG = "showDialog_downloadResource";
		new AlertDialog.Builder(context).setMessage("���ҽ��� �ٿ�ްڽ��ϴ�. �����͸� ���� ����ϴ� �������̸� �����ϰ� Ȯ���� �����ּ���.")
	    .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            Log.d(TAG, "Ȯ�� Ŭ��");
	            downloadResource(context, downloadendhandler);
	            dialog.dismiss();
	        }
	    })
	    .setNegativeButton("���", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            Log.d(TAG, "��� Ŭ��");
	            downloadendhandler.sendEmptyMessage(-1);
	            dialog.dismiss();
	        }
	    })
	    .show();
	} 
}
