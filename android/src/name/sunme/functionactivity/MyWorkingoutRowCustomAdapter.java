package name.sunme.functionactivity;

import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.GlobalData;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyWorkingoutRowCustomAdapter extends ArrayAdapter<FitApiDataClass>{
	Context context;
	int resource;
	String TAG = "MyWorkingoutRow";
	FitApiDataClass[] data;
	MyWorkingoutItem[] mainData;
	int mainPosition;
	public MyWorkingoutRowCustomAdapter(Context context, int resource,
			FitApiDataClass[] data, MyWorkingoutItem[] mainData, int mainPosition) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
		this.mainPosition = mainPosition;
		this.mainData = mainData;
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);
        
        
        TextView myworkingout_title_row_row = (TextView)listItem.findViewById(R.id.myworkingout_title_row_row);
        ImageView myworkingout_row_row_bg = (ImageView)listItem.findViewById(R.id.myworkingout_row_row_bg);
        ImageView myworkingout_row_row_bg_selected = (ImageView)listItem.findViewById(R.id.myworkingout_row_row_bg_selected);
        TextView myworkingout_next_timer = (TextView)listItem.findViewById(R.id.myworkingout_next_timer);
        TextView myworkingout_next_calendar = (TextView)listItem.findViewById(R.id.myworkingout_next_calendar);
        
        
        
        myworkingout_title_row_row.setText(data[position]._subMenuTitle);
        myworkingout_row_row_bg.setImageResource(GlobalData.getBackground(data[position]._subMenuId));
        
        if(mainData[mainPosition].setting_subMenuIds[position]==true) {
        	myworkingout_row_row_bg_selected.setVisibility(View.VISIBLE);
        }
        
        
        
        String[] t_time = data[position]._exerciseTime.split(" ");
        String r_time = null;
        for(int i=0; i<t_time.length; i++) {
        	if(t_time[i].contains("Ка")) {
        		r_time = t_time[i];
        	}
        }
        if (r_time == null) r_time = data[position]._exerciseTime;
        myworkingout_next_timer.setText(r_time);
        
        
        myworkingout_next_calendar.setText(data[position]._exerciseFrequency);
        
        return listItem;
	}
	
}
