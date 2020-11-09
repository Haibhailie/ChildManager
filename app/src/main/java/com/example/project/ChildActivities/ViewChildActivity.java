/*
    * This activity give user a list view of children
    * User may choose to add or edit child from this activity
 */
package com.example.project.ChildActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project.R;

import java.util.List;

public class ViewChildActivity extends AppCompatActivity {
    ChildManager childManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);
        childManager = ChildManager.getInstance();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        populateListView();
        showInstruction();
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
            int avatarId = childManager.getChildAvatarId(position);
            imageView.setImageResource(avatarId);
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
        TextView editInstruction = (TextView) findViewById(R.id.tv_edit_instruction);
        if (childManager.getLength() == 0) {
            editInstruction.setVisibility(View.GONE);
            fab.startAnimation(animation);
        } else {
            editInstruction.setVisibility(View.VISIBLE);
            fab.clearAnimation();
        }
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, ViewChildActivity.class);
        return intent;
    }
}