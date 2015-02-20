package name.sunme.maindrawbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

import name.sunme.maindrawbar.R; 
import name.sunme.seniorfit.DBAdapter;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;

public class RecordingFragment extends Fragment {
	private LineGraph recording_week_walkingline;
	DBAdapter dbAdapter;
	int[] value_linegraph; 
	String[] label_linegraph;
	
	
	
    public RecordingFragment() {
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	//do modify
        View rootView = inflater.inflate(R.layout.fragment_recording, container, false);
         
        recording_week_walkingline = (LineGraph)rootView.findViewById(R.id.recording_week_walkingline);
        dbAdapter = new DBAdapter(getActivity());
        
        
        loadLastDataForGraph();
        recording_week_walkingline.removeAllLines();
		
		Line l = new Line();
		LinePoint p;		
		
		for(int i=0; i<7; i++) {
			p = new LinePoint(); 
			p.setX(i); 
			p.setY(value_linegraph[i]); 
			l.addPoint(p); 
			p.setLabel_string(label_linegraph[i]);
		} 
		
		l.setColor(Color.parseColor("#3ec2c7"));
		
		recording_week_walkingline.setMinY(0);
		recording_week_walkingline.addLine(l);
		recording_week_walkingline.setLabelSize(30);
		recording_week_walkingline.setRangeY(0, getMaxWalking());
		recording_week_walkingline.setLineToFill(1);
        return rootView;
    }
    
	Date today;
	String db_str_walking = "wk_";
	String db_str_walking_today;
	SimpleDateFormat format = new SimpleDateFormat("MM.dd");  
	SimpleDateFormat dbformat = new SimpleDateFormat("yyyy.MM.dd");
	void loadDbKeyToday() {
		today = new Date();
		db_str_walking_today = db_str_walking+dbformat.format(today); 
	}
	
    void loadLastDataForGraph() {
    	
    	
		Date lastDay = new Date();

		
		loadDbKeyToday();
		
		
		 

		label_linegraph = new String[7];
		value_linegraph = new int[7];
		 
		int idx = 6;
		for(int i=0; i<7; i++) {
			try {
				String dbkey = db_str_walking+dbformat.format(lastDay);
				String dbvalue = dbAdapter.get_setting(dbkey); 
				value_linegraph[6-i] = Integer.parseInt(dbvalue);
			} catch (Exception e) {
				value_linegraph[6-i] = 0;
			}
			label_linegraph[6-i] = format.format(lastDay);
			
			lastDay = this.getYesterday(lastDay);
		}
	}
    
	public static Date getYesterday ( Date today )
	{
	    if ( today == null ) 
	        throw new IllegalStateException ( "today is null" );
	 
	    Date yesterday = new Date ( );
	    yesterday.setTime ( today.getTime ( ) - ( (long) 1000 * 60 * 60 * 24 ) );
	     
	    return yesterday;
	}
	
	int getMaxWalking() {
		int max = 100; 
		for(int i=0; i<7; i++) {
			if (max < value_linegraph[i]) {
				max = value_linegraph[i];
			}
		}
		return max;
	}
}