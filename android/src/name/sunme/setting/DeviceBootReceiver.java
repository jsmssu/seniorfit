package name.sunme.setting;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmController acrr = new AlarmController(context);
            acrr.setSimpleAlarm();
        }
    }
}