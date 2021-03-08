package com.example.my_future.MenuBottom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_AVATAR;
import static com.example.my_future.Variables.APP_PREFERENCES_NICKNAME;
import static com.example.my_future.Variables.APP_PREFERENCES_TARGET;
import static com.example.my_future.Variables.TAG;

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
        avatar = v.findViewById(R.id.avatar);
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
                    new SaveAvatarTask().execute();
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

    class SaveAvatarTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    urlAvatar = String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.getMessage();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Picasso.with(getContext()).load(urlAvatar).error(R.drawable.default_avatar).into(avatar, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] byteArray = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_AVATAR, encodedImage);
                    editor.apply();
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
