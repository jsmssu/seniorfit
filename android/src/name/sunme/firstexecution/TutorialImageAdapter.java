package name.sunme.firstexecution;

import name.sunme.maindrawbar.R;
import name.sunme.video.VideoDetailActivity;
import name.sunme.video.VideoShowActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TutorialImageAdapter extends PagerAdapter {
	public String TAG = "TutorialImageAdapter";
	public Context context;
	LayoutInflater inflater;
	
	
	
	
	public int[] images = new int[] { R.drawable.tutorial1,
			R.drawable.tutorial2, R.drawable.tutorial3, R.drawable.tutorial4 };
	public String[] titles = new String[] { "�Ϸ� �", "�پ��� ���α׷�", "���� ��𼭵�", "���������� ����" };
	public String[] contents = new String[] { 
			"�ôϾ�������\n�ʼ� ����� ä��������", 
			"� ��, ��ü ������ ����\n�پ��� ���α׷��� �����մϴ�",
			"��Ʈ��Ī �� �ƴ϶� �ȱ� ��带\n�����մϴ�. ���� ��𼭵�\n�ôϾ��Ͱ� �Բ� �ϼ���!",
			"ȥ�ڼ��� �ս��� ��� �� �ִ�" };
	
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
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.activity_tutorial_viewpager, container, false);
		ImageView tutorial_background = (ImageView)itemView.findViewById(R.id.tutorial_background);
		TextView tutorial_title = (TextView)itemView.findViewById(R.id.tutorial_title);
		TextView tutorial_content = (TextView)itemView.findViewById(R.id.tutorial_content); 
		 
		tutorial_background.setImageResource(images[position]);
		tutorial_title.setText(titles[position]);
		tutorial_content.setText(contents[position]); 
		if (position == images.length-1) {
			Button tutorial_next = (Button)itemView.findViewById(R.id.tutorial_next);
			tutorial_next.setVisibility(View.VISIBLE);
			tutorial_title.setTextColor(Color.WHITE);
			tutorial_content.setTextColor(Color.WHITE);
			tutorial_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,
							FacebookLoginActivity.class); 
					context.startActivity(intent);
					((Activity) context).finish();
				}
			});
		}
		((ViewPager) container).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);
	}
}