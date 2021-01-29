package com.example.my_future.MenuBottom;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    public static final String APP_PREFERENCES = "saveDataUser";
    public static final String APP_PREFERENCES_NAME = "Nickname";
    SharedPreferences mSettings;

    View v;
    TextView nickname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_activity_profile, container, false);

        init();
        return v;
    }

    private void init() {
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Button button = v.findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeDataFragment()).commit();
            }
        });

        CircleImageView avatar = v.findViewById(R.id.avatar);
        nickname = v.findViewById(R.id.nickname);
        TextView target = v.findViewById(R.id.target);
        TextView weight = v.findViewById(R.id.weight);
        if (mSettings.contains(APP_PREFERENCES_NAME)) {
            nickname.setText(mSettings.getString(APP_PREFERENCES_NAME, ""));
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(avatar).load(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue()).error(R.drawable.default_avatar).into(avatar);
                nickname.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("nickname").getValue()));
                target.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("target").getValue()));
                weight.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("weight").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String string = nickname.getText().toString();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, string);
        editor.apply();
    }
}
