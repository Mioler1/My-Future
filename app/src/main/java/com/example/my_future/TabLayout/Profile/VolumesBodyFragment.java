package com.example.my_future.TabLayout.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.FillingDataVolumesActivity;
import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.my_future.Variables.ALL_CHECK_DATA;
import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_BICEPS;
import static com.example.my_future.Variables.APP_DATA_USER_CHEST;
import static com.example.my_future.Variables.APP_DATA_USER_FOREARM;
import static com.example.my_future.Variables.APP_DATA_USER_GROWTH;
import static com.example.my_future.Variables.APP_DATA_USER_HIP;
import static com.example.my_future.Variables.APP_DATA_USER_NECK;
import static com.example.my_future.Variables.APP_DATA_USER_SHIN;
import static com.example.my_future.Variables.APP_DATA_USER_WAIST;
import static com.example.my_future.Variables.APP_DATA_USER_WEIGHT;
import static com.example.my_future.Variables.CHECK_DATA_VOLUME;

public class VolumesBodyFragment extends Fragment {
    TextView weight, growth, neck, biceps, forearm, chest, waist, hip, shin;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    SharedPreferences mSettings, checkDataSettings;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_volumes_body, container, false);
        init();
        if (String.valueOf(checkDataSettings.contains(CHECK_DATA_VOLUME)).equals("true")) {
            RelativeLayout relativeReadData = v.findViewById(R.id.RelRead);
            RelativeLayout relativePlus = v.findViewById(R.id.Rel_plus);
            relativeReadData.setVisibility(View.VISIBLE);
            relativePlus.setVisibility(View.GONE);
            downloadData();
        }
        return v;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mSettings = getContext().getSharedPreferences(ALL_DATA_USER, Context.MODE_PRIVATE);
        checkDataSettings = getContext().getSharedPreferences(ALL_CHECK_DATA, Context.MODE_PRIVATE);

        weight = v.findViewById(R.id.weight);
        growth = v.findViewById(R.id.growth);
        neck = v.findViewById(R.id.neck);
        biceps = v.findViewById(R.id.biceps);
        forearm = v.findViewById(R.id.forearm);
        chest = v.findViewById(R.id.chest);
        waist = v.findViewById(R.id.waist);
        hip = v.findViewById(R.id.hip);
        shin = v.findViewById(R.id.shin);

        v.findViewById(R.id.but_plus).setOnClickListener(view ->
            startActivity(new Intent(getContext(), FillingDataVolumesActivity.class)));
    }

    private void downloadData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_DATA_USER_WEIGHT)) {
                    weight.setText(mSettings.getString(APP_DATA_USER_WEIGHT, ""));
                } else {
                    weight.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("weight").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_GROWTH)) {
                    growth.setText(mSettings.getString(APP_DATA_USER_GROWTH, ""));
                } else {
                    growth.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("growth").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_NECK)) {
                    neck.setText(mSettings.getString(APP_DATA_USER_NECK, ""));
                } else {
                    neck.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_BICEPS)) {
                    biceps.setText(mSettings.getString(APP_DATA_USER_BICEPS, ""));
                } else {
                    biceps.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_FOREARM)) {
                    forearm.setText(mSettings.getString(APP_DATA_USER_FOREARM, ""));
                } else {
                    forearm.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_CHEST)) {
                    chest.setText(mSettings.getString(APP_DATA_USER_CHEST, ""));
                } else {
                    chest.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_WAIST)) {
                    waist.setText(mSettings.getString(APP_DATA_USER_WAIST, ""));
                } else {
                    waist.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_HIP)) {
                    hip.setText(mSettings.getString(APP_DATA_USER_HIP, ""));
                } else {
                    hip.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_SHIN)) {
                    shin.setText(mSettings.getString(APP_DATA_USER_SHIN, ""));
                } else {
                    shin.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("shin").getValue()));
                }
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
