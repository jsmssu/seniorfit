package name.sunme.functionactivity;
  
import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.maindrawbar.MyDrawerItem;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.seniorfit.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyWorkingoutListCustomAdapter extends ArrayAdapter<MyWorkingoutItem>{
	String TAG = "MyWorkingoutListCustomAdapter";
	Context context;
	int resource;
	MyWorkingoutItem[] data;
	
	private DBAdapter dbApter;
	
	String dbkey_m_fold = "m_fold_";
	String dbkey_s_fold = "s_fold_";
	
	boolean[] row_folded;
	
	public MyWorkingoutListCustomAdapter(Context context, int resource, MyWorkingoutItem[] data) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
		// TODO Auto-generated constructor stub
		row_folded = new boolean[data.length];
		dbApter = new DBAdapter(context);
	}
	
	
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);
  
        TextView title = (TextView) listItem.findViewById(R.id.myworkingout_title_row);
        title.setText(data[position].title);  
        
        
        
        
        
        
        
        final ImageView myworkingout_check = (ImageView)listItem.findViewById(R.id.myworkingout_check); 
        updateCheckImage(position, myworkingout_check);	
        myworkingout_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				data[position].reverse_check_mainMenuTitle();
				updateCheckImage(position, myworkingout_check);		
			}
		});
        
        
        
        
        
      //리스트뷰 인 리스트뷰를 위해!
        final ListView listview = (ListView)listItem.findViewById(R.id.myworkingout_subtitle);
        
        
        final ImageView myworkingout_fold = (ImageView)listItem.findViewById(R.id.myworkingout_fold); 
        myworkingout_fold.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listview.getVisibility() == View.GONE) {
					row_folded[position] = true;
					listview.setVisibility(View.VISIBLE);
					myworkingout_fold.setImageResource(R.drawable.myworkingout_folded);
				} 
				else if (listview.getVisibility() == View.VISIBLE) {
					row_folded[position] = false;
					listview.setVisibility(View.GONE);
					myworkingout_fold.setImageResource(R.drawable.myworkingout_unfolded);
				} 
			}
		}); 
         
        if (row_folded[position] == true) {
        	listview.setVisibility(View.VISIBLE);
        }
        
        MyWorkingoutRowCustomAdapter mwca= new MyWorkingoutRowCustomAdapter(context, R.layout.activity_my_workingout_row_row, data[position].fads,  data, position); 
        data[position].loadSetting(context);
        listview.setAdapter(mwca);
        listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int subPosition, long id) {
				data[position].setting_subMenuIds[subPosition] = !data[position].setting_subMenuIds[subPosition];
				if (data[position].setting_subMenuIds[subPosition]) {
					((ImageView)view.findViewById(R.id.myworkingout_row_row_bg_selected)).setVisibility(View.VISIBLE);;	
				} else {
					((ImageView)view.findViewById(R.id.myworkingout_row_row_bg_selected)).setVisibility(View.GONE);;
				}
			}
		});
        Utils.setListViewHeightBasedOnChildren(listview);  
        return listItem;
    }
	
	public void updateCheckImage(int position, ImageView mainMenuCheckImg) {
		boolean mainMenuCheck = data[position].setting_checked; 
    	if (mainMenuCheck == true) {
    		mainMenuCheckImg.setImageResource(R.drawable.myworkingout_checkedcircle);
    	} else {
    		mainMenuCheckImg.setImageResource(R.drawable.myworkingout_uncheckedcircle);
    	}
	}
}
