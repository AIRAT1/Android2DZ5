package de.android.android2dz5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    public static final String EXTRA = "extra";
    public static final String ALARM_ON = "alarm on";
    public static final String ALARM_OFF = "alarm off";
    public static final String WHALE_CHOICE = "whale_choice";
    public static final int ONE_MINUTE = 60000; // milliseconds in 1 minute
    private AlarmManager alarmManager;
    private TimePicker alarmTimePicker;
    private Button alarmOn, alarmOff;
    private TextView updateText, songText;
    private Context context;
    private PendingIntent pendingIntent;
    private Intent alarmIntent;
    private final Calendar calendar = Calendar.getInstance();
    private String hourString = "";
    private String minuteString = "";
    private String songString = "";
    static Thread thread;
    private int choose_whale_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add day/night auto switch
        if (savedInstanceState == null) {
            getDelegate().setLocalNightMode(
                    AppCompatDelegate.MODE_NIGHT_AUTO
            );
            recreate();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmTimePicker = (TimePicker)findViewById(R.id.timePicker);
//        alarmTimePicker.setIs24HourView(true);
        updateText = (TextView)findViewById(R.id.updateText);
        songText = (TextView)findViewById(R.id.songText);
        alarmIntent = new Intent(this.context, AlarmReceiver.class);

        // create spinner
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.whale_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // set on onClickListener to the onItemSelected method
        spinner.setOnItemSelectedListener(this);

        alarmOn = (Button)findViewById(R.id.alarmOn);
        alarmOff = (Button)findViewById(R.id.alarmOff);
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
                alarmOn.setEnabled(false);
                int hour = alarmTimePicker.getCurrentHour();
                int minute = alarmTimePicker.getCurrentMinute();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                // to fix alarm time for tomorrow
                long _alarm = 0;
                if (calendar.getTimeInMillis() < System.currentTimeMillis() - ONE_MINUTE) {
                    _alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
                }else {
                    _alarm = calendar.getTimeInMillis();
                }

                hourString = String.valueOf(hour);
                minuteString = String.valueOf(minute);
                if (hour > 12) hourString = String.valueOf(hour - 12);
                if (minute < 10) minuteString = "0" + String.valueOf(minute);
                setAlarmText("Alarm set to: " + hourString + ":" + minuteString);
                songText.setText("Current melodie is: " + songString);
                alarmIntent.putExtra(EXTRA, ALARM_ON);
                alarmIntent.putExtra(WHALE_CHOICE, choose_whale_sound);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, _alarm,
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
                alarmOn.setEnabled(true);
                stopMusic();
                setAlarmText("Alarm off!");
                songText.setText("Current melodie is: ");
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
        alarmIntent.putExtra(WHALE_CHOICE, choose_whale_sound);
        sendBroadcast(alarmIntent);
    }

    private void setAlarmText(String s) {
        updateText.setText(s);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choose_whale_sound = (int) id;
        String[] choose = getResources().getStringArray(R.array.whale_array);
        songString = choose[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
