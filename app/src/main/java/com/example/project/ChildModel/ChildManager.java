/*
    * Class with singleton support
    * return an instance which have a list (childList) of all added children in the program
    * can manipulate added child from the instance
 */

package com.example.project.ChildModel;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChildManager {
    private static ChildManager instance;

    private List<Child> childList = new ArrayList<>();
    private ChildManager() {}

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public Child getChild(int index) {
        return childList.get(index);
    }

    public int getChildAvatarId(int index) {
        return childList.get(index).getAvatarId();
    }

    public String getChildName(int index) {
        return childList.get(index).getName();
    }

    public int getChildAge(int index) {
        return childList.get(index).getAge();
    }

    public void add(Child child) {
        childList.add(child);
    }

    public void setChildName(int index, String name) {
        childList.get(index).setName(name);
    }

    public void setChildAge(int index, int age) {
        childList.get(index).setAge(age);
    }

    public void setChildGender(int index, int gender) {
        childList.get(index).setGender(gender);
    }

    public int getChildGender(int index) {
        return childList.get(index).getGender();
    }

    public int getChildID(int index) {
        return childList.get(index).getID();
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public void setChildAvatarId(int index, int id) {
        childList.get(index).setAvatarId(id);
    }

    public void deleteChild(int index) {
        childList.remove(index);
    }

    public int getLength() {
        return childList.size();
    }

    public int findChildIndexById(int id){

        for(int i = 0; i < this.getLength(); i++){
            if(this.getChildID(i) == id){
                return i;
            }
        }

        Log.println(Log.ERROR, "CHILD", "Missing Child " + id);
        return -1;
    }

}
