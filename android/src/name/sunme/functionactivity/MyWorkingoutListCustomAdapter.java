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

public class MyWorkingoutListCustomAdapter extends ArrayAdapter<MyWorkingoutItem>{
	String TAG = "MyWorkingoutListCustomAdapter";
	Context context;
	int resource;
	ArrayList<MyWorkingoutItem> data;
	
	private DBAdapter adapter;
	
	
	public MyWorkingoutListCustomAdapter(Context context, int resource, ArrayList<MyWorkingoutItem> data) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
		// TODO Auto-generated constructor stub
		
		adapter = new DBAdapter(context);
	}
	
	
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);
  
        TextView title = (TextView) listItem.findViewById(R.id.myworkingout_title_row);
        title.setText(data.get(position).title);  
        
        
        
        
        
        //리스트뷰 인 리스트뷰를 위해!
        final ListView listview = (ListView)listItem.findViewById(R.id.myworkingout_subtitle);
        ArrayList<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>(); 
        ArrayList<FitApiDataClass> fads = data.get(position).fads;
        Log.d(TAG, "타이틀 : "+title + ", 하위메뉴 : "+fads.size() + "개");
        for(int i=0 ; i<fads.size(); i++) {
        	HashMap<String, String> hm = new HashMap<String, String>();
        	hm.put("title", fads.get(i)._subMenuTitle);
        	listdata.add(hm);
        	Log.d(TAG, "하위메뉴 : "+fads.get(i)._subMenuTitle);
        	//데이터에서 타이틀만뽑아야지 일단.
        }
        SimpleAdapter simpleadapter = new SimpleAdapter(
				context,  
				listdata,
        		R.layout.activity_my_workingout_row_row,
        		new String[]{"title"}, 
        		new int[]{R.id.myworkingout_title_row_row});
        listview.setAdapter(simpleadapter);
        
        
        Utils.setListViewHeightBasedOnChildren(listview); 
        
        
         
        
        
        final ImageView myworkingout_fold = (ImageView)listItem.findViewById(R.id.myworkingout_fold); 
        myworkingout_fold.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listview.getVisibility() == View.GONE) {
					listview.setVisibility(View.VISIBLE);
					myworkingout_fold.setImageResource(R.drawable.myworkingout_unfolded);
				} 
				else if (listview.getVisibility() == View.VISIBLE) {
					listview.setVisibility(View.GONE);
					myworkingout_fold.setImageResource(R.drawable.myworkingout_folded);
				} 
			}
		});
        
        
        ImageView myworkingout_check = (ImageView)listItem.findViewById(R.id.myworkingout_check); 
        data.get(position).radioImage = myworkingout_check;
        
        
        myworkingout_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Log.d(TAG, "mwiListLen : "+data.size());
				for(int i=0; i<data.size(); i++) {
					if(i == position) {data.get(i).setCheck();}
					else {data.get(i).resetCheck();}
				} 
			}
		});

        /*
        String myworkingout = adapter.get_setting("myworkingout");
        if (myworkingout == null) {
        	myworkingout = "0";
        	adapter.put_setting("myworkingout", myworkingout);
        }*/
        return listItem;
    }
}
