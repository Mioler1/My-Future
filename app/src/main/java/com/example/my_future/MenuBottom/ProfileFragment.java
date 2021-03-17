package com.example.my_future.MenuBottom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.my_future.Fragments.ChangeDataFragment;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_AVATAR;
import static com.example.my_future.Variables.APP_DATA_USER_NICKNAME;
import static com.example.my_future.Variables.APP_DATA_USER_TARGET;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ViewPager viewPager;
    TabLayout tabLayout;
    CircleImageView avatar;
    PageAdapter pageAdapter;
    SharedPreferences mSettings;
    View v;
    String urlAvatar = "";

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
        mSettings = getContext().getSharedPreferences(ALL_DATA_USER, Context.MODE_PRIVATE);

        viewPager = v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.tabLayout);

        pageAdapter = new PageAdapter(getFragmentManager());
        pageAdapter.addFragment(new VolumesBodyFragment());
        pageAdapter.addFragment(new SportResultFragment());
        pageAdapter.addFragment(new HealthFragment());
        pageAdapter.addFragment(new GraphFragment());

        viewPager.setAdapter(pageAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setSaveFromParentEnabled(false);
        tabLayout.setupWithViewPager(viewPager);

        int[] iconResId = {R.drawable.ic_person,
                R.drawable.ic_fitness_black,
                R.drawable.ic_health,
                R.drawable.ic_graph};
        for (int i = 0; i < iconResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(iconResId[i]);
        }
        downloadData();

        v.findViewById(R.id.ic_changeData).setOnClickListener(view ->
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeDataFragment()).commit());
    }

    private void downloadData() {
        avatar = v.findViewById(R.id.avatar);
        TextView nickname = v.findViewById(R.id.nickname);
        TextView target = v.findViewById(R.id.target);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_DATA_USER_AVATAR)) {
                    String mImageUri = mSettings.getString(APP_DATA_USER_AVATAR, "");
                    byte[] decode = Base64.decode(mImageUri, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    avatar.setImageBitmap(bitmap);
                } else {
                    urlAvatar = String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue());
                    Picasso.with(getContext()).load(urlAvatar).error(R.drawable.default_avatar).into(avatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] byteArray = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            editor.putString(APP_DATA_USER_AVATAR, encodedImage);
                            editor.apply();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
                if (mSettings.contains(APP_DATA_USER_NICKNAME)) {
                    nickname.setText(mSettings.getString(APP_DATA_USER_NICKNAME, ""));
                } else {
                    nickname.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("nickname").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_TARGET)) {
                    target.setText(mSettings.getString(APP_DATA_USER_TARGET, ""));
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
