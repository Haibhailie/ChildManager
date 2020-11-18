package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.project.ChildActivities.EditChildActivity;
import com.example.project.ChildActivities.ViewChildActivity;
import com.example.project.ChildModel.Child;
import com.example.project.R;
import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ViewTaskActivity extends AppCompatActivity {
    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskManager = TaskManager.getInstance();
        //List<Task> tasks =

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupBasic();
        populateListView();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateListView();
        setupBasic();
    }

    private void setupBasic() {
        //fab
        FloatingActionButton fab = findViewById(R.id.taskFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditTaskActivity.makeLaunchIntent(ViewTaskActivity.this, -1);
                startActivity(intent);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_blink);


    }

    private void populateListView() {
        ArrayList<String> newArray = new ArrayList<>();
        for(int i = 0; i < taskManager.getTaskLength(); i++){
            String each ="  " + taskManager.getTask(i).toString();
            newArray.add(each);
        }

        String[] arr = newArray.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.task_list,arr);

        ListView listView = (ListView)findViewById(R.id.taskListview);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, ViewTaskActivity.class);
        return intent;
    }


}