package com.example.project.KidsActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;

public class EditKidsActivity extends AppCompatActivity {
    private static final String EXTRA_IDX = "kidPos";
    private ChildManager childManager;
    int childPos;
    EditText nameText, ageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kids);
        childManager = ChildManager.getInstance();
        nameText = (EditText) findViewById(R.id.et_kids_name);
        ageText = (EditText) findViewById(R.id.et_kids_age);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" on toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        childPos = extraKidPosFromIntent();
        // If we are going to edit an existing kid
        // show original value in each fields
        if (childPos != -1) {
            setupEditModel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                String name = nameText.getText().toString();
                String ageStr = ageText.getText().toString();
                // First check if all fields are filled
                if (name.equals("")) {
                    Toast.makeText(EditKidsActivity.this,
                            "Please enter name of child",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else if (ageStr.equals("")){
                    Toast.makeText(EditKidsActivity.this,
                            "Please enter kid age",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Then check if all fields are valid
                int age = Integer.parseInt(ageStr);
                if (age < 0) {
                    Toast.makeText(EditKidsActivity.this,
                            "age of kid > 0",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (childPos == -1) {
                    // Add new lens
                    Child child = new Child(name, age);
                    childManager.add(child);
                } else {
                    // edit existed lens
                    childManager.setChildName(childPos, name);
                    childManager.setChildAge(childPos, age);
                }
                Toast.makeText(EditKidsActivity.this, "Kid Information Saved!", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private int extraKidPosFromIntent() {
        Intent intent = getIntent();
        return intent.getIntExtra(EXTRA_IDX, -1);
    }

    private void setupEditModel() {
        nameText.setText(childManager.getChildName(childPos));
        ageText.setText(childManager.getChildAge(childPos) + "");
    }

    public static Intent makeLaunchIntent (Context context, int kidPos) {
        Intent intent = new Intent(context, EditKidsActivity.class);
        intent.putExtra(EXTRA_IDX, kidPos);
        return intent;
    }
}