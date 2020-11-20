package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;
import com.example.project.R;
import com.example.project.TaskModel.RecyclerViewAdapter;
import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewTaskActivity extends AppCompatActivity {
    private TaskManager taskManager = TaskManager.getInstance();
    private ArrayList<String> taskList = new ArrayList<>();
    private final String TAG = "ViewTaskActivity";
    private RecyclerViewAdapter adapter;
    private ChildManager childManager = ChildManager.getInstance();
    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String CHILD_PREFS_NAME = "ChildList";
    private static final String TASK_PREFS_NAME = "TaskList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        loadTaskData();
        retrieveTasks();
        createDisplayArrayList();

        FloatingActionButton taskFab = findViewById(R.id.taskFab);
        taskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddTaskActivity.makeLaunchIntent(ViewTaskActivity.this);
                startActivity(intent);
                finish();

            }
        });
    }

    public void loadTaskData(){
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CHILD_PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        List<Child> childList = gson.fromJson(json, type);

        if (childList != null) {
            childManager.setChildList(childList);
        }
    }

    public void retrieveTasks(){
        SharedPreferences prefs = this.getSharedPreferences(TASK_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(TASK_PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> retrievedTaskList = gson.fromJson(json, type);

        if (retrievedTaskList != null) {
            taskManager.setTaskArrayList(retrievedTaskList);
        }
    }

    public void createDisplayArrayList(){
        Log.d(TAG, "createDisplayArrayList: Moving object array list onto recycler view");
        int serialNo = 1;
        for(Task t:taskManager){
            taskList.add(serialNo+".\t"+t.getTaskName()+"\t"+t.getTheAssignedChildId());
            serialNo++;
        }
        initializeRecyclerView();
    }

    private void initializeRecyclerView(){
        Log.d(TAG, "initializeRecyclerView: Entered Method");
        RecyclerView recyclerView = findViewById(R.id.taskRecyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, ViewTaskActivity.class);
        return intent;
    }

}