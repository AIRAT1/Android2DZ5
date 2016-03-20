package de.android.android2dz5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

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
        // music off and user press on
        if (!this.isRunning && startId == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.violin_stirling);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            this.isRunning = true;
            this.startId = 0;
            MainActivity.thread.start();

            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Intent mainActivityIntent = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setSmallIcon(android.R.drawable.btn_radio)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .build();
            notificationManager.notify(0, notification);
        }
        // music on and user press off
        else if (this.isRunning && startId == 0) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            this.isRunning = false;
            this.startId = 0;
            stopSelf();
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
        Log.d("LOG", "service onDestroy");
        super.onDestroy();
    }
}
