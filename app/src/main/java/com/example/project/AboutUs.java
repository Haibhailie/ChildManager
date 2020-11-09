package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class AboutUs extends AppCompatActivity {

    private GifImageView calmingBGVideo;
    private int[] gifSelector = {R.drawable.developers, R.drawable.refactoring, R.drawable.spaces_not_tabs, R.drawable.wait_for_it};

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
        calmingBGVideo = (GifImageView) findViewById(R.id.memeGif);
        setupText();
        setupRandomGif();
    }

    public void setupRandomGif(){
        Random random = new Random();
        int gifNumber = random.nextInt(4)+0;
        calmingBGVideo.setBackgroundResource(gifSelector[gifNumber]);
    }

    public void setupText(){
        TextView introParagraph = findViewById(R.id.textAboutUs);
        introParagraph.setText(Html.fromHtml(getString(R.string.about_us_intro_text)));

        TextView kellyParagraph = findViewById(R.id.textAboutKelly);
        kellyParagraph.setText(Html.fromHtml(getString(R.string.kelly_about)));

        TextView ziruiParagraph = findViewById(R.id.textAboutZirui);
        ziruiParagraph.setText(Html.fromHtml(getString(R.string.zirui_about)));

        TextView knexParagraph = findViewById(R.id.textAboutKent);
        knexParagraph.setText(Html.fromHtml(getString(R.string.knex_about)));

        TextView shanksParagraph = findViewById(R.id.textAboutShanks);
        shanksParagraph.setText(Html.fromHtml(getString(R.string.shanks_about)));

    }
    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, AboutUs.class);
        return intent;
    }
}