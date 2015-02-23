package name.sunme.seniorfit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import name.sunme.seniorfit.DBAdapter;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class WalkingGraphData {
	String TAG = "WalkingData"; 
	
	public Context context; 
	
	public LineDataSet linedataset;
	
	public ArrayList<Entry> data;
	public ArrayList<String> labels;
	public String title;
	public int line_color;
	public int count;
	public int max;
	
	public Calendar days[];
	public Calendar day_base;
	
	public String db_str;
	public DBAdapter dbAdapter;
	
	public SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
	public SimpleDateFormat labelformat = new SimpleDateFormat("MM.dd");
	public WalkingGraphData(Context context, String title, String db_str, int count) {
		this.title = title;
		this.context = context;
		this.db_str = db_str;
		this.count = count;
		this.dbAdapter = new DBAdapter(context); 
	}
	public void autoLoadData(AxisDependency where, int color) {   
		loadDays(count); 
		int[] values = new int[days.length];
		labels = new ArrayList<String>();
		ArrayList<Entry> result = new ArrayList<Entry>();  
		max = 0;
		for(int i=0; i<days.length; i++) { 
			String key = db_str + dbformat.format(days[i].getTime());
			String value_str = dbAdapter.get_setting(key);
			labels.add(labelformat.format(days[i].getTime())); 
			Entry e;
			if (value_str!=null) {
				int value = Integer.parseInt(value_str);
				e = new Entry(value, i);
				if (max < value) max = value;
			} else {
				e = new Entry(0, i);
			}
			result.add(e);	
			
		} 
		data = result;
		linedataset = new LineDataSet(data, title);
		linedataset.setAxisDependency(where);
		linedataset.setColor(color);
		linedataset.setCircleColor(color);
		linedataset.setLineWidth(2f);
		linedataset.setCircleSize(4f);
		linedataset.setFillAlpha(65);
		linedataset.setFillColor(Color.WHITE);
		linedataset.setHighLightColor(Color.rgb(244, 117, 117));
	}
	
	
	public void loadDays(int count) {
		if(days!=null) return; 
		days = new Calendar[count];
		for(int i=0; i<count; i++) {
			days[i] = Calendar.getInstance();
			days[i].add(Calendar.DATE, -count+i+1);
		}
	}
	public static ArrayList<String> getLabel(Calendar[] labelDay) {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");
 
		for(int i=0; i<labelDay.length; i++) {
			result.add(format.format(labelDay[i].getTime()));
		} 
		return result;
	}
}
