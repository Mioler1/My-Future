package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.my_future.MenuBottom.ProfileFragment;
import com.example.my_future.TabLayout.Profile.VolumesBodyFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.my_future.Variables.ALL_CHECK_DATA;
import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_BICEPS;
import static com.example.my_future.Variables.CHECK_DATA_VOLUME;
import static com.example.my_future.Variables.APP_DATA_USER_CHEST;
import static com.example.my_future.Variables.APP_DATA_USER_FOREARM;
import static com.example.my_future.Variables.APP_DATA_USER_HIP;
import static com.example.my_future.Variables.APP_DATA_USER_NECK;
import static com.example.my_future.Variables.APP_DATA_USER_SHIN;
import static com.example.my_future.Variables.APP_DATA_USER_WAIST;

public class FillingDataVolumesActivity extends AppCompatActivity {
    EditText waist, neck, chest, biceps, forearm, hip, shin;
    ProgressBar progressBarDataVolume;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    SharedPreferences mSettings, checkData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data_volumes);

        init();
        downloadData();
    }

    private void init() {
        mSettings = this.getSharedPreferences(ALL_DATA_USER, MODE_PRIVATE);
        checkData = this.getSharedPreferences(ALL_CHECK_DATA, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        waist = findViewById(R.id.waist);
        neck = findViewById(R.id.neck);
        chest = findViewById(R.id.chest);
        biceps = findViewById(R.id.biceps);
        forearm = findViewById(R.id.forearm);
        hip = findViewById(R.id.hip);
        shin = findViewById(R.id.shin);
        progressBarDataVolume = findViewById(R.id.progressBarVolume);
    }

    private void downloadData() {
        progressBarDataVolume.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                String neck_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue());
                String biceps_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue());
                String forearm_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue());
                String chest_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue());
                String waist_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue());
                String hip_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue());
                String shin_text = String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("shin").getValue());
                if (mSettings.contains(APP_DATA_USER_NECK)) {
                    if (!mSettings.getString(APP_DATA_USER_NECK, "").equals("—")) {
                        neck.setText(mSettings.getString(APP_DATA_USER_NECK, ""));
                    }
                } else {
                    if (!neck_text.equals("—") && !neck_text.equals("null")) {
                        neck.setText(neck_text);
                    }
                }
                if (mSettings.contains(APP_DATA_USER_BICEPS)) {
                    if (!mSettings.getString(APP_DATA_USER_BICEPS, "").equals("—")) {
                        biceps.setText(mSettings.getString(APP_DATA_USER_BICEPS, ""));
                    }
                } else {
                    if (!biceps_text.equals("—") && !biceps_text.equals("null")) {
                        biceps.setText(biceps_text);
                    }
                }
                if (mSettings.contains(APP_DATA_USER_FOREARM)) {
                    if (!mSettings.getString(APP_DATA_USER_FOREARM, "").equals("—")) {
                        forearm.setText(mSettings.getString(APP_DATA_USER_FOREARM, ""));
                    }
                } else {
                    if (!forearm_text.equals("—") && !forearm_text.equals("null")) {
                        forearm.setText(forearm_text);
                    }
                }
                if (mSettings.contains(APP_DATA_USER_CHEST)) {
                    if (!mSettings.getString(APP_DATA_USER_CHEST, "").equals("—")) {
                        chest.setText(mSettings.getString(APP_DATA_USER_CHEST, ""));
                    }
                } else {
                    if (!chest_text.equals("—") && !chest_text.equals("null")) {
                        chest.setText(chest_text);
                    }
                }
                if (mSettings.contains(APP_DATA_USER_WAIST)) {
                    if (!mSettings.getString(APP_DATA_USER_WAIST, "").equals("—")) {
                        waist.setText(mSettings.getString(APP_DATA_USER_WAIST, ""));
                    }
                } else {
                    if (!waist_text.equals("—") && !waist_text.equals("null")) {
                        waist.setText(waist_text);
                    }
                }
                if (mSettings.contains(APP_DATA_USER_HIP)) {
                    if (!mSettings.getString(APP_DATA_USER_HIP, "").equals("—")) {
                        hip.setText(mSettings.getString(APP_DATA_USER_HIP, ""));
                    }
                } else {
                    if (!hip_text.equals("—") && !hip_text.equals("null")) {
                        hip.setText(hip_text);
                    }
                }
                if (mSettings.contains(APP_DATA_USER_SHIN)) {
                    if (!mSettings.getString(APP_DATA_USER_SHIN, "").equals("—")) {
                        shin.setText(mSettings.getString(APP_DATA_USER_SHIN, ""));
                    }
                } else {
                    if (!shin_text.equals("—") && !shin_text.equals("null")) {
                        shin.setText(shin_text);
                    }
                }
                editor.apply();
                progressBarDataVolume.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBarDataVolume.setVisibility(View.GONE);
            }
        });
    }

    public void onClickSaveDataVolume(View view) {
        String waist_text = waist.getText().toString();
        String neck_text = neck.getText().toString();
        String chest_text = chest.getText().toString();
        String biceps_text = biceps.getText().toString();
        String forearm_text = forearm.getText().toString();
        String hip_text = hip.getText().toString();
        String shin_text = shin.getText().toString();
        SharedPreferences.Editor editor = mSettings.edit();
        progressBarDataVolume.setVisibility(View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!waist_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("waist").setValue(waist_text);
                    editor.putString(APP_DATA_USER_WAIST, waist_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("waist").setValue("—");
                    editor.putString(APP_DATA_USER_WAIST, "—");
                }
                if (!neck_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("neck").setValue(neck_text);
                    editor.putString(APP_DATA_USER_NECK, neck_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("neck").setValue("—");
                    editor.putString(APP_DATA_USER_NECK, "—");
                }
                if (!chest_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("chest").setValue(chest_text);
                    editor.putString(APP_DATA_USER_CHEST, chest_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("chest").setValue("—");
                    editor.putString(APP_DATA_USER_CHEST, "—");
                }
                if (!biceps_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("biceps").setValue(biceps_text);
                    editor.putString(APP_DATA_USER_BICEPS, biceps_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("biceps").setValue("—");
                    editor.putString(APP_DATA_USER_BICEPS, "—");
                }
                if (!forearm_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("forearm").setValue(forearm_text);
                    editor.putString(APP_DATA_USER_FOREARM, forearm_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("forearm").setValue("—");
                    editor.putString(APP_DATA_USER_FOREARM, "—");
                }
                if (!hip_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("hip").setValue(hip_text);
                    editor.putString(APP_DATA_USER_HIP, hip_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("hip").setValue("—");
                    editor.putString(APP_DATA_USER_HIP, "—");
                }
                if (!shin_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("shin").setValue(shin_text);
                    editor.putString(APP_DATA_USER_SHIN, shin_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("shin").setValue("—");
                    editor.putString(APP_DATA_USER_SHIN, "—");
                }
                if (!waist_text.isEmpty() && !neck_text.isEmpty() && !chest_text.isEmpty() && !biceps_text.isEmpty() && !forearm_text.isEmpty() && !hip_text.isEmpty() && !shin_text.isEmpty()) {
                    SharedPreferences.Editor editorCheck = checkData.edit();
                    editorCheck.putString(CHECK_DATA_VOLUME, "true");
                    editorCheck.apply();
                    Intent intent = new Intent();
                    intent.putExtra("check", "true");
                    setResult(RESULT_OK, intent);
                }
                editor.apply();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBarDataVolume.setVisibility(View.GONE);
                MyToast("Данные не добавились");
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataVolumesActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}