/**
 * Each user added child is an object of Child class
 * Child object stores general information of a child
 */


package com.example.project.childmodel;

import android.net.Uri;

import com.example.project.BuildConfig;
import com.example.project.R;

public class Child {
    public static final Uri DEFAULT_URI = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_avator);
    private String name;
    private int age;
    private String avatarUriPath;
    private int ID;
    private int gender; // 0 for boy, 1 for girl

    //initialize
    public Child(String name, int age, String avatarUriPath, int gender, int ID) {
        this.ID = ID;
        this.name = name;
        this.avatarUriPath = avatarUriPath;
        this.age = age;
        this.gender = gender;
    }

    public void setAvatarUriPath(String path) {
        this.avatarUriPath = path;
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

    public int getId() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public String getAvatarUriPath() {
        return avatarUriPath;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " , age:" + age;
    }
}

