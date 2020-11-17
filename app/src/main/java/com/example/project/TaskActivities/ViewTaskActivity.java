package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.ChildActivities.EditChildActivity;
import com.example.project.ChildActivities.ViewChildActivity;
import com.example.project.R;
import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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


    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, ViewTaskActivity.class);
        return intent;
    }
}