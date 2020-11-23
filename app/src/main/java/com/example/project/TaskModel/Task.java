package com.example.project.TaskModel;

import com.example.project.ChildModel.ChildManager;

public class Task {
    private String taskName, description;
    private String theAssignedChildId;
    private String avatarID;
    private ChildManager childManager = ChildManager.getInstance();

    public Task(String taskName, String theAssignedChildId, String description, String avatarID){
        this.taskName = taskName;
        this.theAssignedChildId = theAssignedChildId;
        this.avatarID = avatarID;
        this.description = description;
    }

    public void refreshChildInstance(){
        childManager = ChildManager.getInstance();
    }

    public String getAvatarId() {
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
            if(stringIsNull(theAssignedChildId)) {
                presentChildIndex = -1;
                break;
            }
            else if(theAssignedChildId.compareTo(childManager.getChildName(i))==0){
                presentChildIndex=i;
                break;
            }
        }
        if(presentChildIndex==-1){
            presentChildIndex=0;
        }
        else if(presentChildIndex==childManager.getLength()-1){
            presentChildIndex=0;
        }
        else{
            presentChildIndex++;
        }
        try{
        theAssignedChildId=childManager.getChildName(presentChildIndex);
        avatarID=childManager.getChildAvatarUriPath(presentChildIndex);
        }catch (Exception e){ }
    }

    private boolean stringIsNull(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
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