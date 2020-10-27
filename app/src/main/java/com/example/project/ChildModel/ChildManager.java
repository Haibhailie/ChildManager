package com.example.project.ChildModel;

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

    public int getChildAvatarID(int index) {
        return childList.get(index).getAvatarID();
    }
}
