package com.example.project.CoinFlipActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.Bundle;

import com.example.project.ChildModel.ChildManager;
import com.example.project.CoinFlipModel.Coin;
import com.example.project.CoinFlipModel.CoinFlipHistoryManager;
import com.example.project.CoinFlipModel.CoinFlipHistoryMember;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Flips a two headed coin with equal weight.
 * Returns to MainActivity
 *
 * Passes index of the child
 */
public class CoinFlipActivity extends AppCompatActivity {

    private static final String COIN = "Coin";
    private static final String UP = "UP";
    private static final String CHILDMANAGER_TAG = "ChildManager";

    private static final String EXTRA_INDEX = "CoinFlip - ChildIndex";
    private static final String EXTRA_CHOICE = "CoinFlip - ChildChoice";

    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String FLIP_PREFS_NAME = "FlipPrefs";

    private int indexOfChild;
    private boolean childChoiceIsHeads;

    private ChildManager childManager;
    private CoinFlipHistoryManager flipManager;
    private Coin coin;

    public static Intent makeLaunchIntent(Context context, int index, boolean isHeads) {
        Intent intent = new Intent(context, CoinFlipActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        intent.putExtra(EXTRA_CHOICE, isHeads);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        indexOfChild = intent.getIntExtra(EXTRA_INDEX, -1);
        childChoiceIsHeads = intent.getBooleanExtra(EXTRA_CHOICE, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" on toolbar
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e){
            Log.println(Log.ERROR, UP, "Up bar Error:" + e.getMessage());
        }

        // Setup
        extractDataFromIntent();
        setHistoryButtonVisibility(View.GONE);

        childManager = ChildManager.getInstance();
        flipManager = CoinFlipHistoryManager.getInstance();
        coin = new Coin();

        historyButtonListener();
        loadSavedHistory();
        coinAnimation();

    }

    private void loadSavedHistory(){
        List<CoinFlipHistoryMember> savedHistory = getHistory(CoinFlipActivity.this);
        if (savedHistory != null) {
            flipManager.setFlipList(removeMissingChildren(savedHistory));
        }
    }

    private void historyButtonListener(){
        Button history = (Button) findViewById(R.id.coin_flip_history_button);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCoinFlipHistory();
                finish();
            }
        });
    }

    private void launchCoinFlipHistory(){
        Intent intent = CoinFlipHistoryActivity.makeLaunchIntent(CoinFlipActivity.this, indexOfChild);
        startActivity(intent);
    }


    private void coinAnimation(){
        View coinTails = findViewById(R.id.coin_flip_tails);
        View coinHeads = findViewById(R.id.coin_flip_heads);

        Animation tailAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_coin_tails);
        Animation headAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_coin_heads);

        // https://freesound.org/people/SpaceJoe/sounds/485724/
        playSound(R.raw.coinflip);

        tailAnimation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
                coinFlipTimerSetWinner();
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
            }
        });

        coinHeads.startAnimation(headAnimation);
        coinTails.startAnimation(tailAnimation);
        Log.println(Log.INFO, COIN, "Starting animation: Coin");
    }

    private void playSound(int soundResource){
        final MediaPlayer mp = MediaPlayer.create(this, soundResource);
        mp.start();
    }

    private void setCoinToWinningCoin(){
        ImageView coinImage;
        String logInfoText;

        boolean heads = coin.flipCoin();
        int headsTailsIcon;
        int winLostIcon;

        if(heads){
            coinImage = (ImageView) findViewById(R.id.coin_flip_tails);
            setWinningText(R.string.coin_flip_heads);
            headsTailsIcon = R.drawable.c_coin_heads;
            logInfoText = "Heads";
        }
        else {
            coinImage = (ImageView) findViewById(R.id.coin_flip_heads);
            setWinningText(R.string.coin_flip_tails);
            headsTailsIcon = R.drawable.c_coin_tails;
            logInfoText = "Tails";
        }

        if(heads == childChoiceIsHeads) {
            winLostIcon = R.drawable.y_mark;
        } else{
            winLostIcon = R.drawable.x_mark;
        }

        coinImage.setAlpha(0f);

        // Save History if a child chose
        if(indexOfChild != -1) {
            CoinFlipHistoryMember newFlip = new CoinFlipHistoryMember(childManager.getChildID(indexOfChild),
                    winLostIcon, headsTailsIcon);
            flipManager.add(newFlip);
            saveHistory(CoinFlipActivity.this, flipManager.getFlipList());
        }
        setHistoryButtonVisibility(View.VISIBLE);

        Log.println(Log.INFO, COIN, "Landed: "  + logInfoText);

    }

    private void setHistoryButtonVisibility(int visibility) {
        if(indexOfChild != -1 || visibility == View.GONE) {
            Button historyButton = (Button) findViewById(R.id.coin_flip_history_button);
            historyButton.setVisibility(visibility);
        }
    }

    private void coinFlipTimerSetWinner(){

        int delay = (int)(getResources().getInteger(R.integer.coin_flip_quarter_time)*3.5);
        CountDownTimer timer = new CountDownTimer(delay, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                setCoinToWinningCoin();
                playSound(R.raw.coinland);
            }
        };

        timer.start();
    }

    private void setWinningText(int textId){
        TextView winningText = (TextView) findViewById(R.id.coin_flip_win_text);
        winningText.setText(textId);
    }

    // Reference: https://www.youtube.com/watch?v=jcliHGR3CHo&ab_channel=CodinginFlow
    public static void saveHistory(Context context, List<CoinFlipHistoryMember> flipList) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(flipList);
        editor.putString(FLIP_PREFS_NAME, json);
        editor.apply();

        Log.println(Log.INFO, FLIP_PREFS_NAME, "Saved History. Total Saved: " + ((flipList != null) ? flipList.size() : ""));
    }

    public static List<CoinFlipHistoryMember> getHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(FLIP_PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<CoinFlipHistoryMember>>() {}.getType();
        List<CoinFlipHistoryMember> flipList = gson.fromJson(json, type);

        Log.println(Log.INFO, FLIP_PREFS_NAME, "Loaded History. Total Loaded: " + ((flipList != null) ? flipList.size() : ""));

        return flipList;
    }

    private List<CoinFlipHistoryMember> removeMissingChildren(List<CoinFlipHistoryMember> flipList){
        List<CoinFlipHistoryMember> cleansedFlipList = new ArrayList<>();

        for(CoinFlipHistoryMember flip : flipList) {

            if (childManager.findChildIndexById(flip.getChildId()) != -1) {
                cleansedFlipList.add(flip);
            }
        }


        return cleansedFlipList;
    }



}