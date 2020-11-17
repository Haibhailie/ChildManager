package com.example.project.TaskModel;

import android.widget.Toast;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;

public class Task {
    private String taskName, description;
    private int theAssignedChildId;
    private ChildManager childManager = ChildManager.getInstance();

    public Task(String taskName, int theAssignedChildId, String description){
        this.taskName = taskName;
        this.theAssignedChildId = theAssignedChildId;
        this.description = description;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTheAssignedChildId() {
        return theAssignedChildId;
    }

    public void setTheAssignedChild(int index) {
        this.theAssignedChildId = childManager.getChildID(index);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return taskName;
    }

}
