package com.example.project.takebreathactivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.MainActivity;
import com.example.project.taskactivities.ViewTaskActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.project.R;

public class TakeBreathActivity extends AppCompatActivity {

    public static Intent makeLaunchIntent(MainActivity context) {
        Intent intent = new Intent(context, TakeBreathActivity.class);
        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupBeginButton();
    }

    private void setupBeginButton() {

    }
}