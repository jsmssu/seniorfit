package name.sunme.functionactivity;

import java.util.ArrayList;

import android.util.Log;
import android.widget.ImageView;
import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.FitApiDataClass;

public class MyWorkingoutItem {
	String TAG = "MyWorkingoutItem";
	public String title;
	public boolean checked;
	public int radioDraw;
	public ImageView radioImage;
	
    public ArrayList<FitApiDataClass> fads;
 
    // Constructor.
    public MyWorkingoutItem(String title, ArrayList<FitApiDataClass> fads) {
        this.title = title;
        this.fads = fads;
        checked = false;
        radioDraw = R.drawable.myworkingout_uncheckedcircle;
    }
    public void reverseCheck() {
    	checked = !checked;
    	setRadioImage();
    }
    public void setCheck() {
    	checked = true;
    	setRadioImage();
    }
    public void resetCheck() {
    	checked = false;
    	setRadioImage();
    }
    public void setRadioImage() {
    	if (checked) {
    		radioDraw = R.drawable.myworkingout_checkedcircle;
    	} else {
    		radioDraw = R.drawable.myworkingout_uncheckedcircle;
    	}
    	if (radioImage != null)radioImage.setImageResource(radioDraw);
    	else Log.d(TAG, "radioImage = " + radioImage);
    }
}
