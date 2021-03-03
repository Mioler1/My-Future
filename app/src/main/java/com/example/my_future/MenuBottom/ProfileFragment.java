package com.example.my_future.MenuBottom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.my_future.R;
import com.example.my_future.TabLayout.DepthPageTransformer;
import com.example.my_future.TabLayout.Profile.GraphFragment;
import com.example.my_future.TabLayout.Profile.HealthFragment;
import com.example.my_future.TabLayout.Profile.PageAdapter;
import com.example.my_future.TabLayout.Profile.SportResultFragment;
import com.example.my_future.TabLayout.Profile.VolumesBodyFragment;
import com.google.android.material.tabs.TabLayout;
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

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;
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
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        viewPager = v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.tabLayout);

        pageAdapter = new PageAdapter(getFragmentManager());
        pageAdapter.addFragment(new VolumesBodyFragment(), "Объёмы");
        pageAdapter.addFragment(new SportResultFragment(), "Спорт");
        pageAdapter.addFragment(new HealthFragment(), "Здоровья");
        pageAdapter.addFragment(new GraphFragment(), "Графики");
        viewPager.setAdapter(pageAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setSaveFromParentEnabled(false);
        tabLayout.setupWithViewPager(viewPager);

        downloadData();
    }

    private void downloadData() {
        CircleImageView avatar = v.findViewById(R.id.avatar);
        TextView nickname = v.findViewById(R.id.nickname);
        TextView target = v.findViewById(R.id.target);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_PREFERENCES_AVATAR)) {
                    String mImageUri = mSettings.getString(APP_PREFERENCES_AVATAR, "");
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
                if (mSettings.contains(APP_PREFERENCES_TARGET)) {
                    target.setText(mSettings.getString(APP_PREFERENCES_TARGET, ""));
                } else {
                    target.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("target").getValue()));
                }
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
