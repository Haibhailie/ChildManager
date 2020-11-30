package com.example.project.takebreathactivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.MainActivity;
import com.example.project.takebreathmodel.BreathState;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.project.R;

public class TakeBreathActivity extends AppCompatActivity {

    private BreathState breath;

    public static Intent makeLaunchIntent(MainActivity context) {
        Intent intent = new Intent(context, TakeBreathActivity.class);
        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        breath = new BreathState();
        setupBeginButton();
    }

    private void setupBeginButton() {
        Button breathButton = (Button) findViewById(R.id.take_breath_breath_button);
        breathButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonDown();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    buttonUp();
                }
                return true;
            }
        });
    }

    private void buttonDown(){
        breath.changeState();
        Log.println(Log.INFO, "BREATH_BUTTON", "DOWN");
    }

    private void buttonUp(){
        breath.changeState();
        Log.println(Log.INFO, "BREATH_BUTTON", "UP");
    }
}