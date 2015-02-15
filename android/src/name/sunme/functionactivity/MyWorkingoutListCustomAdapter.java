package name.sunme.functionactivity;
  
import java.util.ArrayList;

import name.sunme.maindrawbar.MyDrawerItem;
import name.sunme.maindrawbar.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyWorkingoutListCustomAdapter extends ArrayAdapter<MyWorkingoutItem>{
	Context context;
	int resource;
	ArrayList<MyWorkingoutItem> data;
	public MyWorkingoutListCustomAdapter(Context context, int resource, ArrayList<MyWorkingoutItem> data) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);
  
        TextView title = (TextView) listItem.findViewById(R.id.myworkingout_title_row);
        title.setText(data.get(position).title);  
        
        return listItem;
    }

}
