package de.android.android2dz5;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class RingtonePlayingService extends Service {
    private MediaPlayer mediaPlayer;
    private int startID;
    public RingtonePlayingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String getExtraString = intent.getExtras().getString(MainActivity.EXTRA);
        assert getExtraString != null;
        switch (getExtraString) {
            case MainActivity.ALARM_ON:
                startId = 1;
                break;
            case MainActivity.ALARM_OFF:
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.violin_stirling);
        Log.d("LOG", "onStartCommand");
        mediaPlayer.start();





        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
