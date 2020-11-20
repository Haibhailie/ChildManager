package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.ChildActivities.EditChildActivity;
import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.TaskModel.RecyclerViewAdapter;
import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    private TaskManager taskManager = TaskManager.getInstance();
    private ArrayList<String> taskList = new ArrayList<>();
    private List<Child> childList = new ArrayList<>();
    private final String TAG = "EditTaskActivity";
    private ChildManager childManager = ChildManager.getInstance();
    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String CHILD_PREFS_NAME = "ChildList";
    private static final String TASK_PREFS_NAME = "TaskList";
    private String enteredTaskName;
    private String enteredDescription;
    private String enteredChildName;
    private Spinner childSpinner;
    private EditText taskName;
    private int avatarID;
    private EditText taskDescription;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupInputResources();

    }

    public void setupInputResources(){
        taskName = findViewById(R.id.editTextTaskName);
        taskDescription = findViewById(R.id.editTextTaskDescription);
        childSpinner = findViewById(R.id.childSelectSpinner);
        submitButton = findViewById(R.id.submitNewTask);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeInputsAndExit();
                Intent intent = ViewTaskActivity.makeLaunchIntent(AddTaskActivity.this);
                startActivity(intent);
                finish();
            }
        });
        initializeChildSpinner();
    }

    public void takeInputsAndExit(){
        boolean inputErrorOccured = false;
        if(TextUtils.isEmpty(taskName.getText().toString())){
            taskName.setError("Task name cannot be empty!");
            inputErrorOccured=true;
        }
        if(TextUtils.isEmpty(taskDescription.getText().toString())){
            taskDescription.setError("Task name cannot be empty!");
            inputErrorOccured=true;
        }
        if(inputErrorOccured){
            Toast.makeText(this, "Check your inputs!", Toast.LENGTH_SHORT).show();
        }
        else{
            enteredTaskName=taskName.getText().toString();
            enteredDescription=taskDescription.getText().toString();
            taskManager.addTask(new Task(enteredTaskName, enteredChildName,enteredDescription, avatarID));
        }

    }

    public void initializeChildSpinner(){
        childList = childManager.getChildList();
        final List<String> childNameList = new ArrayList<>();
        for(Child t:childList){
            childNameList.add(t.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childNameList);
        childSpinner.setAdapter(spinnerAdapter);
        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enteredChildName = childSpinner.getSelectedItem().toString();
                avatarID = childManager.getChildAvatarId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                enteredChildName = "Unassigned";
            }
        });
    }

    public static Intent makeLaunchIntent (Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        return intent;
    }
}