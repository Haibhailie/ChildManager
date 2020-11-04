package com.example.project.Timeout;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;

import com.example.project.KidsActivities.KidsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;

public class Timeout extends AppCompatActivity implements View.OnClickListener {

    private TextView CDText;
    private Button CDButton;
    private Button CDTimerA;
    private Button CDTimerB;
    private Button CDTimerC;
    private Button CDTimerD;
    private Button CDTimerE;
    private Button CDTimerCustom;
    private Button CDTimerReset;
    private CountDownTimer timer;
    private EditText customTimerText;
    private GifImageView calmingBGVideo;
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

    public void startTimerCountdown() {
        hideAllButtons();
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                setTimerText();
            }

            @Override
            public void onFinish() {
            }
        }.start();

        isRunning = true;
        CDButton.setText("STOP");
    }

    public void pauseTimerCountdown() {
        showAllButtons();
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
        showAllButtons();
        timer.cancel();
        CDButton.setText("Start");
        timeLeft = timeLeftbackup;
        setTimerText();
        isRunning = false;
    }

    private void setCustomButtonTimer() {
        String customTimerString = String.valueOf(customTimerText.getText());
        Log.d(TAG, "The timer now has " + customTimerString + " mins left. ");
        timeLeft = Integer.parseInt(customTimerString) * 60000;
    }

    private void initializeTimerScreen(){

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