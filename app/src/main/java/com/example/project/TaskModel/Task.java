package com.example.project.TaskModel;

import android.widget.Toast;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String taskName, description;
    private String theAssignedChildId;
    private int avatarID;
    private ChildManager childManager = ChildManager.getInstance();

    public Task(String taskName, String theAssignedChildId, String description, int avatarID){
        this.taskName = taskName;
        this.theAssignedChildId = theAssignedChildId;
        this.avatarID = avatarID;
        this.description = description;
    }

    public int getAvatarId() {
        return avatarID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTheAssignedChildId() {
        return theAssignedChildId;
    }

    public void setNextChildInQueue(){
        int presentChildIndex=0;
        for(int i=0;i<childManager.getLength();i++){
            if(theAssignedChildId.compareTo(childManager.getChildName(i))==0){
                presentChildIndex=i;
                break;
            }
        }
        if(presentChildIndex==childManager.getLength()-1){
            presentChildIndex=0;
        }
        else{
            presentChildIndex++;
        }
        theAssignedChildId=childManager.getChildName(presentChildIndex);
        avatarID=childManager.getChildAvatarId(presentChildIndex);
    }

    public void setTheAssignedChild(int index) {
        this.theAssignedChildId = childManager.getChildName(index);
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