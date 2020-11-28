package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.childactivities.ViewChildActivity;
import com.example.project.coinmodelactivities.ChooseChildCoinFlipActivity;
import com.example.project.takebreathactivities.TakeBreathActivity;
import com.example.project.taskactivities.ViewTaskActivity;
import com.example.project.timeout.Timeout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

/**
* Main menu:
* setup Configure Button
* setup Countdown Button
* setup Flip Coin Button
* setup About Us Button
* setup tool bar and action bar
* */

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupConfigureButton();
        setupCountdownButton();
        setupFlipCoinButton();
        setupAboutUsButton();
        setupTaskButton();
        setupBreathButton();
    }

    private void setupConfigureButton() {
        Button configure = (Button) findViewById(R.id.configureButton);
        configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewChildActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupCountdownButton() {
        Button countDown = (Button) findViewById(R.id.countdownButton);
        countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Timeout.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupFlipCoinButton() {
        Button flip = (Button) findViewById(R.id.flipButton);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChooseChildCoinFlipActivity.makeLaunchIntent(MainActivity.this, -1);
                startActivity(intent);
            }
        });
    }

    private void setupAboutUsButton() {
        Button about = (Button) findViewById(R.id.aboutButton);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AboutUs.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupTaskButton() {
        Button task = (Button) findViewById(R.id.taskButton);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewTaskActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupBreathButton() {
        Button task = (Button) findViewById(R.id.breathButton);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TakeBreathActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}