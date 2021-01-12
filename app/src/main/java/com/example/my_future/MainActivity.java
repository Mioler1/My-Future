package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.my_future.Menu.MenuListFragment;
import com.example.my_future.Menu.NavItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavItemSelectedListener {

    ImageView avatar_img;

    FirebaseDatabase db;
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMenu();
        init();
        loadingAvatar();
    }


    private void init() {
        avatar_img = findViewById(R.id.avatar);

        db = FirebaseDatabase.getInstance();
        mRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            mMenuFragment.setNavItemSelectedListener(this);
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onNavItemSelectedListener(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.id_favorite:
                Toast.makeText(MainActivity.this, "fwfw", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_settings:
                Toast.makeText(MainActivity.this, "fwfw41", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void loadingAvatar() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()){
                    if (user.getKey().equals(mAuth.getUid())) {
                        for (DataSnapshot profile : user.getChildren()) {
                            if (profile.getKey().equals("profile")) {
                                for (DataSnapshot avatar : profile.getChildren()) {
                                    if (avatar.getKey().equals("avatar")) {
                                        Glide.with(avatar_img).load(avatar.getValue().toString()).error(R.drawable.up).into(avatar_img);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Не загрузилось");
            }
        });
    }

    public void onClickOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, FirstScreenActivity.class));
        finish();
    }

    private void MyToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}