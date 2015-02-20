package name.sunme.setting;

import name.sunme.maindrawbar.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Notification mNoti = new NotificationCompat.Builder(context)
				.setContentTitle("SeniorFit").setContentText("오늘의 운동을 시작하세요")
				.setSmallIcon(R.drawable.ic_launcher)//.setTicker("오늘의 운동을 시작하세요")
				.setAutoCancel(true).build();
		NotificationManager nNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nNM.notify(7777, mNoti);
	}

}
