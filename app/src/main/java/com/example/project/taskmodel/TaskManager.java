package com.example.project.taskmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Task manager singleton class that holds all the details for available tasks in the application
 * The set instance rewrites the empty instance with one that is taken from sharedpreferences if applicable
 * It has an array list of tasks and functions to receive certain necessary data from it
 */
public class TaskManager implements Iterable<Task>{
    private static TaskManager instance;
    private ArrayList<Task> taskArrayList = new ArrayList<>();
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

    public void editTaskWithIndex(int position, String taskName, String taskDescription, String childAssigned, String avatarID){
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

    @NonNull @Override public Iterator<Task> iterator() {
        return taskArrayList.iterator();
    }
}
