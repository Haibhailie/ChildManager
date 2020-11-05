package com.example.project.CoinFlipActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.CoinFlipModel.CoinFlipHistoryManager;
import com.example.project.CoinFlipModel.CoinFlipHistoryMember;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.project.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CoinFlipHistoryActivity extends AppCompatActivity {

    private final String CHILDMANAGER_TAG = "ChildManager";
    private static final String COIN = "Coin";
    private static final String EXTRA_INDEX = "CoinFlipHistory - ChildIndex";

    private int childIndex;
    private ChildManager childManager;
    private CoinFlipHistoryManager flipManager;

    private List<CoinFlipHistoryMember> chosenChildHistory = new ArrayList<>();

    public static Intent makeLaunchIntent(Context context, int index) {
        Intent intent = new Intent(context, CoinFlipHistoryActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        childIndex = intent.getIntExtra(EXTRA_INDEX, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extractDataFromIntent();
        childManager = ChildManager.getInstance();
        flipManager = CoinFlipHistoryManager.getInstance();
        chosenChildHistory = getHistoryOfOneChildFromIndex(childIndex);
        addOnClickToggle();
        setButtonToChildName();

        // Enable "up" on toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.println(Log.INFO, CHILDMANAGER_TAG, childManager.getLength() + "");
        populateListView(flipManager.getFlipList());
    }

    private void addOnClickToggle(){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.coin_flip_history_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    populateListView(chosenChildHistory);
                } else {
                    populateListView(flipManager.getFlipList());
                }
            }
        });
    }

    private void setButtonToChildName(){
        String text = getString(R.string.coin_flip_history_single);
        String childName = ((childIndex != -1) ?  childManager.getChildName(childIndex) : "Child Not Found.");
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.coin_flip_history_toggle);

        toggleButton.setTextOff(String.format(text, childName));
        toggleButton.setText(String.format(text, childName));

    }

    private List<CoinFlipHistoryMember> getHistoryOfOneChildFromIndex(int index){

        List<CoinFlipHistoryMember> childList = new ArrayList<>();
        int childID = childManager.getChildID(index);

        for(CoinFlipHistoryMember flip : flipManager.getFlipList()){

            if(flip.getChildID() == childID){
                childList.add(flip);
            }
        }

        return childList;
    }


    private void populateListView(List<CoinFlipHistoryMember> coinFlipList){

        ListAdapter adapter = new CoinFlipHistoryActivity.MyListAdapter(coinFlipList);

        ListView list = (ListView) findViewById(R.id.coin_flip_history_list);
        list.setAdapter(adapter);

    }

    // Adds an adapter to the list view. Allows for on images to be added.
    private class MyListAdapter extends ArrayAdapter<CoinFlipHistoryMember> {

        List<CoinFlipHistoryMember> flipList;

        public MyListAdapter(List<CoinFlipHistoryMember> coinFlipList){
            super(CoinFlipHistoryActivity.this, R.layout.history_list, coinFlipList);
            flipList = coinFlipList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int childIndex = -1;

            try {
                childIndex = childManager.findChildIndexById(flipList.get(position).getChildID());
            } catch (Exception e){
                Log.println(Log.ERROR, CHILDMANAGER_TAG, "Failed to load: " + e.getMessage());
            }

            // make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.history_list, parent, false);
            }

            // Fill the view
            ImageView childAvatar = (ImageView) itemView.findViewById(R.id.coin_flip_history_child_avatar);
            ImageView winLoseIcon = (ImageView) itemView.findViewById(R.id.coin_flip_history_win_lose_image);
            ImageView headsTailsIcon = (ImageView) itemView.findViewById(R.id.coin_flip_history_coin_chosen);

            winLoseIcon.setImageResource(flipList.get(position).getWinLoseIcon());
            headsTailsIcon.setImageResource(flipList.get(position).getHeadsTailsIcon());

            if(childIndex == -1) {
                childAvatar.setImageResource(R.drawable.default_avator);
            }else{
                childAvatar.setImageResource(childManager.getChildAvatarId(childIndex));
            }

            // Text:
            TextView itemText = (TextView) itemView.findViewById(R.id.coin_flip_history_history_text);

            String item = "ERROR - Maybe they were removed.";
            if(childIndex != -1) {
                item = String.format("%s", childManager.getChildName(childIndex));
            }
            itemText.setText(item);

            return itemView;
        }
    }
}