package com.example.project.CoinFlipActivities;

import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String INDEX_PREFS_NAME = "IndexPref" ;

    private ChildManager childManager;
    private int flipIndex;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, ChooseChildCoinFlipActivity.class);
    }

    public void launchCoinFlip(int position, boolean choice){
        Intent intent = CoinFlipActivity.makeLaunchIntent(ChooseChildCoinFlipActivity.this, position, choice);
        startActivity(intent);
        finish();
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

        flipIndex = loadFlipIndex(ChooseChildCoinFlipActivity.this);
        loadFlipIndex(ChooseChildCoinFlipActivity.this);

        // Enable "up" on toolbar
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e){
            Log.println(Log.ERROR, UP, "Up bar Error:" + e.getMessage());
        }


        Log.println(Log.INFO, CHILDMANAGER_TAG, childManager.getLength() + "");
        populateListView();
        registerListItemClickCallback();
        if (childManager.getLength()>0) {
            setupQueueKid();
        }

    }

    private void checkIfAnyChildrenInManager() {
        if(childManager.getLength() == 0){
            Log.println(Log.INFO, CHILDMANAGER_TAG, "No Children, moving onto coin flip");
            launchCoinFlip(-1, false);
        }
    }

    private void populateListView(){
        ListAdapter adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.coin_flip_child_list_view);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter(){
            super(ChooseChildCoinFlipActivity.this, R.layout.kid_list, childManager.getChildList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.kid_list, parent, false);
            }

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.child_avatar);
            imageView.setImageResource(childManager.getChildAvatarId(position));

            // Text:
            TextView itemText = (TextView) itemView.findViewById(R.id.text_childinfo);
            String item = String.format("%s", childManager.getChildName(position));
            itemText.setText(item);

            return itemView;
        }
    }

    private void registerListItemClickCallback(){
        ListView list = (ListView) findViewById(R.id.coin_flip_child_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createHeadsTailsChoiceDialog(position);
            }
        });
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

    private void createHeadsTailsChoiceDialog(final int position){

        View v = LayoutInflater.from(ChooseChildCoinFlipActivity.this).inflate(R.layout.heads_tails_message, null);
        final Dialog dialog = new Dialog(ChooseChildCoinFlipActivity.this);

        dialog.setContentView(v);
        dialog.setTitle("Choice:");

        Button headButton = (Button) dialog.findViewById(R.id.message_layout_heads_button);
        Button tailsButton = (Button) dialog.findViewById(R.id.message_layout_tails_button);

        headButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                launchCoinFlip(position, true);
            }
        });

        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                launchCoinFlip(position, false);
            }
        });

        dialog.show();
    }

    /*
        Following code figure out who's in the head of queue
    */

    private void updateFlipIndex() {
        flipIndex = (flipIndex+1) % childManager.getLength();
    }
    // Save and Load FlipIndex

    private void saveFlipIndex(Context context, int flipIndex) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(INDEX_PREFS_NAME, flipIndex);
        editor.apply();
    }

    private int loadFlipIndex(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(INDEX_PREFS_NAME, 0);
    }

    private void setupQueueKid() {
        ImageView imageView = (ImageView) findViewById(R.id.IV_queue_kid_avator);
        imageView.setImageResource(childManager.getChildAvatarId(flipIndex));

        TextView textView =  (TextView) findViewById(R.id.text_queue_kid_name);
        String name = String.format("%s", childManager.getChildName(flipIndex));
        textView.setText(name);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.kidInQueue);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update flip index
                int currentFlipIndex = flipIndex;
                createHeadsTailsChoiceDialog(flipIndex);
                updateFlipIndex();
                saveFlipIndex(ChooseChildCoinFlipActivity.this, currentFlipIndex);

            }
        });
    }
}

