package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AuthorizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    public void Authorization(View view) {
        LinearLayout authorizationLayout = findViewById(R.id.AutoLayout);
        LinearLayout registrationLayout = findViewById(R.id.RegLayout);
        authorizationLayout.setVisibility(View.VISIBLE);
        registrationLayout.setVisibility(View.GONE);
    }

    public void Registration(View view) {
        LinearLayout authorizationLayout = findViewById(R.id.AutoLayout);
        LinearLayout registrationLayout = findViewById(R.id.RegLayout);
        authorizationLayout.setVisibility(View.GONE);
        registrationLayout.setVisibility(View.VISIBLE);
    }
}