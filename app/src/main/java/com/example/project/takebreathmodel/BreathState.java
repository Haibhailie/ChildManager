package com.example.project.takebreathmodel;

import android.util.Log;

import java.util.Date;

public class BreathState {

    private static String BREATH_TAG = "BREATH_STATE";

    private State breathState;

    private long startTime;
    private int breathCount;

    private enum State {
        WAITING_INHALE,
        INHALE,
        EXHALE,
        DONE
    }

    private static long THREE_SECONDS = 3000;

    public BreathState(){
        breathState = State.WAITING_INHALE;
        breathCount = 3;
    }

    public void changeState(){
        switch (breathState){
            case WAITING_INHALE:
                waitingInhale();
                Log.println(Log.INFO, BREATH_TAG, "WAITING_INHALE");
                break;
            case INHALE:
                inhale();
                Log.println(Log.INFO, BREATH_TAG, "INHALE");
                break;
            case EXHALE:
                exhale();
                Log.println(Log.INFO, BREATH_TAG, "EXHALE");
                break;
            case DONE:
                done();
                Log.println(Log.INFO, BREATH_TAG, "DONE");
                break;
        }
    }

    public State getState(){
        return breathState;
    }

    private long getElapsedTime(){
        return (new Date()).getTime() - startTime;
    }

    private void resetStartTime(){
        startTime = System.currentTimeMillis();
    }

    private void waitingInhale(){
        breathState = State.INHALE;
        resetStartTime();
    }

    private void inhale(){
        long elapsedTime = getElapsedTime();
        resetStartTime();

        if(elapsedTime >= THREE_SECONDS) {
            breathState = State.EXHALE;
        }else{
            breathState = State.WAITING_INHALE;
        }
    }

    private void exhale(){
        long elapsedTime = getElapsedTime();
        resetStartTime();

        if(elapsedTime >= THREE_SECONDS) {
            if(breathCount == 0){
                done();
                breathState = State.DONE;
            } else{
                breathState = State.WAITING_INHALE;
            }
        }
    }

    private void done(){

    }

}
