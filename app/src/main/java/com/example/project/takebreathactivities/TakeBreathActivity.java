package com.example.project.takebreathactivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;

public class TakeBreathActivity extends AppCompatActivity {

    private static int THREE_SECONDS = 3000;
    private static int SEVEN_SECONDS = 7000;

    public static Intent makeLaunchIntent(MainActivity context) {
        Intent intent = new Intent(context, TakeBreathActivity.class);
        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setState(waitingInhaleState);
        setupBeginButton();
    }

    private void setupBeginButton() {
        Button breathButton = (Button) findViewById(R.id.take_breath_breath_button);

        breathButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentState.handleClickOn();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    currentState.handleClickOff();
                }
                return true;
            }
        });
    }

    private void setButtonText(String text){
        Button breathButton = (Button) findViewById(R.id.take_breath_breath_button);
        breathButton.setText(text);
    }

    private void setHelpText(String text){
        TextView helpText = (TextView) findViewById(R.id.take_breath_help_text);
        helpText.setText(text);
    }

    private final State waitingInhaleState = new WaitingInhaleState();
    private final State inhaleState = new InhaleState();
    private final State exhaleState = new ExhaleState();

    private State currentState = new IdleState();

    private void setState(State newState){
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }


    public abstract class State{
        void handleEnter() {}
        void handleExit(){}
        void handleClickOff() {}
        void handleClickOn() {}
    }

    // INHALE STATE
    private class InhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(exhaleState);

        @Override
        void handleEnter() {
            setButtonText("In");
            setHelpText("Hold button and breath in");

            Log.println(Log.INFO, "STATE", "Inhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
            Log.println(Log.INFO, "STATE", "Inhale Exit");
        }

        @Override
        void handleClickOff() {
            timerHandler.removeCallbacks(timerRunnable);
            Log.println(Log.INFO, "STATE", "Inhale Click Off");
        }

        @Override
        void handleClickOn() {
            timerHandler.postDelayed(timerRunnable, THREE_SECONDS);
            Log.println(Log.INFO, "STATE", "Inhale Click On");
        }
    }

    // EXHALE STATE
    private class ExhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(inhaleState);
        Runnable heldTooLongRunnable = () -> heldTooLong();

        @Override
        void handleEnter() {
            setButtonText("Out");
            setHelpText("Let go off button and breath out");
            timerHandler.postDelayed(heldTooLongRunnable, SEVEN_SECONDS);

            Log.println(Log.INFO, "STATE", "Exhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
            Log.println(Log.INFO, "STATE", "Exhale Exit");
        }

        @Override
        void handleClickOff() {
            timerHandler.removeCallbacks(heldTooLongRunnable);
            timerHandler.postDelayed(timerRunnable, THREE_SECONDS);
            Log.println(Log.INFO, "STATE", "Exhale Click Off");
        }

        @Override
        void handleClickOn() {
            timerHandler.removeCallbacks(timerRunnable);
            Log.println(Log.INFO, "STATE", "Exhale Click On");
        }

        void heldTooLong(){
            setHelpText("LET GO");
            Log.println(Log.INFO, "STATE", "Inhale Held too long");
        }
    }

    // WAITING INHALE STATE
    private class WaitingInhaleState extends State {
        @Override
        void handleClickOn() {
            setState(inhaleState);
        }
    }

    // NULL STATE
    private class IdleState extends State {
    }









}