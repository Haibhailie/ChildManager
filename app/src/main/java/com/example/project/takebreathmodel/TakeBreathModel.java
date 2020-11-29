package com.example.project.takebreathmodel;

import java.util.Date;

public class TakeBreathModel {

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

    public void TakeBreathModel(){
        breathState = State.WAITING_INHALE;
        breathCount = 3;
    }

    public void changeState(){
        switch (breathState){
            case WAITING_INHALE:
                waitingInhale();
                break;
            case INHALE:
                inhale();
                break;
            case EXHALE:
                exhale();
                break;
            case DONE:
                done();
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
