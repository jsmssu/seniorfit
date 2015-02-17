package name.sunme.functionactivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.FitApiDataClass;

public class OtherProgramItem {
	String title = null;
	String time = null;
	String day = null;
	boolean isTodays = false;
	Integer background = null;
	FitApiDataClass[] fads = null;
	
	public OtherProgramItem() {
		
	}
	public OtherProgramItem(String title, String time, String day, 
			FitApiDataClass[] fads, Integer background, boolean isTodays) {
		this.title = title;
		this.time = time;
		this.day = day;
		this.fads = fads;
		this.background = background;
		this.isTodays = isTodays;
		if (background == null) {
			background = R.drawable.otherprogram_bg1;
		}
	}
	public String getJson_submenuids(){ 
		JSONArray ja = new JSONArray();
		for(int i=0; i<fads.length; i++) {
			ja.put(fads[i]._subMenuId);	
		}
		return ja.toString();
	}
	public String toJson() {
		JSONObject jo = new JSONObject();
		try { jo.put("title", title); } catch (Exception e) {};
		try { jo.put("time", time); } catch (Exception e) {};
		try { jo.put("day", day); } catch (Exception e) {};
		try { jo.put("isTodays", isTodays); } catch (Exception e) {};
		try { jo.put("background", background); } catch (Exception e) {};
		try {
			JSONArray ja = new JSONArray();
			for(int i=0; i<fads.length; i++) {
				ja.put(fads[i]._subMenuId);	
			}
			jo.put("subMenuIds", ja); 
		} catch (Exception e) {};
		return jo.toString();
	}
	
	public static OtherProgramItem fromJSON(Context context, JSONObject jo){
		OtherProgramItem opi = new OtherProgramItem();
		try { opi.title = jo.getString("title"); } catch (JSONException e) {}
		try { opi.time = jo.getString("time"); } catch (JSONException e) {}
		try { opi.day = jo.getString("day"); } catch (JSONException e) {}
		try { opi.day = jo.getString("day"); } catch (JSONException e) {}
		try { opi.isTodays = jo.getBoolean("isTodays"); } catch (JSONException e) {}
		try { opi.background = jo.getInt("background"); } catch (JSONException e) {}
		try {
			JSONArray ja = jo.getJSONArray("subMenuIds");
			opi.fads = new FitApiDataClass[ja.length()];
			DBAdapter dbAdapter = new DBAdapter(context);
			for(int i=0; i<ja.length(); i++) {
				opi.fads[i] = dbAdapter.get_fitapidata_fromSubMenuId(ja.getString(i));
			}
		} catch (JSONException e) {}
		return opi;
	}
	public String getSubMenuIds() {
		JSONArray jo = new JSONArray();
		for(int i=0; i<fads.length; i++) {
			jo.put(fads[i]._subMenuId);	
		}
		return jo.toString();
	}
}
