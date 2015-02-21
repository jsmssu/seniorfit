package name.sunme.setting;
import java.util.Calendar;

import name.sunme.maindrawbar.MainActivity;
import name.sunme.maindrawbar.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	String TAG = "AlarmReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
			
		Toast.makeText(context, "running", 3).show();
	      
		Intent tintent = new Intent(context, MainActivity.class);
    	PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, tintent, 0);
		Notification mNoti = new NotificationCompat.Builder(context)
				.setContentTitle("SeniorFit").setContentText("오늘의 운동을 시작하세요")
				.setContentIntent(mPendingIntent)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("오늘의 운동을 시작하세요")
				.setAutoCancel(true).build();
		NotificationManager nNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nNM.notify(7777, mNoti);
	}

}
