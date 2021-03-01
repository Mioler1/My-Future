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
import static com.example.my_future.Variables.APP_PREFERENCES_ACTIVITY;
import static com.example.my_future.Variables.APP_PREFERENCES_DISEASES;
import static com.example.my_future.Variables.APP_PREFERENCES_EXPERIENCE;
import static com.example.my_future.Variables.APP_PREFERENCES_PRESSURE;

public class HealthFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    SharedPreferences mSettings;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_health, container, false);
        init();
        return v;
    }

    private void init() {
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        TextView activity = v.findViewById(R.id.activity);
        TextView diseases = v.findViewById(R.id.diseases);
        TextView experience = v.findViewById(R.id.experience);
        TextView pressure = v.findViewById(R.id.pressure);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_PREFERENCES_ACTIVITY)) {
                    activity.setText(mSettings.getString(APP_PREFERENCES_ACTIVITY, ""));
                } else {
                    activity.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("activity").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_DISEASES)) {
                    diseases.setText(mSettings.getString(APP_PREFERENCES_DISEASES, ""));
                } else {
                    diseases.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("diseases").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_EXPERIENCE)) {
                    experience.setText(mSettings.getString(APP_PREFERENCES_EXPERIENCE, ""));
                } else {
                    experience.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("experience").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_PRESSURE)) {
                    pressure.setText(mSettings.getString(APP_PREFERENCES_PRESSURE, ""));
                } else {
                    pressure.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("pressure").getValue()));
                }
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
