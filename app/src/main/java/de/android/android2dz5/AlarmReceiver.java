package de.android.android2dz5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String getExtraString = intent.getExtras().getString(MainActivity.EXTRA);
        int getYourWhaleChoice = intent.getExtras().getInt(MainActivity.WHALE_CHOICE);
        Log.d("LOG", "int song value " + getYourWhaleChoice);
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra(MainActivity.EXTRA, getExtraString);
        serviceIntent.putExtra(MainActivity.WHALE_CHOICE, getYourWhaleChoice);
        context.startService(serviceIntent);

    }

}


