package com.example.project.TaskModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.project.ChildModel.ChildManager;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskManager implements Iterable<Task>{

    private static TaskManager instance;
    private ArrayList<Task> taskArrayList = new ArrayList<>();;

    private TaskManager(){}

    public void setInstance(TaskManager t){
        instance = t;
    }

    public static TaskManager getInstance(){
        if(instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public void editTaskWithIndex(int position, String taskName, String taskDescription, String childAssigned, int avatarID){
        Task editedTask = new Task(taskName, childAssigned, taskDescription, avatarID);
        taskArrayList.set(position, editedTask);
    }

    public void addTask(Task theTask){
        taskArrayList.add(theTask);
    }

    public void removeTask(int index){
        taskArrayList.remove(index);
    }

    public void clearTask(){
        taskArrayList.removeAll(taskArrayList);
    }

    public int getTaskLength(){
        Log.d("Returning", "length is :" + taskArrayList.size());
        return taskArrayList.size();
    }

    public Task getTask(int index){
        return taskArrayList.get(index);
    }

    @NonNull
    @Override
    public Iterator<Task> iterator() {
        return taskArrayList.iterator();
    }
}
