package de.android.android2dz5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LOG", "onReceive");
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(serviceIntent);
    }
}
