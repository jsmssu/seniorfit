package name.sunme.functionactivity;

import java.util.ArrayList;

import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OtherProgramListCustomAdapter extends ArrayAdapter<OtherProgramItem>{
	String TAG = "OtherProgramListCustomAdapter";
	Context context;
	int resource;
	ArrayList<OtherProgramItem> data;
	
	
	public OtherProgramListCustomAdapter(Context context, int resource, ArrayList<OtherProgramItem> data) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);
  

        ImageView otherprogram_listrow_today = (ImageView) listItem.findViewById(R.id.otherprogram_listrow_today);
        ImageView otherprogram_listrow_bg = (ImageView) listItem.findViewById(R.id.otherprogram_listrow_bg);
        
        TextView otherprogram_listrow_title = (TextView) listItem.findViewById(R.id.otherprogram_listrow_title);
        TextView otherprogram_listrow_time = (TextView) listItem.findViewById(R.id.otherprogram_listrow_time);
        TextView otherprogram_listrow_day = (TextView) listItem.findViewById(R.id.otherprogram_listrow_day);
        
        
        otherprogram_listrow_bg.setImageResource(data.get(position).background);
        
        if (data.get(position).isTodays) {
        	otherprogram_listrow_today.setVisibility(View.VISIBLE);
        }
        
        otherprogram_listrow_title.setText(data.get(position).title);
        otherprogram_listrow_time.setText(data.get(position).time);
        otherprogram_listrow_day.setText(data.get(position).day);
        
        return listItem;
    }
}
