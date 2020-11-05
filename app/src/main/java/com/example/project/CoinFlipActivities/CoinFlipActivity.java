package com.example.project.CoinFlipActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.CoinFlipModel.Coin;
import com.example.project.CoinFlipModel.CoinFlipHistoryManager;
import com.example.project.CoinFlipModel.CoinFlipHistoryMember;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

public class CoinFlipActivity extends AppCompatActivity {

    private static final String COIN = "Coin";
    private static final String EXTRA_INDEX = "CoinFlip - ChildIndex";
    private static final String EXTRA_CHOICE = "CoinFlip - ChildChoice";


    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String FLIP_PREFS_NAME = "FlipPrefs";

    private int childIndex;
    private boolean childChoiceIsHeads;

    private ChildManager childManager;
    private CoinFlipHistoryManager flipManager;

    private Coin coin = new Coin();

    public static Intent makeLaunchIntent(Context context, int index, boolean isHeads) {
        Intent intent = new Intent(context, CoinFlipActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        intent.putExtra(EXTRA_CHOICE, isHeads);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        childIndex = intent.getIntExtra(EXTRA_INDEX, -1);
        childChoiceIsHeads = intent.getBooleanExtra(EXTRA_CHOICE, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" on toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        coinAnimation();

        // Setup
        extractDataFromIntent();
        childManager = ChildManager.getInstance();
        flipManager = CoinFlipHistoryManager.getInstance();
        loadSavedHistory();
        historyButtonListener();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadSavedHistory(){
        List<CoinFlipHistoryMember> savedHistory = getHistory(CoinFlipActivity.this);
        if (savedHistory != null) {
            flipManager.setFlipList(savedHistory);
        }
    }

    private void historyButtonListener(){
        Button history = (Button) findViewById(R.id.coin_flip_history_button);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CoinFlipHistoryActivity.makeLaunchIntent(CoinFlipActivity.this, childIndex);
                startActivity(intent);
            }
        });
    }

    private void coinAnimation(){
        View coinTails = findViewById(R.id.coin_flip_tails);
        View coinHeads = findViewById(R.id.coin_flip_heads);

        Animation tailAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_coin_tails);
        Animation headAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_coin_heads);

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
        CoinFlipHistoryMember newFlip = new CoinFlipHistoryMember(childManager.getChildID(childIndex),
                winLostIcon, headsTailsIcon);
        flipManager.add(newFlip);
        saveHistory(CoinFlipActivity.this, flipManager.getFlipList());

        Log.println(Log.INFO, COIN, "Landed: "  + logInfoText);

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



}