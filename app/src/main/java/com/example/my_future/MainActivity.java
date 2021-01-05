package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.my_future.Menu.MenuListFragment;
import com.example.my_future.Menu.NavItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavItemSelectedListener {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMenu();
        init();
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

    private void init() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, AuthorizationActivity.class));
        finish();
    }
}