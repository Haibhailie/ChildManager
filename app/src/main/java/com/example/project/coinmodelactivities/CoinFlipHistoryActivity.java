package com.example.project.coinmodelactivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.project.childmodel.Child;
import com.example.project.childmodel.ChildManager;
import com.example.project.coinflipmodel.CoinFlipHistoryManager;
import com.example.project.coinflipmodel.CoinFlipHistoryMember;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the user to see the history of coin flips.
 * Can sort coin flips by child or all children.
 * Deletes children history that are deleted.
 */

public class CoinFlipHistoryActivity extends AppCompatActivity {
    private static final String CHILD_MANAGER_TAG = "ChildManager";
    private static final String UP_TAG = "UP";
    private static final String EXTRA_INDEX = "CoinFlipHistory - ChildIndex";
    private int childIndex;
    private ChildManager childManager;
    private CoinFlipHistoryManager flipManager;
    private List<CoinFlipHistoryMember> chosenChildHistory;

    public static Intent makeLaunchIntent(Context context, int index) {
        Intent intent = new Intent(context, CoinFlipHistoryActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        childIndex = intent.getIntExtra(EXTRA_INDEX, -1);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable "up" on toolbar
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        catch (NullPointerException e){
            Log.println(Log.ERROR, UP_TAG, "Up bar Error:" + e.getMessage());
        }
        // Setup
        extractDataFromIntent();
        childManager = ChildManager.getInstance();
        flipManager = CoinFlipHistoryManager.getInstance();
        chosenChildHistory = new ArrayList<>();
        chosenChildHistory = getHistoryOfOneChildFromIndex(childIndex);
        populateListView(flipManager.getFlipList());
        setButtonToChildName();
        addOnClickToggle();
        Log.println(Log.INFO, CHILD_MANAGER_TAG, childManager.getLength() + "");
    }

    private void addOnClickToggle(){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.coin_flip_history_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    populateListView(chosenChildHistory);
                    setTextOfOracleTextSingle();
                }

                else {
                    populateListView(flipManager.getFlipList());
                    setTextOfOracleTextAll();
                }
            }
        });
        toggle.setChecked(true);
    }

    private void setButtonToChildName(){
        String text = getString(R.string.coin_flip_history_all);
        String childName = ((childIndex != -1) ? childManager.getChildName(childIndex) : "N/A.");
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.coin_flip_history_toggle);

        toggleButton.setTextOff(String.format(text, childName));
    }

    private void setTextOfOracleTextSingle(){
        String text = getString(R.string.coin_flip_history_oracle_single);
        String childName = ((childIndex != -1) ? childManager.getChildName(childIndex) : "N/A.");
        TextView textView = (TextView) findViewById(R.id.coin_flip_history_oracle_text);
        textView.setText(String.format(text, childName));
    }

    private void setTextOfOracleTextAll(){
        String text = getString(R.string.coin_flip_history_oracle_all);
        TextView textView = (TextView) findViewById(R.id.coin_flip_history_oracle_text);
        textView.setText(text);
    }

    private List<CoinFlipHistoryMember> getHistoryOfOneChildFromIndex(int index){
        List<CoinFlipHistoryMember> childList = new ArrayList<>();
        int childID = childManager.getChildId(index);
        for(CoinFlipHistoryMember flip : flipManager.getFlipList()){
            if(flip.getChildId() == childID){
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

    private class MyListAdapter extends ArrayAdapter<CoinFlipHistoryMember> {
        List<CoinFlipHistoryMember> flipList;
        public MyListAdapter(List<CoinFlipHistoryMember> coinFlipList){
            super(CoinFlipHistoryActivity.this, R.layout.history_list, coinFlipList);
            flipList = coinFlipList;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            int childIndex;
            try {
                childIndex = childManager.findChildIndexById(flipList.get(position).getChildId());
            }

            catch (Exception e){
                childIndex = -1;
                Log.println(Log.ERROR, CHILD_MANAGER_TAG, "Failed to load: " + e.getMessage());
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
            //if no child
            if(childIndex == -1) {
                childAvatar.setImageResource(R.drawable.default_avator);
            }
            //if have child
            else{
                Uri avatarUri = Uri.parse(childManager.getChildAvatarUriPath(childIndex));
                try {
                    childAvatar.setImageURI(avatarUri);
                }

                catch (RuntimeException e) {
                    childAvatar.setImageURI(Child.DEFAULT_URI);
                }
            }
            // Text:
            TextView itemText = (TextView) itemView.findViewById(R.id.coin_flip_history_history_text);
            TextView dateView = (TextView) itemView.findViewById(R.id.coin_flip_history_date_time);
            String item = "";
            if(childIndex != -1) {
                item = String.format("%s", childManager.getChildName(childIndex));
            } else {
                item = "N/A";
            }
            dateView.setText(flipList.get(position).getDateTimeFlip());
            itemText.setText(item);
            return itemView;
        }
    }
}