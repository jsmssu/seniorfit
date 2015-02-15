package name.sunme.functionactivity;

import java.util.ArrayList;

import name.sunme.seniorfit.FitApiDataClass;

public class MyWorkingoutItem {
	public String title;
    public ArrayList<FitApiDataClass> fads;
 
    // Constructor.
    public MyWorkingoutItem(String title, ArrayList<FitApiDataClass> fads) {
        this.title = title;
        this.fads = fads;
    }
}
