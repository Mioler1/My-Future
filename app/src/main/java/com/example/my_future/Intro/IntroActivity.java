package com.example.my_future.Intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.my_future.MainActivity;
import com.example.my_future.R;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPager introViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //setup ViewPager

        List<IntroItem> mList = new ArrayList<>();
        mList.add(new IntroItem("List1","Loremasffsafffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",R.drawable.chat));
        mList.add(new IntroItem("List2","Loremasffsafffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",R.drawable.eat));
        mList.add(new IntroItem("List3","Loremasffsafffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",R.drawable.profile));

        screenPager = findViewById(R.id.Pager1);
        introViewPager = new IntroViewPager(this,mList);
        screenPager.setAdapter(introViewPager);
    }

    public void onClickLast(View view) {
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
    }
}