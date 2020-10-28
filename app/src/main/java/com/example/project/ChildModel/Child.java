package com.example.project.ChildModel;

import com.example.project.R;

public class Child {
    private String name;
    private int age;
    private int avatarID;

    public Child(String name, int age, int avatarID) {
        this.name = name;
        this.age = age;
        this.avatarID = avatarID;
    }

    public Child(String name, int age) {
        this.name = name;
        this.age = age;
        this.avatarID = R.drawable.default_avator;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getImage() {
        return avatarID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImage(int image) {
        this.avatarID = image;
    }

    public int getAvatarID() {
        return avatarID;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " , age:" + age;
    }
}

