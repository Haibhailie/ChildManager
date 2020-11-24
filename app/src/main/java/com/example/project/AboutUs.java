package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

/*
 * About Us:
 * Introducing each person in the group
 * Putting random gif at the end
 * Activate the button press from the main menu
 * */

public class AboutUs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupText();
        setupFlipButton();
    }

    public void setupText(){
        TextView introParagraph = findViewById(R.id.textAboutUs);
        introParagraph.setText(Html.fromHtml(getString(R.string.about_us_intro_text)));
    }

    public void setupFlipButton(){

        final ToggleButton flipButton = findViewById(R.id.flipButton);

        flipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    TextView title = findViewById(R.id.welcome);
                    title.setText(getString(R.string.citation_title));
                    TextView introParagraph = findViewById(R.id.textAboutUs);
                    flipButton.setText("ABOUT");
                    introParagraph.setText(Html.fromHtml(getString(R.string.citations)));
                    introParagraph.setMovementMethod(LinkMovementMethod.getInstance());
                    introParagraph.setLinkTextColor(Color.BLUE);
                }
                else {
                    TextView title = findViewById(R.id.welcome);
                    title.setText(getString(R.string.who_are_we));
                    TextView introParagraph = findViewById(R.id.textAboutUs);
                    flipButton.setText("ABOUT");
                    introParagraph.setText(Html.fromHtml(getString(R.string.about_us_intro_text)));
                    introParagraph.setMovementMethod(LinkMovementMethod.getInstance());
                    introParagraph.setLinkTextColor(Color.BLUE);

                }
            }
        });
    }

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, AboutUs.class);
        return intent;
    }
}