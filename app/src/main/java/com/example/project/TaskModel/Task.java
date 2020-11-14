package com.example.project.TaskModel;

import com.example.project.ChildModel.Child;

public class Task {
    private String taskName, description;
    private Child theNextChild;

    public Task(String taskName, Child theNextChild, String description){
        this.taskName = taskName;
        this.theNextChild = theNextChild;
        this.description = description;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Child getTheNextChild() {
        return theNextChild;
    }

    public void setTheNextChild(Child theNextChild) {
        this.theNextChild = theNextChild;
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
