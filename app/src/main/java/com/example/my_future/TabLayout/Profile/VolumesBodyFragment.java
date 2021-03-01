package com.example.my_future.TabLayout.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_BICEPS;
import static com.example.my_future.Variables.APP_PREFERENCES_CHEST;
import static com.example.my_future.Variables.APP_PREFERENCES_FOREARM;
import static com.example.my_future.Variables.APP_PREFERENCES_GROWTH;
import static com.example.my_future.Variables.APP_PREFERENCES_HIP;
import static com.example.my_future.Variables.APP_PREFERENCES_NECK;
import static com.example.my_future.Variables.APP_PREFERENCES_SHIN;
import static com.example.my_future.Variables.APP_PREFERENCES_WAIST;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;

public class VolumesBodyFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    SharedPreferences mSettings;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_volumes_body, container, false);
        init();
        return v;
    }

    private void init() {
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        TextView weight = v.findViewById(R.id.weight);
        TextView growth = v.findViewById(R.id.growth);
        TextView neck = v.findViewById(R.id.neck);
        TextView biceps = v.findViewById(R.id.biceps);
        TextView forearm = v.findViewById(R.id.forearm);
        TextView chest = v.findViewById(R.id.chest);
        TextView waist = v.findViewById(R.id.waist);
        TextView hip = v.findViewById(R.id.hip);
        TextView shin = v.findViewById(R.id.shin);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_PREFERENCES_WEIGHT)) {
                    weight.setText(mSettings.getString(APP_PREFERENCES_WEIGHT, ""));
                } else {
                    weight.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("weight").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_GROWTH)) {
                    growth.setText(mSettings.getString(APP_PREFERENCES_GROWTH, ""));
                } else {
                    growth.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("growth").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_NECK)) {
                    neck.setText(mSettings.getString(APP_PREFERENCES_NECK, ""));
                } else {
                    neck.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_BICEPS)) {
                    biceps.setText(mSettings.getString(APP_PREFERENCES_BICEPS, ""));
                } else {
                    biceps.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_FOREARM)) {
                    forearm.setText(mSettings.getString(APP_PREFERENCES_FOREARM, ""));
                } else {
                    forearm.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_CHEST)) {
                    chest.setText(mSettings.getString(APP_PREFERENCES_CHEST, ""));
                } else {
                    chest.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_WAIST)) {
                    waist.setText(mSettings.getString(APP_PREFERENCES_WAIST, ""));
                } else {
                    waist.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_HIP)) {
                    hip.setText(mSettings.getString(APP_PREFERENCES_HIP, ""));
                } else {
                    hip.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_SHIN)) {
                    shin.setText(mSettings.getString(APP_PREFERENCES_SHIN, ""));
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
