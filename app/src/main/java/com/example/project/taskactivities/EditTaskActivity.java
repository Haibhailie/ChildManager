package com.example.project.taskactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.project.childmodel.Child;
import com.example.project.childmodel.ChildManager;
import com.example.project.taskmodel.Task;
import com.example.project.taskmodel.TaskManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {
    private TaskManager taskManager = TaskManager.getInstance();
    private ChildManager childManager = ChildManager.getInstance();
    private String enteredTaskName;
    private String enteredDescription;
    private String enteredChildName;
    private List<Child> childList = new ArrayList<>();
    private Spinner childSpinner;
    private EditText taskName;
    private String avatarID;
    private EditText taskDescription;
    private Button submitButton;
    Task selectedTask;
    private ImageView childAvatar;
    private int taskClickedPosition;
    private static final String EXTRA_TASK_POS = "taskPos";
    private static final String TASK_PREFS_NAME = "TaskList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewTaskActivity.makeLaunchIntent(EditTaskActivity.this);
                startActivity(intent);
                finish();
            }
        });

        Bundle b = getIntent().getExtras();
        taskClickedPosition = b.getInt(EXTRA_TASK_POS);
        selectedTask=taskManager.getTask(taskClickedPosition);
        setupInputResources();
    }

    @Override
    public void onBackPressed(){
        Intent intent = ViewTaskActivity.makeLaunchIntent(EditTaskActivity.this);
        finish();
        startActivity(intent);
    }

    public void setupInputResources(){
        taskName = findViewById(R.id.editTextTaskName);
        taskDescription = findViewById(R.id.editTextTaskDescription);
        childSpinner = findViewById(R.id.childSelectSpinner);
        submitButton = findViewById(R.id.submitNewTask);
        childAvatar = findViewById(R.id.childAvatar);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeInputsAndExit();
                Intent intent = ViewTaskActivity.makeLaunchIntent(EditTaskActivity.this);
                saveTasks();
                finish();
                startActivity(intent);

            }
        });
        setInitialValues();
        initializeChildSpinner();
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

    public void setInitialValues(){
        taskName.setText(selectedTask.getTaskName());
        taskDescription.setText(selectedTask.getDescription());

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
            taskManager.editTaskWithIndex(taskClickedPosition, enteredTaskName, enteredDescription, enteredChildName, avatarID);
        }
    }

    public void initializeChildSpinner(){
        TextView emptyChildren = findViewById(R.id.NoChildrenText);
        emptyChildren.setVisibility(View.GONE);
        childList = childManager.getChildList();
        int defaultPosition=0, i=0;
        final List<String> childNameList = new ArrayList<>();
        for(Child t:childList){
            if(t.getName().compareTo(selectedTask.getTheAssignedChildId())==0)
                defaultPosition=i;
            childNameList.add(t.getName());
            i++;
        }
        if(childList.isEmpty()){
            emptyChildren.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childNameList);
        childSpinner.setAdapter(spinnerAdapter);
        childSpinner.setSelection(defaultPosition);
        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enteredChildName = childSpinner.getSelectedItem().toString();
                avatarID = childManager.getChildAvatarUriPath(position);
                //childAvatar.setImageResource(avatarID);
                Uri avatarUri = Uri.parse(avatarID);

                try {
                    childAvatar.setImageURI(avatarUri);
                } catch (RuntimeException e) {
                    childAvatar.setImageURI(Child.DEFAULT_URI);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                enteredChildName = taskManager.getTask(taskClickedPosition).getTheAssignedChildId();

            }
        });
    }

    public static Intent makeLaunchIntent(Context context, int taskPos) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(EXTRA_TASK_POS, taskPos);
        return intent;
    }
}