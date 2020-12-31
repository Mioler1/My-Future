package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
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