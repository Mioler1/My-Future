package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SplashTheme);
        super.onCreate(savedInstanceState);
        startActivity(new Intent(StartScreenActivity.this, FirstScreenActivity.class));
        finish();
    }
}