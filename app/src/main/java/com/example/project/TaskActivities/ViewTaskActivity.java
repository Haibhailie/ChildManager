package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private TaskManager taskManager = TaskManager.getInstance();
    private ArrayList<String> taskList = new ArrayList<>();
    private final String TAG = "ViewTaskActivity";
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        addDemoTasks();
        createDisplayArrayList();
    }

    public void addDemoTasks(){
        taskManager.addTask(new Task("Trash", 2, "Take the trash out!"));
        taskManager.addTask(new Task("Utensils", 1, "Wash the damn utensils"));
        Log.d(TAG, "Size before calling adapter is: "+taskManager.getTaskLength());
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