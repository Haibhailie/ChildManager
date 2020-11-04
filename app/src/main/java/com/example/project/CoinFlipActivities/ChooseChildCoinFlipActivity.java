package com.example.project.CoinFlipActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.KidsActivities.EditKidsActivity;
import com.example.project.KidsActivities.KidsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project.R;

import java.util.List;

public class ChooseChildCoinFlipActivity extends AppCompatActivity {

    private final String CHILDMANAGER_TAG = "ChildManager";
    private ChildManager childManager;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, ChooseChildCoinFlipActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        childManager = ChildManager.getInstance();
        loadChildData();

        // Enable "up" on toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.println(Log.INFO, CHILDMANAGER_TAG, childManager.getLength() + "");
        populateListView();
        registerClickCallback();
    }

    // Populates the List View with lenses.
    private void populateListView(){

        ListAdapter adapter = new MyListAdapter();

        // Configure
        ListView list = (ListView) findViewById(R.id.coin_flip_child_list_view);
        list.setAdapter(adapter);
    }

    // Adds an adapter to the list view. Allows for on images to be added.
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

            // Find the lens to work with
            Child currentChild = childManager.getChild(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currentChild.getAvatarId());

            // Text:
            TextView itemText = (TextView) itemView.findViewById(R.id.text_lensinfo);
            String item = String.format("%s", currentChild.getName());
            itemText.setText(item);

            return itemView;
        }
    }

    // Adds an on click event for each listView item.
    private void registerClickCallback(){
        ListView list = (ListView) findViewById(R.id.coin_flip_child_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = CoinFlipActivity.makeLaunchIntent(ChooseChildCoinFlipActivity.this, position);
                startActivity(intent);
            }
        });
    }

    // Possible TODO: Change the childManager to laod on MainActivity instead of EditKidsActivity
    private void loadChildData(){
        if(childManager.getLength() == 0){
            List<Child> savedChildList = EditKidsActivity.getKidsRecord(ChooseChildCoinFlipActivity.this);
            if (savedChildList != null) {
                childManager.setChildList(savedChildList);
                Log.println(Log.INFO, CHILDMANAGER_TAG, "Loaded Child List from EditKidsActivity");
            }
        }
    }
}

