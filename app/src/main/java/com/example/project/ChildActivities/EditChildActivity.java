/*
    * This activity allow a user to edit a child.
 */

package com.example.project.ChildActivities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.project.ChildModel.Child;
import com.example.project.ChildModel.ChildManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditChildActivity extends AppCompatActivity {
    private static final String EXTRA_CHILD_POS = "childPos";
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int CAMERA_REQUEST_CODE = 124;
    private static final String APP_PREFS_NAME = "AppPrefs";
    private static final String CHILD_PREFS_NAME = "ChildList";
    private static final String CHILD_CURRENT_ID = "ChildID";
    private ChildManager childManager;
    int childPos;
    EditText nameText, ageText;
    ImageView avatarPreview;
    private int gender;
    Uri avatarUri;
    private List<Integer> avatarImageViewArray;
    private List<Integer> avatarResIDArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);
        childManager = ChildManager.getInstance();
        nameText = (EditText) findViewById(R.id.et_child_name);
        ageText = (EditText) findViewById(R.id.et_child_age);
        avatarPreview = (ImageView) findViewById(R.id.iv_avatar_preview);
        avatarUri = null;


        // These two arrays would be used to set click event on imageView (avatar)
        avatarImageViewArray = Arrays.asList(R.id.iv_boy1, R.id.iv_boy2,R.id.iv_boy3,R.id.iv_boy4,R.id.iv_boy5,
                R.id.iv_girl1,R.id.iv_girl2,R.id.iv_girl3,R.id.iv_girl4,R.id.iv_girl5);

        avatarResIDArray = Arrays.asList(R.drawable.b_avatar1, R.drawable.b_avatar2,R.drawable.b_avatar3,R.drawable.b_avatar4,
                R.drawable.b_avatar5,R.drawable.g_avatar1, R.drawable.g_avatar2, R.drawable.g_avatar3, R.drawable.g_avatar4,R.drawable.g_avatar5);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        // Enable "up" on toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        childPos = extractChildPosFromIntent();
        checkPermission();
        setupAvatarOption();
        setupAvatarButton();
        // If we are going to edit an existing child
        // show original value in each fields
        if (childPos != -1) {
            setupEditModel();
        }
    }

    /**
     * When user click the button, a dialog will pop up from the bottom
     * User has two options: take a photo or select image from gallery
     */
    private void setupAvatarButton() {
        Button btn = (Button) findViewById(R.id.btn_custom_avatar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditChildActivity.this,
                        R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.select_avatar_dialog,
                        (LinearLayout) findViewById(R.id.dialog_avatar));
                // set up click event for two buttons
               bottomSheetView.findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFromGallery();
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetView.findViewById(R.id.btn_takephoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        capturePhoto();
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }

            });
    }

    /**
     * Select image from gallery, assign the Uri to child's avatar property
     */
    private void selectFromGallery() {
        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    /**
     * Capture a photo from camera, and save it to external storage
     */
    private void capturePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_REQUEST_CODE);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                ContentResolver resolver = EditChildActivity.this.getContentResolver();
                avatarUri = data.getData();
                resolver.takePersistableUriPermission(avatarUri, takeFlags);
                avatarPreview.setImageURI(avatarUri);
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE && data != null) {
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                avatarPreview.setImageBitmap(capturedImage);
                Calendar cal = Calendar.getInstance();
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), capturedImage, ""+cal.getTimeInMillis(), "description");
                avatarUri = Uri.parse(path);
        }
    }

    /**
     * Check Read/Write permission
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(EditChildActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditChildActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            Log.e("value", "Permission Granted, Now you can take picture .");
        }
        if (ContextCompat.checkSelfPermission(EditChildActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditChildActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            Log.e("value", "Permission Granted, Now you can write to external  .");
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
                    Toast.makeText(EditChildActivity.this,
                            "Please enter name of child",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else if (ageStr.equals("")){
                    Toast.makeText(EditChildActivity.this,
                            "Please enter child age",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else if (avatarUri == null) {
                    Toast.makeText(EditChildActivity.this,
                            "Please select an avatar for child",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Then check if all fields are valid
                int age = Integer.parseInt(ageStr);
                if (age < 0) {
                    Toast.makeText(EditChildActivity.this,
                            "age of child 0",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                getGender();
                if (childPos == -1) {
                    // Add new lens
                    Child child = new Child(name, age, avatarUri.toString(), gender, getChildID());
                    childManager.add(child);
                    Toast.makeText(EditChildActivity.this, "New child Added!", Toast.LENGTH_SHORT).show();
                } else {
                    // edit existed lens
                    childManager.setChildName(childPos, name);
                    childManager.setChildAge(childPos, age);
                    childManager.setChildAvatarUriPath(childPos, avatarUri.toString());
                    childManager.setChildGender(childPos, gender);
                    Toast.makeText(EditChildActivity.this, "child Info Updated!", Toast.LENGTH_SHORT).show();
                }
                saveChildList(EditChildActivity.this, childManager.getChildList());
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

    private int getChildID() {
        int returnValue = 0;

        SharedPreferences prefs = getSharedPreferences(CHILD_CURRENT_ID, MODE_PRIVATE);
        String idString = prefs.getString(CHILD_PREFS_NAME, "0");
        returnValue = Integer.parseInt(idString) + 1;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CHILD_PREFS_NAME, Integer.toString(returnValue));
        editor.apply();
        Log.println(Log.INFO, "CHILD", "Child's ID: " + returnValue);
        return returnValue;
    }

    private int extractChildPosFromIntent() {
        Intent intent = getIntent();
        return intent.getIntExtra(EXTRA_CHILD_POS, -1);
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
        // set up avatar
        avatarUri = Uri.parse(childManager.getChildAvatarUriPath(childPos));
        try {
            avatarPreview.setImageURI(avatarUri);
        } catch (RuntimeException e) {
            avatarPreview.setImageURI(Child.DEFAULT_URI);
        }
    }

    public static Intent makeLaunchIntent (Context context, int childPos) {
        Intent intent = new Intent(context, EditChildActivity.class);
        intent.putExtra(EXTRA_CHILD_POS, childPos);
        return intent;
    }



    // Reference: https://www.youtube.com/watch?v=jcliHGR3CHo&ab_channel=CodinginFlow
    public static void saveChildList(Context context, List<Child> childList) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(childList);
        editor.putString(CHILD_PREFS_NAME, json);
        editor.apply();
    }

    public static List<Child> getSavedChildList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CHILD_PREFS_NAME, null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        List<Child> childList = gson.fromJson(json, type);
        return childList;
    }

    private void setupAvatarOption() {
        for (int i = 0; i < avatarImageViewArray.size(); i++) {
            final ImageView img = (ImageView) findViewById(avatarImageViewArray.get(i));
            final int finalI = i;
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarUri = Uri.parse("android.resource://" + getPackageName() + "/" + avatarResIDArray.get(finalI));
                    avatarPreview.setImageURI(avatarUri);
                    setAvatarsBackground(img);
                }
            });
        }
    }

    /**
     * Upon click a default avatar, the avatar will be highlighted
     */
    private void setAvatarsBackground(ImageView targetImg) {
        for (int i = 0; i < avatarImageViewArray.size(); i++) {
             ImageView imageView = (ImageView) findViewById(avatarImageViewArray.get(i));
             imageView.setBackgroundResource(android.R.color.transparent);
        }
        targetImg.setBackgroundResource(android.R.color.holo_blue_bright);
        targetImg.setPadding(1,1,1,1);
    }
}