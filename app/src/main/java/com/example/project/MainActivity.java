package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.CoinFlipActivities.ChooseChildCoinFlipActivity;
import com.example.project.ChildActivities.ViewChildActivity;
import com.example.project.Timeout.Timeout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

//TODO Change text to string.xml
//TODO Add a comment for each class/activity


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupConfigureButton();
        setupCountdownButton();
        setupFlipCoinButton();
        //setupAboutUsButton();
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
                Intent intent = ChooseChildCoinFlipActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    /*private void setupAboutUsButton() {
        Button flip = (Button) findViewById(R.id.aboutButton);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChooseChildCoinFlipActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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