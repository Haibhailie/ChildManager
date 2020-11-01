package com.example.project.Timeout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.KidsActivities.KidsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;

public class Timeout extends AppCompatActivity {

    private TextView CDText;
    private Button CDButton;
    private CountDownTimer timer;
    private long timeLeft = 600000; //default value is 10 mins
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }

    public void pauseTimerCountdown() {
        timer.cancel();
        isRunning = false;
    }

    public void setTimerText() {
        int minsLeft = (int) timeLeft / 60000;
        int secsLeft = (int) (timeLeft % 60000) / 1000;

        String minsLeftString = String.valueOf(minsLeft);
        String secsLeftString = "";
        if(secsLeft<10)
            secsLeftString+="0";
        secsLeftString+=String.valueOf(secsLeft);

        CDText.setText(minsLeftString+":"+secsLeftString);

    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, Timeout.class);
        return intent;
    }

}