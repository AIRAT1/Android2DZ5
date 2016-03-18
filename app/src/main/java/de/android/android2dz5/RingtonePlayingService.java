package de.android.android2dz5;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class RingtonePlayingService extends Service {
    private MediaPlayer mediaPlayer;
    private int startId;
    private boolean isRunning;
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
        // music off and user pressed on
        if (!this.isRunning && startId == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.violin_stirling);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            this.isRunning = true;
            this.startId = 0;
        }
        // music on and user press off
        else if (this.isRunning && startId == 0) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            this.isRunning = false;
            this.startId = 0;
        }
        // music off and user press off
        else if (!this.isRunning && startId == 0) {
            this.isRunning = false;
            this.startId = 0;
        }
        // music on and user press on
        else if (this.isRunning && startId == 1) {
            this.isRunning = true;
            this.startId = 1;
        }
        // just to catch the add event
        else {

        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
