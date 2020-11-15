package com.example.project.TaskActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.ChildActivities.EditChildActivity;
import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;

public class EditTaskActivity extends AppCompatActivity {
    private TaskManager taskManager;
    EditText nameText, descriptionText, assignedChildText;
    String name, description, assignedChild;
    //Child assignedChild;
    int taskPos;
    private static final String EXTRA_TASK_POS = "taskPos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskManager = TaskManager.getInstance();
        nameText = (EditText) findViewById(R.id.theTaskName);
        descriptionText = (EditText) findViewById(R.id.theTaskDescription);
        assignedChildText = (EditText) findViewById(R.id.assignedChild);

        taskPos = extractTaskPosFromIntent();
        if(taskPos != -1){
            startEditTasks();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                name = nameText.getText().toString();
                description = descriptionText.getText().toString();
                assignedChild = assignedChildText.getText().toString();
                if(name == ""|| description == "" || assignedChild == ""){
                    Toast.makeText(EditTaskActivity.this,
                            "Please enter valid input",
                            Toast.LENGTH_SHORT).show();
                }

                if(taskPos == -1){
                    Task newTask = new Task(name, assignedChild, description);
                    taskManager.addTask(newTask);
                } else{
                    taskManager.getTask(taskPos).setTaskName(name);
                    taskManager.getTask(taskPos).setDescription(description);
                    taskManager.getTask(taskPos).setTheAssignedChild(assignedChild);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int extractTaskPosFromIntent() {
        Intent intent = getIntent();
        return intent.getIntExtra(EXTRA_TASK_POS, -1);
    }

    private void startEditTasks() {
        nameText.setText(taskManager.getTask(taskPos).getTaskName());
        descriptionText.setText(taskManager.getTask(taskPos).getDescription());
        assignedChildText.setText(taskManager.getTask(taskPos).getTheAssignedChild().toString());
    }

    public static Intent makeLaunchIntent(ViewTaskActivity context, int taskPos) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(EXTRA_TASK_POS, taskPos);
        return intent;
    }
}