package com.example.my_future;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.my_future.Fragments.ChangeDataFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_ACTIVITY;
import static com.example.my_future.Variables.APP_PREFERENCES_AVATAR;
import static com.example.my_future.Variables.APP_PREFERENCES_BICEPS;
import static com.example.my_future.Variables.APP_PREFERENCES_CHEST;
import static com.example.my_future.Variables.APP_PREFERENCES_DISEASES;
import static com.example.my_future.Variables.APP_PREFERENCES_EXPERIENCE;
import static com.example.my_future.Variables.APP_PREFERENCES_FOREARM;
import static com.example.my_future.Variables.APP_PREFERENCES_GENDER;
import static com.example.my_future.Variables.APP_PREFERENCES_GROWTH;
import static com.example.my_future.Variables.APP_PREFERENCES_HIP;
import static com.example.my_future.Variables.APP_PREFERENCES_NECK;
import static com.example.my_future.Variables.APP_PREFERENCES_NICKNAME;
import static com.example.my_future.Variables.APP_PREFERENCES_PRESSURE;
import static com.example.my_future.Variables.APP_PREFERENCES_SHIN;
import static com.example.my_future.Variables.APP_PREFERENCES_TARGET;
import static com.example.my_future.Variables.APP_PREFERENCES_WAIST;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;
import static com.example.my_future.Variables.fragmentsInStack;

public class TimeClass extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    SharedPreferences mSettings;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bottom_fragment_profile, container, false);
        init();
        return v;
    }

    private void init() {
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//        Button button = v.findViewById(R.id.click);
//        CircleImageView avatar = v.findViewById(R.id.avatar);
//        TextView nickname = v.findViewById(R.id.nickname);
//        TextView target = v.findViewById(R.id.target);
        TextView weight = v.findViewById(R.id.weight);
        TextView growth = v.findViewById(R.id.growth);
        TextView gender = v.findViewById(R.id.gender);
//        TextView activity = v.findViewById(R.id.activity);
//        TextView diseases = v.findViewById(R.id.diseases);
//        TextView experience = v.findViewById(R.id.experience);
//        TextView pressure = v.findViewById(R.id.pressure);
//        TextView neck = v.findViewById(R.id.neck);
//        TextView biceps = v.findViewById(R.id.biceps);
//        TextView forearm = v.findViewById(R.id.forearm);
//        TextView chest = v.findViewById(R.id.chest);
//        TextView waist = v.findViewById(R.id.waist);
//        TextView hip = v.findViewById(R.id.hip);
//        TextView shin = v.findViewById(R.id.shin);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
//                if (mSettings.contains(APP_PREFERENCES_AVATAR)) {
//                    String mImageUri = mSettings.getString(APP_PREFERENCES_AVATAR, "");
//                    byte[] decode = Base64.decode(mImageUri, Base64.DEFAULT);
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
//                    avatar.setImageBitmap(bitmap);
//                } else {
//                    Glide.with(avatar).load(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue()).error(R.drawable.default_avatar).into(avatar);
//                }
//                if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
//                    nickname.setText(mSettings.getString(APP_PREFERENCES_NICKNAME, ""));
//                } else {
//                    nickname.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("nickname").getValue()));
//                }
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
                if (mSettings.contains(APP_PREFERENCES_GENDER)) {
                    gender.setText(mSettings.getString(APP_PREFERENCES_GENDER, ""));
                } else {
                    gender.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("gender").getValue()));
                }
//                if (mSettings.contains(APP_PREFERENCES_TARGET)) {
//                    target.setText(mSettings.getString(APP_PREFERENCES_TARGET, ""));
//                } else {
//                    target.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("target").getValue()));
//                }

//                if (mSettings.contains(APP_PREFERENCES_ACTIVITY)) {
//                    activity.setText(mSettings.getString(APP_PREFERENCES_ACTIVITY, ""));
//                } else {
//                    activity.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("activity").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_DISEASES)) {
//                    diseases.setText(mSettings.getString(APP_PREFERENCES_DISEASES, ""));
//                } else {
//                    diseases.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("diseases").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_EXPERIENCE)) {
//                    experience.setText(mSettings.getString(APP_PREFERENCES_EXPERIENCE, ""));
//                } else {
//                    experience.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("experience").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_PRESSURE)) {
//                    pressure.setText(mSettings.getString(APP_PREFERENCES_PRESSURE, ""));
//                } else {
//                    pressure.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("health").child("pressure").getValue()));
//                }

//                if (mSettings.contains(APP_PREFERENCES_NECK)) {
//                    neck.setText(mSettings.getString(APP_PREFERENCES_NECK, ""));
//                } else {
//                    neck.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_BICEPS)) {
//                    biceps.setText(mSettings.getString(APP_PREFERENCES_BICEPS, ""));
//                } else {
//                    biceps.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_FOREARM)) {
//                    forearm.setText(mSettings.getString(APP_PREFERENCES_FOREARM, ""));
//                } else {
//                    forearm.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_CHEST)) {
//                    chest.setText(mSettings.getString(APP_PREFERENCES_CHEST, ""));
//                } else {
//                    chest.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_WAIST)) {
//                    waist.setText(mSettings.getString(APP_PREFERENCES_WAIST, ""));
//                } else {
//                    waist.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_HIP)) {
//                    hip.setText(mSettings.getString(APP_PREFERENCES_HIP, ""));
//                } else {
//                    hip.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue()));
//                }
//                if (mSettings.contains(APP_PREFERENCES_SHIN)) {
//                    shin.setText(mSettings.getString(APP_PREFERENCES_SHIN, ""));
//                } else {
//                    shin.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("shin").getValue()));
//                }

                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        button.setOnClickListener(view -> {
//            fragmentsInStack.add(new ChangeDataFragment());
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeDataFragment()).commit();
//        });
    }
}
