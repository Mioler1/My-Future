package com.example.my_future.MenuBottom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_AVATAR;
import static com.example.my_future.Variables.APP_PREFERENCES_NICKNAME;
import static com.example.my_future.Variables.APP_PREFERENCES_TARGET;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    SharedPreferences mSettings;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_activity_profile, container, false);
        init();
        return v;
    }

    private void init() {
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        CircleImageView avatar = v.findViewById(R.id.avatar);
        TextView nickname = v.findViewById(R.id.nickname);
        TextView target = v.findViewById(R.id.target);
        TextView weight = v.findViewById(R.id.weight);
        Button button = v.findViewById(R.id.click);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mSettings.contains(APP_PREFERENCES_AVATAR)) {
                    String mImageUri = mSettings.getString("Avatar", "");
                    byte[] decode = Base64.decode(mImageUri, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    avatar.setImageBitmap(bitmap);
                } else {
                    Glide.with(avatar).load(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue()).error(R.drawable.default_avatar).into(avatar);
                }

                if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
                    nickname.setText(mSettings.getString(APP_PREFERENCES_NICKNAME, ""));
                } else {
                    nickname.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("nickname").getValue()));
                }

                if (mSettings.contains(APP_PREFERENCES_WEIGHT)) {
                    weight.setText(mSettings.getString(APP_PREFERENCES_WEIGHT, ""));
                } else {
                    weight.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("weight").getValue()));
                }

                if (mSettings.contains(APP_PREFERENCES_TARGET)) {
                    target.setText(mSettings.getString(APP_PREFERENCES_TARGET, ""));
                } else {
                    target.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("target").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeDataFragment()).commit();
            }
        });
    }
}
