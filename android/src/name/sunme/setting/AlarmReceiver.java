package name.sunme.setting;

 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	final int ALARM_FLAG1=1;
	final int ALARM_FLAG2=2;
	final int ALARM_FLAG3=3;
	final int ALARM_FLAG4=4;
	final int ALARM_FLAG5=5;
	final int ALARM_FLAG6=6;
	final int ALARM_FLAG7=7;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
	}

}
