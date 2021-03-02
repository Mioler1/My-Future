package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_BICEPS;
import static com.example.my_future.Variables.APP_PREFERENCES_BOOLEAN_VOLUME;
import static com.example.my_future.Variables.APP_PREFERENCES_CHEST;
import static com.example.my_future.Variables.APP_PREFERENCES_FOREARM;
import static com.example.my_future.Variables.APP_PREFERENCES_GROWTH;
import static com.example.my_future.Variables.APP_PREFERENCES_HIP;
import static com.example.my_future.Variables.APP_PREFERENCES_NECK;
import static com.example.my_future.Variables.APP_PREFERENCES_SHIN;
import static com.example.my_future.Variables.APP_PREFERENCES_WAIST;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;

public class FillingDataVolumesActivity extends AppCompatActivity {
    EditText waist, neck, chest, biceps, forearm, hip, shin;
    ProgressBar progressBarDataVolume;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data_volumes);

        init();
        downloadData();
    }

    private void init() {
        mSettings = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_PREFERENCES_NECK)) {
                    if (!mSettings.getString(APP_PREFERENCES_NECK, "").equals("—")) {
                        neck.setText(mSettings.getString(APP_PREFERENCES_NECK, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue()).equals("—")) {
                        neck.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue()));
                    }
                }
                if (mSettings.contains(APP_PREFERENCES_BICEPS)) {
                    if (!mSettings.getString(APP_PREFERENCES_BICEPS, "").equals("—")) {
                        biceps.setText(mSettings.getString(APP_PREFERENCES_BICEPS, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue()).equals("—")) {
                        biceps.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue()));
                    }
                }
                if (mSettings.contains(APP_PREFERENCES_FOREARM)) {
                    if (!mSettings.getString(APP_PREFERENCES_FOREARM, "").equals("—")) {
                        forearm.setText(mSettings.getString(APP_PREFERENCES_FOREARM, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue()).equals("—")) {
                        forearm.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue()));
                    }
                }
                if (mSettings.contains(APP_PREFERENCES_CHEST)) {
                    if (!mSettings.getString(APP_PREFERENCES_CHEST, "").equals("—")) {
                        chest.setText(mSettings.getString(APP_PREFERENCES_CHEST, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue()).equals("—")) {
                        chest.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue()));
                    }
                }
                if (mSettings.contains(APP_PREFERENCES_WAIST)) {
                    if (!mSettings.getString(APP_PREFERENCES_WAIST, "").equals("—")) {
                        waist.setText(mSettings.getString(APP_PREFERENCES_WAIST, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue()).equals("—")) {
                        waist.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue()));
                    }
                }
                if (mSettings.contains(APP_PREFERENCES_HIP)) {
                    if (!mSettings.getString(APP_PREFERENCES_HIP, "").equals("—")) {
                        hip.setText(mSettings.getString(APP_PREFERENCES_HIP, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue()).equals("—")) {
                        hip.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue()));
                    }
                }
                if (mSettings.contains(APP_PREFERENCES_SHIN)) {
                    if (!mSettings.getString(APP_PREFERENCES_SHIN, "").equals("—")) {
                        shin.setText(mSettings.getString(APP_PREFERENCES_SHIN, ""));
                    }
                } else {
                    if (!String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("shin").getValue()).equals("—")) {
                        shin.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("shin").getValue()));
                    }
                }
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        if (!waist_text.isEmpty() && !neck_text.isEmpty() && !chest_text.isEmpty() && !biceps_text.isEmpty() && !forearm_text.isEmpty() && !hip_text.isEmpty() && !shin_text.isEmpty()) {
            progressBarDataVolume.setVisibility(View.VISIBLE);
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!waist_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("waist").setValue(waist_text);
                    editor.putString(APP_PREFERENCES_WAIST, waist_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("waist").setValue("—");
                    editor.putString(APP_PREFERENCES_WAIST, "—");
                }
                if (!neck_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("neck").setValue(neck_text);
                    editor.putString(APP_PREFERENCES_NECK, neck_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("neck").setValue("—");
                    editor.putString(APP_PREFERENCES_NECK, "—");
                }
                if (!chest_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("chest").setValue(chest_text);
                    editor.putString(APP_PREFERENCES_CHEST, chest_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("chest").setValue("—");
                    editor.putString(APP_PREFERENCES_CHEST, "—");
                }
                if (!biceps_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("biceps").setValue(biceps_text);
                    editor.putString(APP_PREFERENCES_BICEPS, biceps_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("biceps").setValue("—");
                    editor.putString(APP_PREFERENCES_BICEPS, "—");
                }
                if (!forearm_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("forearm").setValue(forearm_text);
                    editor.putString(APP_PREFERENCES_FOREARM, forearm_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("forearm").setValue("—");
                    editor.putString(APP_PREFERENCES_FOREARM, "—");
                }
                if (!hip_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("hip").setValue(hip_text);
                    editor.putString(APP_PREFERENCES_HIP, hip_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("hip").setValue("—");
                    editor.putString(APP_PREFERENCES_HIP, "—");
                }
                if (!shin_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("shin").setValue(shin_text);
                    editor.putString(APP_PREFERENCES_SHIN, shin_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("shin").setValue("—");
                    editor.putString(APP_PREFERENCES_SHIN, "—");
                }
                if (!waist_text.isEmpty() && !neck_text.isEmpty() && !chest_text.isEmpty() && !biceps_text.isEmpty() && !forearm_text.isEmpty() && !hip_text.isEmpty() && !shin_text.isEmpty()) {
                    editor.putString(APP_PREFERENCES_BOOLEAN_VOLUME, "true");
                }
                editor.apply();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Данные не добавились");
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataVolumesActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}