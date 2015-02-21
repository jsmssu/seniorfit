package name.sunme.maindrawbar;

import java.io.File;
 

import name.sunme.seniorfit.DBAdapter;
import name.sunme.setting.SettingGoalActivity;
import name.sunme.setting.SettingProfileActivity;
import name.sunme.firstexecution.FacebookLoginActivity;
import name.sunme.maindrawbar.R;
import name.sunme.othersite.OtherSiteFragment; 
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory; 
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView; 

public class MainActivity extends Activity {
	
	final static int REQUEST_CODE_SETTING_PROFILE = 2;
	
	
	String TAG = "MainActivity";
	
	
	
	private DrawerLayout drawer_layout;
	private ListView sidebar_left_listview;
	private TextView sidebar_profile_name;
	private LinearLayout sidebar_left_drawer;
	private LinearLayout drawer_left_profile;
	private ImageView sidebar_profile_image;
	
	private MyDrawerListCustomAdapter drAdapter;
	private DBAdapter dbAdapter;
	ActionBarDrawerToggle drawerToggle;
	
	
	
	final MyDrawerItem[] drawerItem = new MyDrawerItem[5];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate");
		dbAdapter = new DBAdapter(getApplicationContext());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
        Log.d(TAG,"start home");
        if (dbAdapter.get_setting("firstsetting") == null) {
        	Log.d(TAG,"Go to get apidata");
        	Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
            startActivity(intent);
        }
 		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity_main);           
        Log.d(TAG, "db check pass");

      
      
      

		
		
        drawer_left_profile = (LinearLayout)findViewById(R.id.drawer_left_profile);

		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		sidebar_left_listview = (ListView) findViewById(R.id.sidebar_left_listview);
		sidebar_left_drawer = (LinearLayout) findViewById(R.id.sidebar_left_drawer);
		sidebar_profile_name = (TextView)findViewById(R.id.sidebar_profile_name);
		sidebar_profile_image = (ImageView)findViewById(R.id.sidebar_profile_image);
		 
		
		drawerItem[0] = new MyDrawerItem(R.drawable.sidebar_aim, "");
		drawerItem[1] = new MyDrawerItem(R.drawable.sidebar_timer, "운동하기");
		drawerItem[2] = new MyDrawerItem(R.drawable.sidebar_graph, "기록보기");
		drawerItem[3] = new MyDrawerItem(R.drawable.sidebar_heart, "건강정보");
		drawerItem[4] = new MyDrawerItem(R.drawable.sidebar_gear, "설정");
		Log.d(TAG, "created menu drawbar's list");
		
		drAdapter = new MyDrawerListCustomAdapter(this, R.layout.drawer_activity_item_row, drawerItem);
		
		{
			FragmentManager fragmentManager = getFragmentManager();
		    fragmentManager.beginTransaction().replace(R.id.content_frame, new WorkingoutFragment()).commit();
		}
		
		sidebar_left_listview.setAdapter(drAdapter);
		sidebar_left_listview.setOnItemClickListener(new ListView.OnItemClickListener() {

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
			    	startActivityForResult(new Intent(getApplicationContext(),SettingGoalActivity.class), REQUEST_CODE_SETTING_PROFILE);
			    	break;
			    case 1:
			        fragment = new WorkingoutFragment();
			        break;
			    case 2:
			        fragment = new RecordingFragment();
			        break;
			    case 3:
			        fragment = new OtherSiteFragment();
			        break;
			    case 4:
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
			 
			        sidebar_left_listview.setItemChecked(position, true);
			        sidebar_left_listview.setSelection(position);
			        getActionBar().setTitle(drawerItem[position].name);
			        drawer_layout.closeDrawer(sidebar_left_drawer);
			        
			    } else {
			        Log.e(TAG, "Error in creating fragment");
			    }
			}
		});
		
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerToggle = new ActionBarDrawerToggle(
		        this,
		        drawer_layout,
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
		        super.onDrawerOpened(drawerView);
		    }
		};
		drawer_layout.setDrawerListener(drawerToggle);
		drawer_left_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SettingProfileActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SETTING_PROFILE);
				drawer_layout.closeDrawer(sidebar_left_drawer);
			}
		}); 
		
		loadValues();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 if (drawerToggle.onOptionsItemSelected(item)) {
		       return true;
		   } 
		   return super.onOptionsItemSelected(item);
	} 
	private void loadGoal() {
		String aims = "주 ";
		if (dbAdapter.get_setting("dayN")!=null) {aims = aims + dbAdapter.get_setting("dayN");}
		else {aims = aims + "~";}
		aims = aims + "회 하루 ";
		if (dbAdapter.get_setting("goalMinutes")!=null) {aims = aims + dbAdapter.get_setting("goalMinutes");}
		else {aims = aims + "~";}
		aims = aims + "분";  
		drawerItem[0].name = aims;
		drAdapter.notifyDataSetChanged();
	}
	
	private void loadName() {
		String name = dbAdapter.get_setting("name");
		if (name != null) { sidebar_profile_name.setText(name);	}
	}
	private void loadProfilePicture() {
		File newfile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"profilepicture.png");
        if (newfile.isFile()) {
        	sidebar_profile_image.setImageBitmap(BitmapFactory.decodeFile(newfile+""));
        }
	}
	private void loadValues() {
		loadName();
    	loadProfilePicture();
    	loadGoal();
	}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SETTING_PROFILE && resultCode == RESULT_OK) {
			Log.d(TAG, "프로필 세팅 적용");	
			loadValues();
		}
		
    }
}
