package com.example.project.ChildModel;

import android.net.Uri;

import com.example.project.R;

public class Child {
    private String name;
    private int age;
    private int avatarID; // can only in String type, otherwise crash in shared preference



    private int gender; // 0 for boy, 1 for girl

    public Child(String name, int age, int avatarID, int gender) {
        this.name = name;
        this.age = age;
        this.avatarID = avatarID;
        this.gender = gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public int getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " , age:" + age;
    }
}

