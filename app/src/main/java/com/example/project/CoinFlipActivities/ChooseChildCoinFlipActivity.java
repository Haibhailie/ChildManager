package com.example.project.CoinFlipActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.KidsActivities.EditKidsActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.project.R;

import java.util.List;

/**
 * Allows the user to choose which child that will get the coinflip.
 * Prompts child with choice.
 *
 * Passes the index of the child and the choice of heads or tails.
 */
public class ChooseChildCoinFlipActivity extends AppCompatActivity {

    private final String UP = "UP";
    private final String CHILDMANAGER_TAG = "ChildManager";
    private final String FLIP_INDEX_TAG ="Flip Index";

    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String INDEX_PREFS_NAME = "IndexPref" ;

    private ChildManager childManager;
    private int flipIndex;
    private int childIndex;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, ChooseChildCoinFlipActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Loads Children History
        childManager = ChildManager.getInstance();
        loadChildData();
        checkIfAnyChildrenInManager();
        childIndex = flipIndex = loadFlipIndex(ChooseChildCoinFlipActivity.this);

        // Enable "up" on toolbar
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e){
            Log.println(Log.ERROR, UP, "Up bar Error:" + e.getMessage());
        }
        if(childManager.getLength() > 0) {
            populateFields();
        }
        Log.println(Log.INFO, CHILDMANAGER_TAG, childManager.getLength() + "");
    }

    private void populateFields() {
        setOracleText();
        setChildNameTag();
        setAvatar();
        setOnClickHeadsTails();
    }

    private void setOracleText(){
        TextView oracleText = (TextView) findViewById(R.id.coin_flip_choose_oracle);
        String resourceString = getString(R.string.coin_flip_choose_oracle);

        oracleText.setText(String.format(resourceString, childManager.getChildName(childIndex)));
    }

    private void setChildNameTag(){
        TextView name = (TextView) findViewById(R.id.coin_flip_choose_child_name);
        name.setText(childManager.getChildName(childIndex));
    }

    private void setAvatar(){
        ImageView childAvatar = (ImageView) findViewById(R.id.coin_flip_choose_avatar);
        childAvatar.setImageResource(childManager.getChildAvatarId(childIndex));
    }

    private void setOnClickHeadsTails(){
        ImageButton heads = (ImageButton) findViewById(R.id.coin_flip_choose_heads_image);
        ImageButton tails = (ImageButton) findViewById(R.id.coin_flip_choose_tails_image);

        heads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeadsTailsClick(true);
            }
        });

        tails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeadsTailsClick(false);
            }
        });
    }

    private void onHeadsTailsClick(Boolean choice){
        updateFlipIndex();
        saveFlipIndex(ChooseChildCoinFlipActivity.this, flipIndex);
        launchCoinFlipActivity(choice, childIndex);
    }

    private void launchCoinFlipActivity(boolean choice, int index){
        Intent intent = CoinFlipActivity.makeLaunchIntent(ChooseChildCoinFlipActivity.this, index, choice);
        startActivity(intent);
        finish();
    }

    private void checkIfAnyChildrenInManager() {
        Log.println(Log.INFO, CHILDMANAGER_TAG, "Number of Children: " + childManager.getLength());
        if(childManager.getLength() == 0){
            Log.println(Log.INFO, CHILDMANAGER_TAG, "No Children, moving onto coin flip");
            launchCoinFlipActivity(false, -1);
        } else{
            populateFields();
        }
    }

    private void loadChildData(){
        if(childManager.getLength() == 0){
            List<Child> savedChildList = EditKidsActivity.getKidsRecord(ChooseChildCoinFlipActivity.this);
            if (savedChildList != null) {
                childManager.setChildList(savedChildList);
                Log.println(Log.INFO, CHILDMANAGER_TAG, "Loaded Child List from EditKidsActivity");
            }
        }
    }

    private void updateFlipIndex() {
        flipIndex = (flipIndex+1) % childManager.getLength();
        Log.println(Log.INFO, FLIP_INDEX_TAG, "Flip Index updated to: " + flipIndex);
    }

    private void saveFlipIndex(Context context, int flipIndex) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(INDEX_PREFS_NAME, flipIndex);
        editor.apply();
    }

    private int loadFlipIndex(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        flipIndex = prefs.getInt(INDEX_PREFS_NAME, 0);
        if(flipIndex >= childManager.getLength() || flipIndex < 0){
            flipIndex = 0;
            saveFlipIndex(context, flipIndex);
        }

        Log.println(Log.INFO, FLIP_INDEX_TAG, "Flip index loaded: " + flipIndex);

        return flipIndex;
    }

}

