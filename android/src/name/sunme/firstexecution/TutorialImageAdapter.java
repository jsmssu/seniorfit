package name.sunme.firstexecution;

import name.sunme.maindrawbar.R;
import name.sunme.video.VideoDetailActivity;
import name.sunme.video.VideoShowActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TutorialImageAdapter extends PagerAdapter {
	public String TAG = "TutorialImageAdapter";
	public Context context;
	LayoutInflater inflater;
	
	
	
	
	public int[] images = new int[] { R.drawable.tutorial1,
			R.drawable.tutorial2, R.drawable.tutorial3, R.drawable.tutorial4 };
	public String[] titles = new String[] { "타이틀1", "타이틀2", "타이틀3", "타이틀4" };
	public String[] contents = new String[] { "contents1", "contents2",
			"contents3","contents4" };
	
	public TutorialImageAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		/*
		 * ImageView imageView = new ImageView(context); imageView.setPadding(0,
		 * 0, 0, 0); imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		 * imageView.setImageResource(images[position]); ((ViewPager)
		 * container).addView(imageView, 0); return imageView;
		 */

		// Declare Variables
		// TextView videodetail_exerciseintensity;
		
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.activity_tutorial_viewpager, container, false);
		ImageView tutorial_background = (ImageView)itemView.findViewById(R.id.tutorial_background);
		TextView tutorial_title = (TextView)itemView.findViewById(R.id.tutorial_title);
		TextView tutorial_context = (TextView)itemView.findViewById(R.id.tutorial_content); 
		 
		tutorial_background.setImageResource(images[position]);
		tutorial_title.setText(titles[position]);
		tutorial_context.setText(contents[position]); 

		((ViewPager) container).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);
	}
}