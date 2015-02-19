package name.sunme.seniorfit;

import java.util.HashMap;

import name.sunme.functionactivity.MyWorkingoutItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public class FitApiDataClass {
	private static String TAG = "FitApiDataClass";
	public String _exerciseIntensity =  null;
	public String _subMenuId =  null;
	public String _exerciseVideosTime =  null;
	public String _exerciseMethod =  null;
	public String _mainMenuId =  null;
	public String _subMenuTitle =  null;
	public String _exerciseFrequency =  null;
	public String _preSubMenuId =  null;
	public String _thumbnailUrl =  null;
	public String _exerciseTime =  null;
	public String _mainMenuTitle =  null;
	public String _nextSubMenuId =  null;
	public String _exerciseVideosUrl =  null;
	
	
	public String _time =  null;
	public String _new =  null;
	public String _machine =  null;
	public String _timeSecond =  null;
	public String _inc =  null;
	
	
	
	public String _nameVideo = null; 
	public String _haveVideo = null;
	public String _nameThumbnail = null;
	public String _haveThumbnail = null; 
	
	
	public static FitApiDataClass parseObject(JSONObject fitdatajson){
		FitApiDataClass fitapidataobj = new FitApiDataClass();
		try { fitapidataobj._exerciseIntensity = fitdatajson.getString("exerciseIntensity"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._subMenuId = fitdatajson.getString("subMenuId"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._exerciseVideosTime = fitdatajson.getString("exerciseVideosTime"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._exerciseMethod = fitdatajson.getString("exerciseMethod"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._mainMenuId = fitdatajson.getString("mainMenuId"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._subMenuTitle = fitdatajson.getString("subMenuTitle"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._exerciseFrequency = fitdatajson.getString("exerciseFrequency"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._preSubMenuId = fitdatajson.getString("preSubMenuId"); } catch (JSONException e) { e.printStackTrace(); }
		try { 
			fitapidataobj._thumbnailUrl = fitdatajson.getString("thumbnailUrl"); 
			fitapidataobj._nameThumbnail = fitapidataobj._thumbnailUrl.substring(fitapidataobj._thumbnailUrl.lastIndexOf('/')+1);
		} catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._exerciseTime = fitdatajson.getString("exerciseTime"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._mainMenuTitle = fitdatajson.getString("mainMenuTitle"); } catch (JSONException e) { e.printStackTrace(); }
		try { fitapidataobj._nextSubMenuId = fitdatajson.getString("nextSubMenuId"); } catch (JSONException e) { e.printStackTrace(); }
		try { 
			fitapidataobj._exerciseVideosUrl = fitdatajson.getString("exerciseVideosUrl"); 
			fitapidataobj._nameVideo = fitapidataobj._exerciseVideosUrl.substring(fitapidataobj._exerciseVideosUrl.lastIndexOf('/')+1);
			Log.d(TAG, "succ _exerciseVideosUrl "+fitapidataobj._exerciseVideosUrl);
		} catch (JSONException e) { 
			Log.d(TAG, "fail _exerciseVideosUrl "+fitapidataobj._exerciseVideosUrl);
			e.printStackTrace(); 
		}
		
		
		
		try { 
			JSONObject _id = fitdatajson.getJSONObject("_id"); 
			try { fitapidataobj._time = _id.getString("time"); } catch (JSONException e) { e.printStackTrace(); }
			try { fitapidataobj._new = _id.getString("new"); } catch (JSONException e) { e.printStackTrace(); }
			try { fitapidataobj._machine = _id.getString("machine"); } catch (JSONException e) { e.printStackTrace(); }
			try { fitapidataobj._timeSecond = _id.getString("timeSecond"); } catch (JSONException e) { e.printStackTrace(); }
			try { fitapidataobj._inc = _id.getString("inc"); } catch (JSONException e) { e.printStackTrace(); }
		} catch (JSONException e) { e.printStackTrace(); }
		return fitapidataobj;
	}
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();    	
		values.put("exerciseIntensity", _exerciseIntensity);
		values.put("subMenuId", _subMenuId);
		values.put("exerciseVideosTime", _exerciseVideosTime);
		values.put("exerciseMethod", _exerciseMethod);
		values.put("mainMenuId", _mainMenuId);
		values.put("subMenuTitle", _subMenuTitle);
		values.put("exerciseFrequency", _exerciseFrequency);
		values.put("preSubMenuId", _preSubMenuId);
		values.put("thumbnailUrl", _thumbnailUrl);
		values.put("exerciseTime", _exerciseTime);
		values.put("mainMenuTitle", _mainMenuTitle);
		values.put("nextSubMenuId", _nextSubMenuId);
		values.put("exerciseVideosUrl", _exerciseVideosUrl);
		
		values.put("nameVideo", _nameVideo);
		values.put("nameThumbnail", _nameThumbnail);
		
		 
		
		values.put("time", _time);
		values.put("new", _new);
		values.put("machine", _machine);
		values.put("timeSecond", _timeSecond);
		values.put("inc", _inc);
		
		return values;
	}
}
