package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.synnapps.carouselview.CarouselView;

public class FirstScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.button_for_activity);
        Button btnAlpha = findViewById(R.id.buttonReg);
        btnAlpha.setOnClickListener(view -> {
            view.startAnimation(animAlpha);
            startActivity(new Intent(FirstScreenActivity.this, RegistrationActivity.class));
        });

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        CarouselView carouselView = findViewById(R.id.carousel);
        carouselView.setPageCount(mImage.length);
        carouselView.setImageListener((position, imageView) ->
                imageView.setImageResource(mImage[position]));
    }

    public void onClickAuthorizationActivity(View view) {
        startActivity(new Intent(FirstScreenActivity.this, AuthorizationActivity.class));
    }

    private final int[] mImage = new int[]{
            R.drawable.page1,
            R.drawable.page4,
            R.drawable.page3,
            R.drawable.page5,
            R.drawable.page2
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            comeEmailVer();
        }
    }

    public void comeEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            startActivity(new Intent(FirstScreenActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(FirstScreenActivity.this, AuthorizationActivity.class));
        }
        finish();
    }
}
