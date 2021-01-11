package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.my_future.Menu.MenuListFragment;
import com.example.my_future.Menu.NavItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class FirstScreen extends AppCompatActivity {

        Button reg, aut;

        private int[] mImage = new int[]{
                R.drawable.page1, R.drawable.page4, R.drawable.page3, R.drawable.page6, R.drawable.page2
        };

        private String[] mImagesTitle = new String[] {
                "Page", "Page", "Page", "Page", "Page"
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.start_screen);

            reg  = findViewById(R.id.buttonReg);
            aut = findViewById(R.id.buttonAut);

            CarouselView carouselView = findViewById(R.id.carousel);
            carouselView.setPageCount(mImage.length);
            carouselView.setImageListener(new ImageListener() {

                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(mImage[position]);
                }
            });
            carouselView.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    Toast.makeText(FirstScreen.this, mImagesTitle[position], Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void onClickButtonReg(View view) {
//        LinearLayout authorizationLayout = findViewById(R.id.AutoLayout);
//        LinearLayout registrationLayout = findViewById(R.id.RegLayout);
//        authorizationLayout.setVisibility(View.VISIBLE);
//        registrationLayout.setVisibility(View.GONE);
//        aut.setVisibility(View.VISIBLE);
//        reg.setVisibility(View.GONE);
        Intent intent = new Intent(FirstScreen.this, AuthorizationActivity.class);
        startActivity(intent);
    }
}
