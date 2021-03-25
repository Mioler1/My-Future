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

import com.example.my_future.FillingDataActivismActivity;
import com.example.my_future.FillingDataHealthActivity;
import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.my_future.Variables.ALL_CHECK_DATA;
import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_ACTIVITY;
import static com.example.my_future.Variables.APP_DATA_USER_DISEASES;
import static com.example.my_future.Variables.APP_DATA_USER_EXPERIENCE;
import static com.example.my_future.Variables.APP_DATA_USER_PRESSURE;
import static com.example.my_future.Variables.CHECK_DATA_ACTIVISM;
import static com.example.my_future.Variables.CHECK_DATA_HEALTH;

public class HealthFragment extends Fragment {
    TextView activism, diseases, experience, pressure;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    SharedPreferences mSettings, checkDataSettings;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_health, container, false);
        init();
        checkData();
        return v;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mSettings = getContext().getSharedPreferences(ALL_DATA_USER, Context.MODE_PRIVATE);
        checkDataSettings = getContext().getSharedPreferences(ALL_CHECK_DATA, Context.MODE_PRIVATE);

        activism = v.findViewById(R.id.activity);
        diseases = v.findViewById(R.id.diseases);
        experience = v.findViewById(R.id.experience);
        pressure = v.findViewById(R.id.pressure);

        v.findViewById(R.id.but_plus).setOnClickListener(view -> {
            if (!String.valueOf(checkDataSettings.contains(CHECK_DATA_HEALTH)).equals("true")) {
                startActivityForResult(new Intent(getContext(), FillingDataHealthActivity.class), 1);
            } else {
                startActivityForResult(new Intent(getContext(), FillingDataActivismActivity.class), 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (String.valueOf(checkDataSettings.contains(CHECK_DATA_ACTIVISM)).equals("true")) {
            String check = data.getStringExtra("check");
            if (check.equals("true")) {
                RelativeLayout relativeReadData = v.findViewById(R.id.RelRead);
                RelativeLayout relativePlus = v.findViewById(R.id.Rel_plus);
                relativeReadData.setVisibility(View.VISIBLE);
                relativePlus.setVisibility(View.GONE);
                downloadData();
            }
        }
    }

    private void checkData() {
        if (String.valueOf(checkDataSettings.contains(CHECK_DATA_HEALTH)).equals("true") &&
                String.valueOf(checkDataSettings.contains(CHECK_DATA_ACTIVISM)).equals("true")) {
            RelativeLayout relativeReadData = v.findViewById(R.id.RelRead);
            RelativeLayout relativePlus = v.findViewById(R.id.Rel_plus);
            relativeReadData.setVisibility(View.VISIBLE);
            relativePlus.setVisibility(View.GONE);
            downloadData();
        }
    }

    private void downloadData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_DATA_USER_ACTIVITY)) {
                    activism.setText(mSettings.getString(APP_DATA_USER_ACTIVITY, ""));
                } else {
                    activism.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("activity").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_DISEASES)) {
                    diseases.setText(mSettings.getString(APP_DATA_USER_DISEASES, ""));
                } else {
                    diseases.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("diseases").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_EXPERIENCE)) {
                    experience.setText(mSettings.getString(APP_DATA_USER_EXPERIENCE, ""));
                } else {
                    experience.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("experience").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_PRESSURE)) {
                    pressure.setText(mSettings.getString(APP_DATA_USER_PRESSURE, ""));
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
