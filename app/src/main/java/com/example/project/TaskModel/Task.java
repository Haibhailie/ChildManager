package com.example.project.TaskModel;

import android.widget.Toast;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;

public class Task {
    private String taskName, description, theAssignedChild;
    private ChildManager childManager = ChildManager.getInstance();
    private Child theChild;

    public Task(String taskName, String theAssignedChild, String description){
        this.taskName = taskName;
        this.theAssignedChild = theAssignedChild;
        this.description = description;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTheAssignedChild() {
        return theAssignedChild;
    }

    public void setTheAssignedChild(String theAssignedChild) {
        for (int i = 0; i < childManager.getLength(); i++) {
            if(childManager.getChildName(i) == theAssignedChild){
                //assigned
                theChild = childManager.getChild(i);
            } else{
                // let the parent to configure the child
                System.out.println("Please configure the child.");
            }
        }

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
