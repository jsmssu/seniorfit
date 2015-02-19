package name.sunme.functionactivity;
   
 
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter; 
import name.sunme.seniorfit.Utils;
import android.app.Activity;
import android.content.Context;  
import android.os.Handler; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View; 
import android.view.View.OnTouchListener;
import android.view.ViewGroup; 
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView; 
import android.widget.ListView; 
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener; 

public class MyWorkingoutListCustomAdapter extends ArrayAdapter<MyWorkingoutItem>{
	String TAG = "MyWorkingoutListCustomAdapter";
	Context context;
	int resource;
	MyWorkingoutItem[] data;
	 
	
	String dbkey_m_fold = "m_fold_";
	String dbkey_s_fold = "s_fold_";
	Handler mHandler;
	
	int position;
	
	public MyWorkingoutListCustomAdapter(Context context, int resource, MyWorkingoutItem[] data, Handler mHandler) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data; 
		this.mHandler = mHandler;
	}
	
	
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
		this.position = position;
		
		
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(resource, parent, false);
        
        
        
        
        final TextView title = (TextView) listItem.findViewById(R.id.myworkingout_title_row);
        final ImageView myworkingout_check = (ImageView)listItem.findViewById(R.id.myworkingout_check);
        final ImageView myworkingout_fold = (ImageView)listItem.findViewById(R.id.myworkingout_fold); 
        final ListView submenu_listview = (ListView)listItem.findViewById(R.id.myworkingout_subtitle);
        
        
        
        
        title.setText(data[position].title); 
        showCheckImage(position, myworkingout_check);	
        showCheckFolded(position, myworkingout_fold, submenu_listview);
        
        
        myworkingout_check.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				data[position].reverse_setting_checked();
				data[position].save_setting_checked(context);
				showCheckImage(position, myworkingout_check);		
				return false;
			}
		}); 
        
        myworkingout_fold.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
    			data[position].reverse_setting_folded();
				data[position].save_setting_folded(context);
				showCheckFolded(position, myworkingout_fold, submenu_listview);
				return false;
			}
		}); 
        
        

        
        MyWorkingoutRowCustomAdapter mwca= new MyWorkingoutRowCustomAdapter(context, R.layout.activity_my_workingout_row_row, data[position].fads,  data, position); 
        submenu_listview.setAdapter(mwca);
        submenu_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int subPosition, long id) {
				showSubMenus(position, subPosition, view);
			}
		});
        Utils.setListViewHeightBasedOnChildren(submenu_listview);  
        return listItem;
    }
	public void showSubMenus(int position, int subPosition, View view) {
		data[position].setting_checked_subMenuIds[subPosition] = !data[position].setting_checked_subMenuIds[subPosition];
		if (data[position].setting_checked_subMenuIds[subPosition]) {
			((ImageView)view.findViewById(R.id.myworkingout_row_row_bg_selected)).setVisibility(View.VISIBLE);;	
		} else {
			((ImageView)view.findViewById(R.id.myworkingout_row_row_bg_selected)).setVisibility(View.GONE);;
		}
	}
	public void showCheckFolded(int position, ImageView mainMenuFoldImg, ListView subMenu) {
		boolean mainMenuFolded = data[position].setting_folded; 
		Log.d(TAG, "updateCheckFolded : "+ mainMenuFolded);
		if (mainMenuFolded == true) {
			mainMenuFoldImg.setImageResource(R.drawable.myworkingout_folded);
			subMenu.setVisibility(View.GONE);
    	} else {
    		mainMenuFoldImg.setImageResource(R.drawable.myworkingout_unfolded);
    		subMenu.setVisibility(View.VISIBLE);
    	}
	}
	public void showCheckImage(int position, ImageView mainMenuCheckImg) {
		boolean mainMenuCheck = data[position].setting_checked; 
    	if (mainMenuCheck == true) {
    		mainMenuCheckImg.setImageResource(R.drawable.myworkingout_checkedcircle);
    	} else {
    		mainMenuCheckImg.setImageResource(R.drawable.myworkingout_uncheckedcircle);
    	}
	}
	
}
