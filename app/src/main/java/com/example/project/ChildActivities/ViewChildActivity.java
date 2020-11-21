/*
 * This activity give user a list view of children
 * User may choose to add or edit child from this activity
 */
package com.example.project.ChildActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewChildActivity extends AppCompatActivity {
    ChildManager childManager;
    boolean showHint;
    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String HINT_PREFS_NAME = "HintPref" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);
        childManager = ChildManager.getInstance();
        showHint = loadShowHint(ViewChildActivity.this);
        // load saved data
        List<Child> savedChildList = EditChildActivity.getSavedChildList(ViewChildActivity.this);
        if (savedChildList != null) {
            childManager.setChildList(savedChildList);
        }
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        // Enable "up" on toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kidPos -1 means add new kids
                Intent intent = EditChildActivity.makeLaunchIntent(ViewChildActivity.this, -1);
                startActivity(intent);
            }
        });

        populateListView();
        // click to show statistics
        registerClickCallback();
        showInstruction();
        setupHintHideButton();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateListView();
        showInstruction();
    }


    // if the button on the left side of hint is clicked
    // the hint will not show up again
    private void setupHintHideButton() {
        final ImageButton btn = (ImageButton) findViewById(R.id.btn_hide_hint);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint = false;
                saveShowHint(ViewChildActivity.this, showHint);
                showInstruction();
            }
        });
    }

    // Reference : https://github.com/baoyongzhang/SwipeMenuListView
    private void populateListView() {
        ArrayAdapter<Child> adapter = new MyListAdapter();
        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listview_child);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xFF)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Intent intent = EditChildActivity.makeLaunchIntent(ViewChildActivity.this, position);
                        startActivity(intent);
                        break;
                    case 1:
                        // delete
                        childManager.deleteChild(position);
                        EditChildActivity.saveChildList(ViewChildActivity.this, childManager.getChildList());
                        break;
                }
                // update listview
                populateListView();
                // update hint message
                showInstruction();
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kids_list, menu);
        return true;
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(ViewChildActivity.this, R.layout.child_list, childManager.getChildList());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_list, parent, false);
            }
            // Fill the view.
            ImageView imageView = (ImageView) itemView.findViewById(R.id.child_avatar);
            Uri avatarUri = Uri.parse(childManager.getChildAvatarUriPath(position));
            // Avatar photo may be deleted, if so we use default avatar
            try {
                imageView.setImageURI(avatarUri);
            } catch (RuntimeException e) {
                imageView.setImageURI(Child.DEFAULT_URI);
            }

            // Fill the Info Text
            TextView infoText = (TextView) itemView.findViewById(R.id.text_childinfo);
            infoText.setText(childManager.getChild(position).toString());
            return itemView;
        }
    }

    // Click kids to get Statistics
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listview_child);
        // long click to edit an existing kid
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = EditChildActivity.makeLaunchIntent(ViewChildActivity.this, position);
                startActivity(intent);
                return false;
            }
        });
    }

    private void showInstruction() {
        FloatingActionButton fab = findViewById(R.id.fab);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_blink);
        ImageView hintArrow = (ImageView) findViewById(R.id.hint_arrow);
        TextView addInstruction = (TextView) findViewById(R.id.tv_add_instruction);
        ImageButton hintButton = (ImageButton) findViewById(R.id.btn_hide_hint);

        if (childManager.getLength() == 0) {
            // no child, show add instruction
            hintButton.setVisibility(View.GONE);
            hintArrow.setVisibility(View.VISIBLE);
            addInstruction.setVisibility(View.VISIBLE);
            addInstruction.setText("Click to add child");
            fab.startAnimation(animation);
            // reset showHint for edit hint
            showHint = true;
            saveShowHint(ViewChildActivity.this, showHint);
        } else {
            // have child, show edit hint
            hintArrow.setVisibility(View.GONE);
            if (showHint) {
                hintButton.setVisibility(View.VISIBLE);
                addInstruction.setText("You may swap the list to edit child");
            } else {
                addInstruction.setVisibility(View.GONE);
                hintButton.setVisibility(View.GONE);
            }
            fab.clearAnimation();
        }
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, ViewChildActivity.class);
        return intent;
    }

    private void saveShowHint(Context context, boolean showHint) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(HINT_PREFS_NAME, showHint);
        editor.apply();
    }

    private boolean loadShowHint(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        boolean loadedHint = prefs.getBoolean(HINT_PREFS_NAME, true);
        return loadedHint;
    }
}
