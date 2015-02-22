package name.sunme.firstexecution;

import com.viewpagerindicator.CirclePageIndicator;

import name.sunme.seniorfit.ImageAdapter;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log; 

public class TutorialActivity extends Activity {

	String TAG = "TutorialActivity";
	public int[] GalImages = new int[] { R.drawable.tutorial1,
			R.drawable.tutorial2, R.drawable.tutorial3, R.drawable.tutorial4};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

		
		
		ImageAdapter adapter = new ImageAdapter(this, GalImages);
		viewPager.setAdapter(adapter);

		
		
		final float density = 2;
		// Bind the title indicator to the adapter
		CirclePageIndicator circleindicator = (CirclePageIndicator) findViewById(R.id.circleindicator);
		// circleindicator.setBackgroundColor(0xFFCCCCCC);
		circleindicator.setRadius(10 * density);
		circleindicator.setPageColor(Color.parseColor("#d1d1d1"));
		circleindicator.setFillColor(Color.BLACK);
		// circleindicator.setStrokeColor(0xFF000000);
		circleindicator.setStrokeWidth(0);
		circleindicator.setViewPager(viewPager);
		circleindicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if (GalImages.length == (position + 1)
						&& positionOffsetPixels == 0
						&& tpositionOffsetPixels == positionOffsetPixels) {
					Log.d(TAG, "다음페이지로 갈까");
					Intent intent = new Intent(getApplicationContext(),
							Setup1Activity.class);

					startActivity(intent);
					finish();
				}
				tpositionOffsetPixels = positionOffsetPixels;
				Log.d(TAG, "Selected " + position + ", " + positionOffset
						+ ", " + positionOffsetPixels);
				// TODO Auto-generated method stub
			}
		});
	}
}