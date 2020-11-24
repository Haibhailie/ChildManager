package com.example.project.taskactivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.project.childmodel.Child;
import com.example.project.taskmodel.Task;
import com.example.project.taskmodel.TaskManager;
import com.example.project.childmodel.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    private TaskManager taskManager = TaskManager.getInstance();
    Task selectedTask;
    private ChildManager childManager = ChildManager.getInstance();
    private ArrayList<Task> taskList = taskManager.getTaskArrayList();
    private int position;
    private static final String EXTRA_TASK_POS = "taskPos";
    private TextView taskName, taskDescription, assignedChild;
    private ImageView childIcon;
    private static final String TASK_PREFS_NAME = "TaskList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewTaskActivity.makeLaunchIntent(PopupActivity.this);
                startActivity(intent);
                finish();
            }
        });
        Bundle b = getIntent().getExtras();
        position = b.getInt(EXTRA_TASK_POS);
        selectedTask = taskManager.getTask(position);
        selectedTask.refreshChildInstance();
        setupInfo();
        setupEditButton();
        setupConfirmButton();
        setupFinishButton();
    }

    private void setupInfo() {
        taskName = findViewById(R.id.popupTaskName);
        taskDescription = findViewById(R.id.popupDescription);
        assignedChild =findViewById(R.id.popupChild);
        childIcon = findViewById(R.id.childIcon);
        Button finishButton = (Button)findViewById(R.id.confirmTask);

        taskName.setText(selectedTask.getTaskName());
        taskDescription.setText(selectedTask.getDescription());
        if(stringIsNull(selectedTask.getTheAssignedChildId())) {
            assignedChild.setText("UNASSIGNED");
            finishButton.setText("Assign");
        }
        else
            assignedChild.setText(selectedTask.getTheAssignedChildId());

        String avatarID = taskList.get(position).getAvatarId();

        try {
            Uri avatarUri = Uri.parse(avatarID);
            childIcon.setImageURI(avatarUri);
        } catch (RuntimeException e) {
            childIcon.setImageURI(Child.DEFAULT_URI);
        }
    }

    public void saveTasks(){
        SharedPreferences prefs = this.getSharedPreferences(TASK_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(taskManager);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("taskManager", json);
        editor.apply();
        editor.commit();
    }

    private void setupEditButton() {
        Button editButton = (Button)findViewById(R.id.editTask);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditTaskActivity.makeLaunchIntent(PopupActivity.this, position);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupFinishButton() {
        Button finishButton = (Button)findViewById(R.id.finishTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewTaskActivity.makeLaunchIntent(PopupActivity.this);
                startActivity(intent);
                taskManager.removeTask(position);
                saveTasks();
                finish();
            }
        });

    }

    private void setupConfirmButton() {

        Button finishButton = (Button)findViewById(R.id.confirmTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childManager.getLength()==0)
                    Toast.makeText(v.getContext(), "No child configured yet! Add one first.", Toast.LENGTH_SHORT).show();
                taskManager.getTask(position).setNextChildInQueue();
                Intent intent = ViewTaskActivity.makeLaunchIntent(PopupActivity.this);
                startActivity(intent);
                saveTasks();
                finish();
            }
        });
    }

    public static Intent makeLaunchIntent (Context context, int taskPos) {
        Intent intent = new Intent(context, PopupActivity.class);
        intent.putExtra(EXTRA_TASK_POS, taskPos);
        return intent;
    }

    private boolean stringIsNull(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }


    @Override
    public void onBackPressed(){
        Intent intent = ViewTaskActivity.makeLaunchIntent(PopupActivity.this);
        finish();
        startActivity(intent);
    }
}