package com.example.project.takebreathactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.project.MainActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;

public class TakeBreathActivity extends AppCompatActivity {

    private static final String UP_TAG = "UP";
    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String BREATHE_PREFS_NAME = "TakeBreathePrefs";

    private static int THREE_SECONDS = 3000;
    private static int SEVEN_SECONDS = 7000;
    private static int TEN_SECONDS = 10000;

    public int breathesLeft;
    private boolean buttonDown = false;

    private MediaPlayer theInhaleMusic;
    private MediaPlayer theExhaleMusic;
    private Animation theInhaleAnimation;
    private Animation theExhaleAnimation;

    public static Intent makeLaunchIntent(MainActivity context) {
        Intent intent = new Intent(context, TakeBreathActivity.class);
        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        theInhaleAnimation = (Animation) AnimationUtils.loadAnimation(TakeBreathActivity.this, R.anim.anim_inhale_shake);
        theExhaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(TakeBreathActivity.this, R.anim.anim_exhale);

        breathesLeft = getBreatheCount(this);

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
                    // no more animation when all breaths are done
                    if (breathesLeft != 0) {
                        inhaleAnimation(v);
//                        v.startAnimation(theInhaleAnimation);
                    }
                    buttonDown = true;
                    currentState.handleClickOn();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    exhaleAnimation(v);
//                    v.startAnimation(theExhaleAnimation);
                    buttonDown = false;
                    currentState.handleClickOff();
                }
                return true;
            }
        });
    }

    private void inhaleShakeStart(){
        Button breatheButton = (Button) findViewById(R.id.take_breath_breath_button);
        breatheButton.startAnimation(theInhaleAnimation);
    }

    private void inhaleShakeStop(){
        Button breatheButton = (Button) findViewById(R.id.take_breath_breath_button);
        breatheButton.clearAnimation();
    }

    private void inhaleAnimation(View v){
        v.animate().scaleXBy(1.25f).setDuration(THREE_SECONDS).start();
        v.animate().scaleYBy(1.25f).setDuration(THREE_SECONDS).start();
    }

    private void exhaleAnimation(View v){
        v.animate().cancel();
        v.animate().scaleX(1f).setDuration(THREE_SECONDS).start();
        v.animate().scaleY(1f).setDuration(THREE_SECONDS).start();
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
        breathesLeft = (breathesLeft + inc);

        if(breathesLeft < 1){
            breathesLeft = 1;
        } else if(breathesLeft > 10){
            breathesLeft = 10;
        }

        updateBreathNumberText();
        saveBreatheCount(this, breathesLeft);
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

    private void setButtonColor(String color) {
        Button breathButton = (Button) findViewById(R.id.take_breath_breath_button);
        if (color.equals("cyan")) {
            breathButton.setBackground(ContextCompat.getDrawable(TakeBreathActivity.this, R.drawable.round_button));
        } else {
            breathButton.setBackground(ContextCompat.getDrawable(TakeBreathActivity.this, R.drawable.exhale_button_border));
        }
    }

    private void setHelpText(String text){
        TextView helpText = (TextView) findViewById(R.id.take_breath_help_text);
        helpText.setText(text);
    }

    private void setupInhaleMusic(){
        theInhaleMusic = MediaPlayer.create(getApplicationContext(),R.raw.inhale);
        theInhaleMusic.start();
    }

    private void setupExhaleMusic(){
        theExhaleMusic = MediaPlayer.create(getApplicationContext(),R.raw.exhale);
        theExhaleMusic.start();
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

    // Start state
    // Moves to inhaleState when button pressed
    private class StartState extends State{
        void handleEnter(){
            setButtonText(getString(R.string.begin));
        }

        void handleClickOn(){
            setState(inhaleState);
            setVisibilityOfBreatheChangeButtons(View.GONE);
        }
    }

    // Deals with inhaling.
    // After 3 seconds held the state will move to waitingExhaleState
    // If the button is let go, the state resets
    // TODO: Add animations and music
    private class InhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(waitingExhaleState);

        @Override
        void handleEnter() {
            setButtonText(getString(R.string.breath_in));
            setHelpText(getString(R.string.breath_help_breath_in));

            if(buttonDown) {
                timerHandler.postDelayed(timerRunnable, THREE_SECONDS);
                setupInhaleMusic();
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
            theInhaleMusic.stop();
            setState(inhaleState);

            Log.println(Log.INFO, "STATE", "Inhale Click Off");
        }

        @Override
        void handleClickOn() {
            timerHandler.postDelayed(timerRunnable, THREE_SECONDS);
            setupInhaleMusic();

            Log.println(Log.INFO, "STATE", "Inhale Click On");
        }

    }

    // Deals with exhaling.
    // After 3 seconds of not being held the state will move to waitingInhaleState
    // If the button is pressed, the state resets
    // TODO: Add animations and music
    private class ExhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable inhaleStateTimer = () -> setState(waitingInhaleState);

        @Override
        void handleEnter() {
            setButtonColor("green");
            timerHandler.postDelayed(inhaleStateTimer, THREE_SECONDS);
            setupExhaleMusic();
            Log.println(Log.INFO, "STATE", "Exhale Enter");
        }

        @Override
        void handleExit() {
            setButtonColor("cyan");
            timerHandler.removeCallbacks(inhaleStateTimer);
            Log.println(Log.INFO, "STATE", "Exhale Exit");
        }

        @Override
        void handleClickOff() {
            timerHandler.postDelayed(inhaleStateTimer, THREE_SECONDS);
            setupExhaleMusic();

            Log.println(Log.INFO, "STATE", "Exhale Click Off");
        }

        @Override
        void handleClickOn() {
            timerHandler.removeCallbacks(inhaleStateTimer);
            theExhaleMusic.stop();

            Log.println(Log.INFO, "STATE", "Exhale Click On");
        }

    }

    // Deals with the time where the button is held too long.
    // After 7 seconds of being held the animations and music will stop
    // Once button is released the state moves to exhaleState
    // TODO: stop animations and music
    private class WaitingExhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> stopAnimationMusic();

        @Override
        void handleEnter() {
            setButtonText(getString(R.string.breath_out));
            setHelpText(getString(R.string.breath_help_breath_out));
            inhaleShakeStart();
            timerHandler.postDelayed(timerRunnable, SEVEN_SECONDS);

            Log.println(Log.INFO, "STATE", "Waiting Exhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
            theInhaleMusic.stop();
            inhaleShakeStop();

            Log.println(Log.INFO, "STATE", "Waiting Exhale Exit");
        }

        @Override
        void handleClickOff() {
            setState(exhaleState);
            breathesLeft--;
            Log.println(Log.INFO, "STATE", "Waiting Exhale Click Off");
        }

        void stopAnimationMusic(){
            inhaleShakeStop();
            theInhaleMusic.stop();
        }

    }

    // Deals with the time where the button is not pressed for a while.
    // After 7 seconds of not being pressed the animations and music will stop
    // Once the button is pressed the state moves to inhaleState
    // TODO: stop animations and music
    private class WaitingInhaleState extends State {

        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(inhaleState);

        @Override
        void handleEnter() {
            updateBreathNumberText();
            if(breathesLeft == 0){
                setButtonText(getString(R.string.breath_good_job));
                setState(idleState);
                setHelpText("");
                theExhaleMusic.stop();
            }

            else{
                inhaleShakeStart();
                setButtonText(getString(R.string.breath_in));
                timerHandler.postDelayed(timerRunnable, SEVEN_SECONDS);
                setHelpText(getString(R.string.breath_help_breath_in));
            }

            Log.println(Log.INFO, "STATE", "Waiting Inhale Enter");
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
            theExhaleMusic.stop();
            inhaleShakeStop();

            Log.println(Log.INFO, "STATE", "Waiting Inhale Exit");
        }

        @Override
        void handleClickOn() {
            setState(inhaleState);

            Log.println(Log.INFO, "STATE", "Waiting Inhale Click Off");
        }
    }

    // Null state
    // Handles no state found errors.
    private class IdleState extends State { }

    public static void saveBreatheCount(Context context, int breathesLeft) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(BREATHE_PREFS_NAME, breathesLeft);
        editor.apply();
        Log.println(Log.INFO, BREATHE_PREFS_NAME, "Saved breathe count: " + breathesLeft);
    }

    public static int getBreatheCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        int breathesLeft = prefs.getInt(BREATHE_PREFS_NAME, 3);
        Log.println(Log.INFO, BREATHE_PREFS_NAME, "Loaded breathe count: " + breathesLeft);
        return breathesLeft;
    }

}