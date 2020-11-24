package com.example.project.coinflipactivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.example.project.BuildConfig;
import com.example.project.childmodel.Child;
import com.example.project.childmodel.ChildManager;
import com.example.project.coinflipmodel.CoinFlipQueue;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class ChangeChildCoinFlipActivity extends AppCompatActivity {

    private ChildManager childManager;
    private CoinFlipQueue childQueue;

    private List<Child> childrenInOrder = new ArrayList<>();

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
        childQueue = CoinFlipQueue.getInstance();
        getListOfChildrenInOrder();

        populateListView();
        registerListItemClickCallback();
    }

    private void getListOfChildrenInOrder(){
        for(int id : childQueue.getQueue()){
            childrenInOrder.add(childManager.getChild(childManager.findChildIndexById(id)));
        }
        childrenInOrder.add(new Child("Anonymous Flip", 0, "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_avator, 0,-2));
    }

    private void populateListView(){
        ListAdapter adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.coin_flip_change_child_child_list);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter(){
            super(ChangeChildCoinFlipActivity.this, R.layout.child_list, childrenInOrder);
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
            Uri avatarUri = Uri.parse(childrenInOrder.get(position).getAvatarUriPath());
            // Avatar photo may be deleted, if so we use default avatar
            try {
                imageView.setImageURI(avatarUri);
            } catch (RuntimeException e) {
                imageView.setImageURI(Child.DEFAULT_URI);
            }

            // Text:
            TextView itemText = (TextView) itemView.findViewById(R.id.text_childinfo);
            String item = String.format("%s", childrenInOrder.get(position).getName());
            itemText.setText(item);

            return itemView;
        }
    }

    private void registerListItemClickCallback(){
        ListView list = (ListView) findViewById(R.id.coin_flip_change_child_child_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index;
                if(childrenInOrder.get(position).getId() != -2) {
                    index = childManager.findChildIndexById(childrenInOrder.get(position).getId());
                } else {
                    index = -2;
                }
                Intent intent = ChooseChildCoinFlipActivity.makeLaunchIntent(ChangeChildCoinFlipActivity.this, index);
                startActivity(intent);
                finish();
            }
        });
    }

}