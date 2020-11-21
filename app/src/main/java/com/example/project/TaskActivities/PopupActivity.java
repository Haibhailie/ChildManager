package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    private TaskManager taskManager = TaskManager.getInstance();
    Task selectedTask;
    private ArrayList<Task> taskList = taskManager.getTaskArrayList();
    private int position;
    private static final String EXTRA_TASK_POS = "taskPos";
    private TextView taskName, taskDescription, assignedChild;
    private ImageView childIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        position = b.getInt(EXTRA_TASK_POS);
        selectedTask = taskManager.getTask(position);

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

        taskName.setText(selectedTask.getTaskName());
        taskDescription.setText(selectedTask.getDescription());
        assignedChild.setText(selectedTask.getTheAssignedChildId());
        childIcon.setImageResource(taskList.get(position).getAvatarId());
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
                finish();
            }
        });

    }

    private void setupConfirmButton() {
        Button finishButton = (Button)findViewById(R.id.confirmTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskManager.getTask(position).setNextChildInQueue();
                Intent intent = ViewTaskActivity.makeLaunchIntent(PopupActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Intent makeLaunchIntent (Context context, int taskPos) {
        Intent intent = new Intent(context, PopupActivity.class);
        intent.putExtra(EXTRA_TASK_POS, taskPos);
        return intent;
    }

    @Override
    public void onBackPressed(){
        Intent intent = ViewTaskActivity.makeLaunchIntent(PopupActivity.this);
        finish();
        startActivity(intent);
    }
}