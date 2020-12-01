package com.example.project.timeout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

/**
 * Time out:
 * For count down timer
 * Have the choice for user
 * Allow start, stop, and reset
 * Gif relaxing background shown as the timer starts
 * Alarm when the time is up
 * Continuous count down when using other app and put the phone into sleep
 */

public class Timeout extends AppCompatActivity implements View.OnClickListener {
    private TextView CDText;
    private Button CDButton, CDTimerA, CDTimerB, CDTimerC, CDTimerD, CDTimerE, CDTimerCustom, CDTimerReset;
    private CountDownTimer timer;
    private EditText customTimerText;
    private GifImageView calmingBGVideo;
    private ProgressBar progress;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private MediaPlayer alarmSound;
    private Animation fadeIn;
    private Animation fadeOut;
    private TextView speedText;
    private long timeLeft = 600000; //default value is 10 mins
    private long timeLeftbackup = 600000;
    private long hiddenTimeLeft = 600000;
    private long millisShown = timeLeft;
    private boolean isRunning;
    private int[] gifSelector = {R.drawable.relaxing1, R.drawable.relaxing2, R.drawable.relaxing3, R.drawable.relaxing4, R.drawable.relaxing5};
    private int timeInterval = 1000;
    private final String TAG = "Timout Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackground((getDrawable(R.drawable.toolbar_bg)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Timeout Screen");
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        //setup toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.cancelAll();
                try {
                    pauseTimerCountdown();
                } catch (Exception e) {
                }
                finish();
            }
        });
        //initialization
        initializeTimerScreen();
        CDText = findViewById(R.id.countdownText);
        CDButton = findViewById(R.id.startCountdown);
        CDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTimeout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_timer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isRunning) {
            switch (item.getItemId()) {
                case R.id.speed25:
                    hiddenTimeLeft = timeLeft * 4;
                    timeInterval = 1000 * 4;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed25);
                    break;
                case R.id.speed50:
                    hiddenTimeLeft = timeLeft * 2;
                    timeInterval = 1000 * 2;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed50);
                    break;
                case R.id.speed75:
                    hiddenTimeLeft = timeLeft * 3 / 4;
                    timeInterval = 1000 * 3 / 4;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed75);
                    break;
                case R.id.speed100:
                    hiddenTimeLeft = timeLeft;
                    timeInterval = 1000;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed100);
                    break;
                case R.id.speed200:
                    hiddenTimeLeft = timeLeft / 2;
                    timeInterval = 1000 / 4;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed200);
                    break;
                case R.id.speed300:
                    hiddenTimeLeft = timeLeft / 3;
                    timeInterval = 1000 / 3;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed300);
                    break;
                case R.id.speed400:
                    hiddenTimeLeft = timeLeft / 4;
                    timeInterval = 1000 / 4;
                    pauseForSpeedChange();
                    startTimerAfterSpeedChange();
                    speedText.setText(R.string.speed400);
                    break;
            }
        }
        else
            Toast.makeText(this, "Start the timer first.", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    public void pauseForSpeedChange() {
        manager.cancel(0);
        timer.cancel();
    }

    public void startTimerAfterSpeedChange() {
        speedText.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(hiddenTimeLeft, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisShown = millisShown - 1000;
                timeLeft = millisShown;
                hiddenTimeLeft = millisUntilFinished;
                setTimerText();
                updateProgressBar();
                updateNotification();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                playAlarmSound();
                CDText.setText("0:00");
                progress.setProgress(100);
                speedText.setVisibility(View.GONE);
                updateNotification();
            }
        }.start();
        isRunning = true;
        CDButton.setText("STOP");
    }

    public void startTimerCountdown() {
        speedText.setVisibility(View.VISIBLE);
        hideAllButtons();
        sendNotification();
        timer = new CountDownTimer(timeLeft, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisShown = millisShown - 1000;
                timeLeft = millisShown;
                setTimerText();
                updateProgressBar();
                updateNotification();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                playAlarmSound();
                CDText.setText("0:00");
                progress.setProgress(100);
                updateNotification();
                speedText.setVisibility(View.GONE);
            }
        }.start();
        isRunning = true;
        CDButton.setText("STOP");
    }

    public void toggleTimeout() {
        if (isRunning) {
            pauseTimerCountdown();
        } else {
            startTimerCountdown();
        }
    }

    public void updateProgressBar() {
        double progressComplete = 100 * ((float) timeLeftbackup - (float) timeLeft) / (float) timeLeftbackup;
        progress.setProgress((int) progressComplete);
        Log.d(TAG, "The progressbar percentage is: " + progressComplete + "%");
    }

    public void sendNotification() {
        String textTitle = "Timeout Timer Running";
        String textContent = "Time left: " + (int) timeLeft / 60000 + "minutes and " + (int) (timeLeft % 60000) / 1000 + "seconds.";
        Intent pauseTimer = new Intent(this, Timeout.class);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.timer_notification)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        Intent notificationIntent = new Intent(this, Timeout.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(contentIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void updateNotification() {
        String textContent = "Time left: " + (int) timeLeft / 60000 + " minutes and " + (int) (timeLeft % 60000) / 1000 + " seconds.";
        builder.setContentText(textContent);
        manager.notify(0, builder.build());
    }

    public void updateNotificationFinish() {
        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
        registerReceiver(finishActivityReceiver, filter);
        Intent intent = new Intent("android.intent.CLOSE_ACTIVITY");
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.cancelAll();
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.timer_notification)
                .setContentTitle("Timeout Complete!")
                .setContentText("Feel better!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.alarm_ringing))
                .setOngoing(true)
                .addAction(R.drawable.timer_pause, "STOP", pIntent);
        manager.notify(0, builder.build());
    }

    public void playAlarmSound() {
        updateNotificationFinish();
        alarmSound = MediaPlayer.create(this, R.raw.timer_alarm);
        alarmSound.start();
        final Animation blinkText = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
        CDText.startAnimation(blinkText);
        CDTimerReset.setVisibility(View.GONE);
        CDButton.setVisibility(View.GONE);
        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setVisibility(View.VISIBLE);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSound.stop();
                finish();
            }
        });
    }

    public void pauseTimerCountdown() {
        showAllButtons();
        manager.cancel(0);
        timer.cancel();
        CDButton.setText("Resume");
        isRunning = false;
        speedText.setVisibility(View.GONE);
    }

    public void setTimerText() {
        int minsLeft = (int) timeLeft / 60000;
        int secsLeft = (int) (timeLeft % 60000) / 1000;

        String minsLeftString = String.valueOf(minsLeft);
        String secsLeftString = "";
        if (secsLeft < 10) {
            secsLeftString += "0";
        }
        secsLeftString += String.valueOf(secsLeft);
        CDText.setText(minsLeftString + ":" + secsLeftString);
    }

    private void resetTimer() {
        manager.cancel(0);
        progress.setProgress(100);
        showAllButtons();
        try {
            timer.cancel();
        } catch (Exception e) {
            Toast.makeText(this, "Timer's not running!", Toast.LENGTH_SHORT).show();
        }
        hiddenTimeLeft = timeLeft;
        timeInterval = 1000;
        speedText.setText("Present Speed: 100%");
        CDButton.setText("Start");
        timeLeft = timeLeftbackup;
        setTimerText();
        isRunning = false;
        speedText.setVisibility(View.GONE);
    }

    private void setCustomButtonTimer() {
        try {
            String customTimerString = String.valueOf(customTimerText.getText());
            Log.d(TAG, "The timer now has " + customTimerString + " mins left. ");
            timeLeft = Integer.parseInt(customTimerString) * 60000;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "It's empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeTimerScreen() {
        progress = (ProgressBar) findViewById(R.id.timerProgress);
        calmingBGVideo = (GifImageView) findViewById(R.id.calmBackground);
        CDTimerA = findViewById(R.id.min1Button);
        CDTimerB = findViewById(R.id.min2Button);
        CDTimerC = findViewById(R.id.min3Button);
        CDTimerD = findViewById(R.id.min5Button);
        CDTimerE = findViewById(R.id.min10Button);
        speedText = findViewById(R.id.presentSpeedText);
        CDTimerCustom = findViewById(R.id.customButton);
        CDTimerReset = findViewById(R.id.resetCountdown);
        customTimerText = findViewById(R.id.customTimerText);
        CDTimerA.setOnClickListener(this);
        CDTimerB.setOnClickListener(this);
        CDTimerC.setOnClickListener(this);
        CDTimerD.setOnClickListener(this);
        CDTimerE.setOnClickListener(this);
        CDTimerCustom.setOnClickListener(this);
        CDTimerReset.setOnClickListener(this);
        speedText.setText(R.string.initial_present_speed);
        speedText.setVisibility(View.GONE);
    }

    private void startCalmingVideo() {
        Random random = new Random();
        int gifNumber = random.nextInt(4) + 0;
        calmingBGVideo.setBackgroundResource(gifSelector[gifNumber]);
        calmingBGVideo.setVisibility(View.VISIBLE);
        calmingBGVideo.startAnimation(fadeIn);
    }

    private void stopCalmingVideo() {
        calmingBGVideo.setVisibility(View.GONE);
        CDText.setTextColor(Color.BLACK);
        if (isRunning) {
            calmingBGVideo.startAnimation(fadeOut);
        }
    }

    private void hideAllButtons() {
        CDTimerA.setVisibility(View.GONE);
        CDTimerB.setVisibility(View.GONE);
        CDTimerC.setVisibility(View.GONE);
        CDTimerD.setVisibility(View.GONE);
        CDTimerE.setVisibility(View.GONE);
        CDTimerCustom.setVisibility(View.GONE);
        customTimerText.setVisibility(View.GONE);
        startCalmingVideo();
    }

    private void showAllButtons() {
        CDTimerA.setVisibility(View.VISIBLE);
        CDTimerB.setVisibility(View.VISIBLE);
        CDTimerC.setVisibility(View.VISIBLE);
        CDTimerD.setVisibility(View.VISIBLE);
        CDTimerE.setVisibility(View.VISIBLE);
        CDTimerCustom.setVisibility(View.VISIBLE);
        customTimerText.setVisibility(View.VISIBLE);
        stopCalmingVideo();
    }

    BroadcastReceiver finishActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            manager.cancelAll();
            alarmSound.stop();
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        CDButton.setText("Start");
        switch (v.getId()) {
            case R.id.min1Button:
                timeLeft = 60000;
                break;
            case R.id.min2Button:
                timeLeft = 120000;
                break;
            case R.id.min3Button:
                timeLeft = 180000;
                break;
            case R.id.min5Button:
                timeLeft = 300000;
                break;
            case R.id.min10Button:
                timeLeft = 600000;
                break;
            case R.id.customButton:
                setCustomButtonTimer();
                break;
            case R.id.resetCountdown:
                resetTimer();
        }
        millisShown = timeLeft;
        timeLeftbackup = timeLeft;
        setTimerText();
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, Timeout.class);
        return intent;
    }
}