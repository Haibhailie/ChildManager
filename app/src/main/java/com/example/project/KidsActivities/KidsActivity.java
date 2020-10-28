/*
    From this activity,
    Parents can get a list of kids name
 */
package com.example.project.KidsActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project.R;

public class KidsActivity extends AppCompatActivity {
    ChildManager childManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids);
        childManager = ChildManager.getInstance();
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kidPos -1 means add new kids
                Intent intent = EditKidsActivity.makeLaunchIntent(KidsActivity.this, -1);
                startActivity(intent);
            }
        });

        populateListView();
        registerClickCallback();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listview_kids);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kids_list, menu);
        return true;
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(KidsActivity.this, R.layout.kid_list, childManager.getChildList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.kid_list, parent, false);
            }
            // Find the lens to work with.
            Child child = childManager.getChild(position);
            // Fill the view.
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(childManager.getChildAvatarID(position));
            // Fill the Info Text
            TextView infoText = (TextView) itemView.findViewById(R.id.text_lensinfo);
            infoText.setText(childManager.getChild(position).toString());
            return itemView;
        }
    }

    // Click kids to get Statistics
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listview_kids);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                Intent intent = KidStatActivity.makeLaunchIntent(KidsActivity.this, position);
//                startActivity(intent);
//            }
//        });

        // long click to edit an existing kid
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = EditKidsActivity.makeLaunchIntent(KidsActivity.this, position);
                startActivity(intent);
                return false;
            }
        });
    }
}