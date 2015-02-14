package name.sunme.maindrawbar;

import java.io.File;

import name.sunme.firstexecution.TutorialActivity;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper; 

import name.sunme.maindrawbar.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	String TAG = "MainActivity";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private TextView sidebar_profile_name;
	ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawer;
	
	private DBHelper helper;
	private DBAdapter dbadapter;
	private ImageView sidebar_profile_image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate");
		helper = new DBHelper(getBaseContext());
		dbadapter = new DBAdapter(getBaseContext());
        Log.d(TAG,"start home");
        if (dbadapter.get_setting("firstsetting") == null) {
        	Log.d(TAG,"Go to get apidata");
        	Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
            startActivity(intent);
            finish();
        }
 		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity_main);           
        Log.d(TAG, "db check pass");

      
      
      

		
		
		

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList = (ListView) findViewById(R.id.left_listview);
		mDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		sidebar_profile_name = (TextView)findViewById(R.id.sidebar_profile_name);
		sidebar_profile_image = (ImageView)findViewById(R.id.sidebar_profile_image);
		final ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];
		
		String aims = "주 ";
		if (dbadapter.get_setting("dayN")!=null) {aims = aims + dbadapter.get_setting("dayN");}
		else {aims = aims + "~";}
		aims = aims + "회 하루 ";
		if (dbadapter.get_setting("goalMinutes")!=null) {aims = aims + dbadapter.get_setting("goalMinutes");}
		else {aims = aims + "~";}
		aims = aims + "분"; 
		
		drawerItem[0] = new ObjectDrawerItem(R.drawable.sidebar_aim, aims);
		drawerItem[1] = new ObjectDrawerItem(R.drawable.sidebar_timer, "운동하기");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.sidebar_graph, "기록보기");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.sidebar_gear, "설정");
		Log.d(TAG, "created menu drawbar's list");
		
		DrawerItemCustomAdapter dradapter = new DrawerItemCustomAdapter(this, R.layout.drawer_activity_item_row, drawerItem);
		
		{
			FragmentManager fragmentManager = getFragmentManager();
		    fragmentManager.beginTransaction().replace(R.id.content_frame, new WorkingoutFragment()).commit();
		}
		
		mDrawerList.setAdapter(dradapter);
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
				
			}
			private void selectItem(int position) {
				Log.d(TAG, "selectItem : " +position);
			    
			    Fragment fragment = null;
			    
			    /* do modify*/
			    switch (position) {
			    case 0:
			    	break;
			    case 1:
			        fragment = new WorkingoutFragment();
			        break;
			    case 2:
			        fragment = new RecordingFragment();
			        break;
			    case 3:
			       fragment = new SettingFragment();
			       break;
			 
			    default:
			        break;
			    } 
			    Log.d(TAG, "fragment : " +fragment+"");
			    if (fragment != null) {
			    	Log.d(TAG, "");
			        FragmentManager fragmentManager = getFragmentManager();
			        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			 
			        mDrawerList.setItemChecked(position, true);
			        Log.d(TAG, "position : " + position+", true");
			        mDrawerList.setSelection(position);
			        Log.d(TAG, "setSelection" + position);
			        getActionBar().setTitle(drawerItem[position].name);
			        Log.d(TAG, "setTitle" + drawerItem[position].name);
			        mDrawerLayout.closeDrawer(mDrawer);
			        Log.d(TAG, "closeDrawer" + mDrawerList+"");
			        
			    } else {
			        Log.e(TAG, "Error in creating fragment");
			    }
			}
		});
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(
		        this,
		        mDrawerLayout,
		        R.drawable.ic_launcher, 
		        R.string.drawer_open, 
		        R.string.drawer_close 
		        ) {
		    
		    /** Called when a drawer has settled in a completely closed state. */
		    public void onDrawerClosed(View view) {
		        super.onDrawerClosed(view);
		    }
		 
		    /** Called when a drawer has settled in a completely open state. */
		    public void onDrawerOpened(View drawerView) {
		    	
		    	loadName();
		    	loadProfilePicture();
		        super.onDrawerOpened(drawerView);
		    }
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 if (mDrawerToggle.onOptionsItemSelected(item)) {
		       return true;
		   } 
		   return super.onOptionsItemSelected(item);
	} 
	
	private void loadName() {
		String name = dbadapter.get_setting("name");
		if (name != null) { sidebar_profile_name.setText(name);	}
	}
	private void loadProfilePicture() {
		File newfile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"profilepicture.png");
        if (newfile.isFile()) {
        	sidebar_profile_image.setImageBitmap(BitmapFactory.decodeFile(newfile+""));
        }
	}
}



/** 액션바색깔바꾸기
 * //ActionBar bar = getActionBar(); 
 *     //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
 *      //bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.settings_icon));
 */
