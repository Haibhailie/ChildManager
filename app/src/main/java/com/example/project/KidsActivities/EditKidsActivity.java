package com.example.project.KidsActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditKidsActivity extends AppCompatActivity {
    private static final String EXTRA_IDX = "kidPos";
    private ChildManager childManager;
    int childPos;
    EditText nameText, ageText;
    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String CHILD_PREFS_NAME = "ChildList";
    private static final int PICK_IMAGE = 100;
    private int avatarId;
    private int gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kids);
        childManager = ChildManager.getInstance();
        nameText = (EditText) findViewById(R.id.et_kids_name);
        ageText = (EditText) findViewById(R.id.et_kids_age);
        avatarId = R.drawable.default_avator;

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

        setupAvatarOption();
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
                getGender();
                if (childPos == -1) {
                    // Add new lens
                    Child child = new Child(name, age, avatarId, gender);
                    childManager.add(child);
                    Toast.makeText(EditKidsActivity.this, "New Kid Added!", Toast.LENGTH_SHORT).show();
                } else {
                    // edit existed lens
                    childManager.setChildName(childPos, name);
                    childManager.setChildAge(childPos, age);
                    childManager.setChildAvatarId(childPos, avatarId);
                    childManager.setChildGender(childPos, gender);
                    Toast.makeText(EditKidsActivity.this, "Kid Info Updated!", Toast.LENGTH_SHORT).show();
                }
                saveKidsRecord(EditKidsActivity.this, childManager.getChildList());
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getGender() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group_gender);
        int radioId = radioGroup.getCheckedRadioButtonId();
        if (radioId == R.id.radio_btn_boy) {
            gender = 0;
        } else {
            gender = 1;
        }
    }

    private int extraKidPosFromIntent() {
        Intent intent = getIntent();
        return intent.getIntExtra(EXTRA_IDX, -1);
    }

    private void setupEditModel() {
        nameText.setText(childManager.getChildName(childPos));
        ageText.setText(childManager.getChildAge(childPos) + "");
        int gender = childManager.getChildGender(childPos);
        RadioButton boyBtn = (RadioButton) findViewById(R.id.radio_btn_boy);
        RadioButton girlBtn = (RadioButton) findViewById(R.id.radio_btn_girl);
        if (gender == 0) {
            boyBtn.setChecked(true);
            girlBtn.setChecked(false);
        } else {
            boyBtn.setChecked(false);
            girlBtn.setChecked(true);
        }
    }

    public static Intent makeLaunchIntent (Context context, int kidPos) {
        Intent intent = new Intent(context, EditKidsActivity.class);
        intent.putExtra(EXTRA_IDX, kidPos);
        return intent;
    }



    // Reference: https://www.youtube.com/watch?v=jcliHGR3CHo&ab_channel=CodinginFlow
    public static void saveKidsRecord(Context context, List<Child> childList) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(childList);
        editor.putString(CHILD_PREFS_NAME, json);
        editor.apply();
    }

    public static List<Child> getKidsRecord(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CHILD_PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        List<Child> childList = gson.fromJson(json, type);
        return childList;
    }

    private void setupAvatarOption() {

        ImageView boy1 = (ImageView) findViewById(R.id.iv_boy1);
        boy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.b_avatar1;
            }
        });
        ImageView boy2 = (ImageView) findViewById(R.id.iv_boy2);
        boy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.b_avatar2;
            }
        });
        ImageView boy3 = (ImageView) findViewById(R.id.iv_boy3);
        boy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.b_avatar3;
            }
        });
        ImageView boy4 = (ImageView) findViewById(R.id.iv_boy4);
        boy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.b_avatar4;
            }
        });
        ImageView boy5 = (ImageView) findViewById(R.id.iv_boy5);
        boy5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.b_avatar5;
            }
        });
        ImageView girl1 = (ImageView) findViewById(R.id.iv_girl1);
        girl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.g_avatar1;
            }
        });
        ImageView girl2 = (ImageView) findViewById(R.id.iv_girl2);
        girl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.g_avatar2;
            }
        });
        ImageView girl3 = (ImageView) findViewById(R.id.iv_girl3);
        girl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.g_avatar3;
            }
        });
        ImageView girl4 = (ImageView) findViewById(R.id.iv_girl4);
        girl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.g_avatar4;
            }
        });
        ImageView girl5 = (ImageView) findViewById(R.id.iv_girl5);
        girl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditKidsActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                avatarId = R.drawable.g_avatar5;
            }
        });

    }


}