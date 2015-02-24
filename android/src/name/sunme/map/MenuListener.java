package name.sunme.map;

import name.bagi.levente.pedometer.Pedometer;
import name.sunme.maindrawbar.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuListener extends View{
    Activity activity; 
    public Button walking_calc;
    public Button walking_rec; 
    public MenuListener(Context context) {
        super(context);
        this.activity = (Activity)context; 
        walking_calc = (Button)activity.findViewById(R.id.walking_calc);
        walking_rec = (Button)activity.findViewById(R.id.walking_rec); 
    }
    

}
