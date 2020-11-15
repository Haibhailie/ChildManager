package com.example.project.CoinFlipActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.R;

import java.util.ArrayList;

public class ChangeChildCoinFlipActivity extends AppCompatActivity {

    ArrayList<Child> childOrderList;
    private ChildManager childManager;

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, ChangeChildCoinFlipActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_child_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        childManager = ChildManager.getInstance();

        populateListView();
        registerListItemClickCallback();
    }

    private void populateListView(){
        ListAdapter adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.coin_flip_change_child_child_list);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter(){
            // TODO CHANGE THE ORDERED CHILD LIST
            super(ChangeChildCoinFlipActivity.this, R.layout.child_list, childManager.getChildList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.child_list, parent, false);
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
        ListView list = (ListView) findViewById(R.id.coin_flip_change_child_child_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO ADD ON CLICK
            }
        });
    }

}