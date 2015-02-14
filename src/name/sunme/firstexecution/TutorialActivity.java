package name.sunme.firstexecution;
 
import name.sunme.seniorfit.ImageAdapter;

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class TutorialActivity extends Activity {

	String TAG = "TutorialActivity";
	public int[] GalImages = new int[] { R.drawable.tutorial1,
			R.drawable.tutorial2, R.drawable.tutorial3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		
		ImageAdapter adapter = new ImageAdapter(this, GalImages);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
				Log.d(TAG, "StateChanged : " + state); 
			}
			@Override
			public void onPageSelected(int position) { 
			  Log.d(TAG, "Selected position" + position); 
			}


			int tpositionOffsetPixels;
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (GalImages.length == (position+1) && positionOffsetPixels == 0
						&& tpositionOffsetPixels == positionOffsetPixels) {
					Log.d(TAG, "다음페이지로 갈까");
					Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
					
					
					startActivity(intent);
					finish();
				}
				tpositionOffsetPixels = positionOffsetPixels;
				Log.d(TAG, "Selected " + position+", "+positionOffset +", " + positionOffsetPixels ); 
				// TODO Auto-generated method stub
				
			} 
		});
		viewPager.setAdapter(adapter);
	}
}

/*
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ffffff"
    tools:context="tutorial" >

    <android.support.v4.view.ViewPager
	android:id="@+id/view_pager"
	android:layout_width="match_parent"
	android:layout_height="match_parent" />

</RelativeLayout>
*/