package com.example.project.ChildModel;

import android.net.Uri;

import com.example.project.KidsActivities.EditKidsActivity;

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

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public void setChildAvatarId(int index, int id) {
        childList.get(index).setAvatarId(id);
    }

}
