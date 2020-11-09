package com.example.project.Timeout;

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
    private long timeLeft = 600000; //default value is 10 mins
    private long timeLeftbackup = 600000;
    private boolean isRunning;
    private int[] gifSelector = {R.drawable.relaxing1, R.drawable.relaxing2, R.drawable.relaxing3, R.drawable.relaxing4, R.drawable.relaxing5};
    private final String TAG = "Timout Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Timeout Screen");
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.cancelAll();
                try {
                    pauseTimerCountdown();
                }
                catch (Exception e){}
                finish();
            }
        });

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

    public void toggleTimeout() {
        if (isRunning)
            pauseTimerCountdown();
        else
            startTimerCountdown();
    }

    public void updateProgressBar(){

        double progressComplete = 100*((float)timeLeftbackup-(float)timeLeft)/(float)timeLeftbackup;
        progress.setProgress((int)progressComplete);
        Log.d(TAG, "The progressbar percentage is: " +progressComplete+ "%");
    }

    public void sendNotification(){

        String textTitle = "Timeout Timer Running";
        String textContent = "Time left: " + (int)timeLeft/60000 +"minutes and " + (int)(timeLeft % 60000) / 1000 + "seconds." ;
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

    public void updateNotification(){
        String textContent = "Time left: " + (int)timeLeft/60000 +" minutes and " + (int)(timeLeft % 60000) / 1000 + " seconds." ;
        builder.setContentText(textContent);
        manager.notify(0, builder.build());
    }

    public void updateNotificationFinish(){
        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
        registerReceiver(finishActivityReceiver, filter);
        Intent intent = new Intent("android.intent.CLOSE_ACTIVITY");
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0 , intent, 0);
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

    public void playAlarmSound(){
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

    public void startTimerCountdown() {
        hideAllButtons();
        sendNotification();
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                setTimerText();
                updateProgressBar();
                updateNotification();
            }
            @Override
            public void onFinish() {
                timeLeft=0;
                playAlarmSound();
                CDText.setText("0:00");
                progress.setProgress(100);
                updateNotification();
            }
        }.start();

        isRunning = true;
        CDButton.setText("STOP");
    }

    public void pauseTimerCountdown() {
        showAllButtons();
        manager.cancel(0);
        timer.cancel();
        CDButton.setText("Resume");
        isRunning = false;
    }

    public void setTimerText() {
        int minsLeft = (int) timeLeft / 60000;
        int secsLeft = (int) (timeLeft % 60000) / 1000;

        String minsLeftString = String.valueOf(minsLeft);
        String secsLeftString = "";
        if (secsLeft < 10)
            secsLeftString += "0";
        secsLeftString += String.valueOf(secsLeft);
        CDText.setText(minsLeftString + ":" + secsLeftString);
    }

    private void resetTimer() {
        manager.cancel(0);
        progress.setProgress(100);
        showAllButtons();
        try {
            timer.cancel();
        }
        catch (Exception e){
            Toast.makeText(this, "Timer's not running!", Toast.LENGTH_SHORT).show();
        }
        CDButton.setText("Start");
        timeLeft = timeLeftbackup;
        setTimerText();
        isRunning = false;
    }

    private void setCustomButtonTimer() {
        try {
            String customTimerString = String.valueOf(customTimerText.getText());
            Log.d(TAG, "The timer now has " + customTimerString + " mins left. ");
            timeLeft = Integer.parseInt(customTimerString) * 60000;
        }
        catch (NumberFormatException e){
            Toast.makeText(this, "It's empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeTimerScreen(){

        progress = (ProgressBar) findViewById(R.id.timerProgress);
        calmingBGVideo = (GifImageView) findViewById(R.id.calmBackground);
        CDTimerA = findViewById(R.id.min1Button);
        CDTimerB = findViewById(R.id.min2Button);
        CDTimerC = findViewById(R.id.min3Button);
        CDTimerD = findViewById(R.id.min5Button);
        CDTimerE = findViewById(R.id.min10Button);
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
    }

    private void startCalmingVideo(){
        Random random = new Random();
        int gifNumber = random.nextInt(4)+0;
        if(gifNumber==0||gifNumber==1||gifNumber==4)
            CDText.setTextColor(Color.WHITE);
        calmingBGVideo.setBackgroundResource(gifSelector[gifNumber]);
        calmingBGVideo.setVisibility(View.VISIBLE);
    }

    private void stopCalmingVideo(){
        calmingBGVideo.setVisibility(View.GONE);
        CDText.setTextColor(Color.BLACK);
    }

    private void hideAllButtons(){
        CDTimerA.setVisibility(View.GONE);
        CDTimerB.setVisibility(View.GONE);
        CDTimerC.setVisibility(View.GONE);
        CDTimerD.setVisibility(View.GONE);
        CDTimerE.setVisibility(View.GONE);
        CDTimerCustom.setVisibility(View.GONE);
        customTimerText.setVisibility(View.GONE);
        startCalmingVideo();
    }

    private void showAllButtons(){
        CDTimerA.setVisibility(View.VISIBLE);
        CDTimerB.setVisibility(View.VISIBLE);
        CDTimerC.setVisibility(View.VISIBLE);
        CDTimerD.setVisibility(View.VISIBLE);
        CDTimerE.setVisibility(View.VISIBLE);
        CDTimerCustom.setVisibility(View.VISIBLE);
        customTimerText.setVisibility(View.VISIBLE);
        stopCalmingVideo();
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, Timeout.class);
        return intent;
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
        timeLeftbackup = timeLeft;
        setTimerText();
    }
}