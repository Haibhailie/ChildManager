package com.example.project.ChildModel;

import android.net.Uri;

import com.example.project.R;

public class Child {
    private String name;
    private int age;
    private int avatarID; // can only in String type, otherwise crash in shared preference

    public Child(String name, int age, int avatarID) {
        this.name = name;
        this.age = age;
        this.avatarID = avatarID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getAvatarId() {
        return avatarID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAvatarId(int id) {
        this.avatarID = id;
    }


    @Override
    public String toString() {
        return "Name: " + name +
                " , age:" + age;
    }
}

