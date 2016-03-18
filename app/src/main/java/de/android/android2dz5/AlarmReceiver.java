package de.android.android2dz5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LOG", "onReceive");
        String getExtraString = intent.getExtras().getString(MainActivity.EXTRA);
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra(MainActivity.EXTRA, getExtraString);
        context.startService(serviceIntent);
    }
}
