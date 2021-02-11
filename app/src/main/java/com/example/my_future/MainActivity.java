package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.my_future.Interface.BackPressed;
import com.example.my_future.MenuFlowing.CalculatedFragment;
import com.example.my_future.MenuFlowing.MenuListFragment;
import com.example.my_future.MenuFlowing.NavItemSelectedListener;
import com.example.my_future.MenuBottom.FoodFragment;
import com.example.my_future.MenuBottom.ForumFragment;
import com.example.my_future.MenuBottom.NotebookFragment;
import com.example.my_future.MenuBottom.PlanFragment;
import com.example.my_future.MenuBottom.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_BOOLEAN_ACTIVITY;
import static com.example.my_future.Variables.APP_PREFERENCES_BOOLEAN_HEALTH;
import static com.example.my_future.Variables.APP_PREFERENCES_BOOLEAN_PROFILE;

public class MainActivity extends AppCompatActivity implements NavItemSelectedListener {

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    BottomNavigationView bottomNav;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setupMenu();
    }

    private void init() {
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlanFragment()).commit();

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        mSettings = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        checkProfile();
    }

    private void checkProfile() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (!String.valueOf(mSettings.contains(APP_PREFERENCES_BOOLEAN_PROFILE)).equals("true")) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(mAuth.getUid())) {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                if (s.getKey().equals("profile")) {
                                    if (s.getValue().equals("none")) {
                                        startActivity(new Intent(MainActivity.this, FillingDataUserActivity.class));
                                        finish();
                                    } else {
                                        editor.putString(APP_PREFERENCES_BOOLEAN_PROFILE, "true");
                                        editor.apply();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (String.valueOf(mSettings.contains(APP_PREFERENCES_BOOLEAN_PROFILE)).equals("true")) {
                    if (!String.valueOf(mSettings.contains(APP_PREFERENCES_BOOLEAN_HEALTH)).equals("true")) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(mAuth.getUid())) {
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    if (s.getKey().equals("health")) {
                                        if (s.getValue().equals("none")) {
                                            startActivity(new Intent(MainActivity.this, FillingDataUserHealthActivity.class));
                                            finish();
                                        } else {
                                            editor.putString(APP_PREFERENCES_BOOLEAN_HEALTH, "true");
                                            editor.apply();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (String.valueOf(mSettings.contains(APP_PREFERENCES_BOOLEAN_HEALTH)).equals("true")) {
                    if (!String.valueOf(mSettings.contains(APP_PREFERENCES_BOOLEAN_ACTIVITY)).equals("true")) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(mAuth.getUid())) {
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    if (s.getKey().equals("health")) {
                                        for (DataSnapshot health : s.getChildren()) {
                                            if (health.getKey().equals("activity")) {
                                                if (health.getValue().equals("none")) {
                                                    startActivity(new Intent(MainActivity.this, FillingDataUserHealthActivity.class).putExtra("openActivity", "false"));
                                                    finish();
                                                } else {
                                                    editor.putString(APP_PREFERENCES_BOOLEAN_ACTIVITY, "true");
                                                    editor.apply();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyToast(databaseError.getMessage());
            }
        });
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
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.id_calculate:
                selectedFragment = new CalculatedFragment();
                break;
            case R.id.id_out:
                mAuth.signOut();
                selectedFragment = new Fragment();
                startActivity(new Intent(MainActivity.this, FirstScreenActivity.class));
                finish();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() <= 0) {
            finish();
        } else {
            if (!(fragment instanceof BackPressed) || !((BackPressed) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.fragment1:
                        selectedFragment = new PlanFragment();
                        break;
                    case R.id.fragment2:
                        selectedFragment = new FoodFragment();
                        break;
                    case R.id.fragment3:
                        selectedFragment = new ForumFragment();
                        break;
                    case R.id.fragment4:
                        selectedFragment = new NotebookFragment();
                        break;
                    case R.id.fragment5:
                        selectedFragment = new ProfileFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack("back").commit();
                return true;
            };

    public void OnClickExit(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, FirstScreenActivity.class));
        finish();
    }

    private void MyToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}