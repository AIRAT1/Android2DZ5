package de.android.android2dz5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA = "extra";
    public static final String ALARM_ON = "alarm on";
    public static final String ALARM_OFF = "alarm off";
    private AlarmManager alarmManager;
    private TimePicker alarmTimePicker;
    private TextView updateText;
    private Context context;
    private PendingIntent pendingIntent;
    private Intent alarmIntent;
    private final Calendar calendar = Calendar.getInstance();
    private String hourString = "";
    private String minuteString = "";
    static Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmTimePicker = (TimePicker)findViewById(R.id.timePicker);
        alarmTimePicker.setIs24HourView(true);
        updateText = (TextView)findViewById(R.id.updateText);
        alarmIntent = new Intent(this.context, AlarmReceiver.class);
        Button alarmOn = (Button)findViewById(R.id.alarmOn);
        Button alarmOff = (Button)findViewById(R.id.alarmOff);
        alarmOn.setOnClickListener(this);
        alarmOff.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarmOn:
                int hour = alarmTimePicker.getCurrentHour();
                int minute = alarmTimePicker.getCurrentMinute();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                hourString = String.valueOf(hour);
                minuteString = String.valueOf(minute);
                if (hour > 12) hourString = String.valueOf(hour - 12);
                if (minute < 10) minuteString = "0" + String.valueOf(minute);
                setAlarmText("Alarm set to: " + hourString + ":" + minuteString);
                alarmIntent.putExtra(EXTRA, ALARM_ON);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);
                updateNewAppWidget(this);

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.SECONDS.sleep(60);
                            stopMusic();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case R.id.alarmOff:
                stopMusic();
                setAlarmText("Alarm off!");
                break;
            default:
                break;
        }
    }

    private void updateNewAppWidget(Context context) {
        Intent newAppWidgetIntent = new Intent(context, NewAppWidget.class);
        newAppWidgetIntent.putExtra("hour", hourString);
        newAppWidgetIntent.putExtra("minute", minuteString);
        context.sendBroadcast(newAppWidgetIntent);
    }

    private void stopMusic() {
        alarmManager.cancel(pendingIntent);
        alarmIntent.putExtra(EXTRA, ALARM_OFF);
        sendBroadcast(alarmIntent);
    }

    private void setAlarmText(String s) {
        updateText.setText(s);
    }
}
