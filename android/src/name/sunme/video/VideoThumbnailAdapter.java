package name.sunme.video;
 
import org.json.JSONObject;

import name.sunme.seniorfit.FitApiDataClass;
import name.sunme.maindrawbar.R; 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout; 

public class VideoThumbnailAdapter extends PagerAdapter {
	// Declare Variables
	String TAG = "VideoThumbnailAdapter";
	Context context;
	Integer[] drawimages;  
	LayoutInflater inflater;
	FitApiDataClass fd;
	JSONObject jo;
	public VideoThumbnailAdapter(Context context, JSONObject jo, FitApiDataClass fd, Integer[] drawimages) {
		this. jo = jo;
		this.fd = fd;
		this.context = context;
		this.drawimages = drawimages;
	}
 
	@Override
	public int getCount() {
		return drawimages.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) { 
		return view == ((LinearLayout) object);
	}
  
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
 
		// Declare Variables 
		//TextView videodetail_exerciseintensity;
		ImageView videodetail_thumbnail;
		ImageView videodetail_ico_play;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.activity_videoslider, container, false);
 
		// Locate the TextViews in viewpager_item.xml
		//videodetail_exerciseintensity = (TextView) itemView.findViewById(R.id.videodetail_exerciseintensity);
		
		videodetail_ico_play = (ImageView) itemView.findViewById(R.id.videodetail_ico_play);
		if (position == 0 && fd._nameVideo!=null) {
			videodetail_ico_play.setVisibility(View.VISIBLE);
			videodetail_ico_play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, VideoShowActivity.class);
					Log.d(TAG, jo.toString());
					intent.putExtra("json", jo.toString());
					((Activity) context).startActivityForResult(intent, VideoDetailActivity.REQUEST_CODE_MOVIE);
				}
			});
		} else {
			videodetail_ico_play.setVisibility(View.GONE);
		}
		videodetail_thumbnail = (ImageView) itemView.findViewById(R.id.videodetail_thumbnail);  
		videodetail_thumbnail.setImageResource(drawimages[position]);

		((ViewPager) container).addView(itemView);
 
		return itemView;
	}
 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((LinearLayout) object);
 
	}  
}
