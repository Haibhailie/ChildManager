/*
    * Class with singleton support
    * return an instance which have a list (childList) of all added children in the program
    * can manipulate added child from the instance
 */

package com.example.project.childmodel;

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

    public int getChildId(int index) {
        int id;

        try{
            id = childList.get(index).getId();
        } catch (ArrayIndexOutOfBoundsException e){
            Log.println(Log.INFO, "ChildManager", "No children in Manager.");
            id = -1;
        }

        return id;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }


    public void deleteChild(int index) {
        childList.remove(index);
    }

    public int getLength() {
        return childList.size();
    }

    public void setChildAvatarUriPath(int index, String path) {
        childList.get(index).setAvatarUriPath(path);
    }

    public String getChildAvatarUriPath (int index) {
        return childList.get(index).getAvatarUriPath();
    }

    public int findChildIndexById(int id){

        for(int i = 0; i < this.getLength(); i++){
            try {
                if (this.getChildId(i) == id) {
                    return i;
                }
            } catch (IndexOutOfBoundsException e){
                Log.println(Log.ERROR, "CHILD", "Missing Child " + id);
            }
        }

        return -1;
    }

}
