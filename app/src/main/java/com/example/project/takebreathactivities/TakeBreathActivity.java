package com.example.project.takebreathactivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.MainActivity;

import androidx.appcompat.app.ActionBar;
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

    private static final String UP_TAG = "UP";

    private static int THREE_SECONDS = 3000;
    private static int SEVEN_SECONDS = 7000;
    private static int TEN_SECONDS = 10000;

    public int breathesLeft = 3;
    private boolean buttonDown = false;

    public static Intent makeLaunchIntent(MainActivity context) {
        Intent intent = new Intent(context, TakeBreathActivity.class);
        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" on toolbar
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e){
            Log.println(Log.ERROR, UP_TAG, "Up bar Error:" + e.getMessage());
        }

        setUpBreatheChangeButtons();
        updateBreathNumberText();
        setState(startState);
        setupBeginButton();
    }

    private void setupBeginButton() {
        Button breathButton = (Button) findViewById(R.id.take_breath_breath_button);

        breathButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonDown = true;
                    currentState.handleClickOn();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    buttonDown = false;
                    currentState.handleClickOff();
                }
                return true;
            }
        });
    }

    private void setUpBreatheChangeButtons(){
        Button breatheDown = (Button) findViewById(R.id.take_breath_lower_button);
        Button breatheUp = (Button) findViewById(R.id.take_breath_raise_button);

        breatheDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeBreatheCountByIncrement(-1);
            }
        });

        breatheUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeBreatheCountByIncrement(1);
            }
        });
    }

    private void changeBreatheCountByIncrement(int inc){
        breathesLeft = breathesLeft + inc;
        if(breathesLeft < 1){
            breathesLeft = 1;
        }
        updateBreathNumberText();
    }

    private void setVisibilityOfBreatheChangeButtons(int visibility){
        Button breatheDown = (Button) findViewById(R.id.take_breath_lower_button);
        Button breatheUp = (Button) findViewById(R.id.take_breath_raise_button);

        breatheDown.setVisibility(visibility);
        breatheUp.setVisibility(visibility);
    }

    private void updateBreathNumberText(){

        TextView breathCount = (TextView) findViewById(R.id.take_breath_number_of_breathes);

        if (breathesLeft == 0){
            breathCount.setVisibility(View.GONE);
        }

        breathCount.setText(breathesLeft + "");
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
    private final State waitingExhaleState = new WaitingExhaleState();
    private final State inhaleState = new InhaleState();
    private final State exhaleState = new ExhaleState();
    private final State startState = new StartState();
    private final State idleState = new IdleState();

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

    // START STATE
    private class StartState extends State{
        void handleEnter(){
            setButtonText("Begin");
        }

        void handleClickOn(){
            setState(inhaleState);
            setVisibilityOfBreatheChangeButtons(View.GONE);
        }
    }

    private class InhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(waitingExhaleState);

        @Override
        void handleEnter() {
            setButtonText("In");
            setHelpText("Hold button and breath in");
            if(buttonDown) {
                timerHandler.postDelayed(timerRunnable, THREE_SECONDS);
            }

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

    private class ExhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable inhaleStateTimer = () -> setState(waitingInhaleState);

        @Override
        void handleEnter() {
            setHelpText(" ");
            timerHandler.postDelayed(inhaleStateTimer, THREE_SECONDS);

            Log.println(Log.INFO, "STATE", "Exhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(inhaleStateTimer);

            Log.println(Log.INFO, "STATE", "Exhale Exit");
        }

        @Override
        void handleClickOff() {
            timerHandler.postDelayed(inhaleStateTimer, THREE_SECONDS);

            Log.println(Log.INFO, "STATE", "Exhale Click Off");
        }

        @Override
        void handleClickOn() {
            timerHandler.removeCallbacks(inhaleStateTimer);

            Log.println(Log.INFO, "STATE", "Exhale Click On");
        }

    }

    private class WaitingExhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setHelpText("Release Button and breath out");

        @Override
        void handleEnter() {
            setButtonText("Out");
            setHelpText("");
            timerHandler.postDelayed(timerRunnable, SEVEN_SECONDS);

            Log.println(Log.INFO, "STATE", "Waiting Exhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);

            Log.println(Log.INFO, "STATE", "Waiting Exhale Exit");
        }

        @Override
        void handleClickOff() {
            setState(exhaleState);
            breathesLeft--;
            Log.println(Log.INFO, "STATE", "Waiting Exhale Click Off");
        }

    }

    private class WaitingInhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(inhaleState);

        @Override
        void handleEnter() {

            updateBreathNumberText();

            if(breathesLeft == 0){
                setButtonText("Good Job");
                setState(idleState);
            } else{
                setButtonText("In");
                timerHandler.postDelayed(timerRunnable, SEVEN_SECONDS);
            }
            setHelpText("");

            Log.println(Log.INFO, "STATE", "Waiting Exhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);

            Log.println(Log.INFO, "STATE", "Waiting Exhale Exit");
        }

        @Override
        void handleClickOn() {
            setState(inhaleState);

            Log.println(Log.INFO, "STATE", "Waiting Exhale Click Off");
        }
    }

    private class IdleState extends State {
    }

}