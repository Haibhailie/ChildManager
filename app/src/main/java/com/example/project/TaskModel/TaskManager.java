package com.example.project.TaskModel;

import java.util.ArrayList;

public class TaskManager{
    private ArrayList<Task> taskArrayList = new ArrayList<>();;
    private static TaskManager instance;

    private TaskManager(){}

    public static TaskManager getInstance(){
        if(instance == null) {
            return new TaskManager();
        }
        return instance;
    }

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public void addTask(Task theTask){
        taskArrayList.add(theTask);
    }

    public void removeTask(int index){
        taskArrayList.remove(index);
    }

    public void clear(){
        taskArrayList.removeAll(taskArrayList);
    }

    public int getTaskLength(){
        return taskArrayList.size();
    }

    public Task getTask(int index){
        return taskArrayList.get(index);
    }

}
